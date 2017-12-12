package fi.livi.digitraffic.meri.service.winternavigation;

import static fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository.UpdatedName.WINTER_NAVIGATION_PORTS;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.dao.winternavigation.WinterNavigationPortRepository;
import fi.livi.digitraffic.meri.domain.winternavigation.PortRestriction;
import fi.livi.digitraffic.meri.domain.winternavigation.WinterNavigationPort;
import ibnet_baltice_ports.Port;
import ibnet_baltice_ports.Ports;
import ibnet_baltice_ports.Restriction;

@Service
public class WinterNavigationPortUpdater {

    private final WinterNavigationClient winterNavigationClient;

    private final WinterNavigationPortRepository winterNavigationRepository;

    private final UpdatedTimestampRepository updatedTimestampRepository;

    private final static Logger log = LoggerFactory.getLogger(WinterNavigationPortUpdater.class);

    @Autowired
    public WinterNavigationPortUpdater(final WinterNavigationClient winterNavigationClient,
                                       final WinterNavigationPortRepository winterNavigationRepository,
                                       final UpdatedTimestampRepository updatedTimestampRepository) {
        this.winterNavigationClient = winterNavigationClient;
        this.winterNavigationRepository = winterNavigationRepository;
        this.updatedTimestampRepository = updatedTimestampRepository;
    }

    @Transactional
    public void updateWinterNavigationPorts() {

        final Ports data = winterNavigationClient.getWinterNavigationPorts();

        final Map<Boolean, List<Port>> ports =
            data.getPort().stream().collect(Collectors.partitioningBy(p -> !StringUtils.isEmpty(p.getPortInfo().getLocode())));

        final List<Port> portsWithoutLocode = ports.get(false);
        if (!portsWithoutLocode.isEmpty()) {
            log.info("Received winterNavigationPorts={} with missing locode. PortIds={}",
                     portsWithoutLocode.size(), portsWithoutLocode.stream().map(p -> p.getPortInfo().getPortId()).collect(Collectors.joining(", ")));
        }

        // Make all ports obsolete before update
        final List<String> locodes = ports.get(true).stream().map(p -> p.getPortInfo().getLocode()).collect(Collectors.toList());
        if (!locodes.isEmpty()) {
            winterNavigationRepository.setRemovedPortsObsolete(locodes);
        }

        final List<WinterNavigationPort> added = new ArrayList<>();
        final List<WinterNavigationPort> updated = new ArrayList<>();

        StopWatch stopWatch = StopWatch.createStarted();
        ports.get(true).forEach(p -> update(p, added, updated));
        winterNavigationRepository.save(added);
        stopWatch.stop();

        log.info("method=updateWinterNavigationPorts addedPorts={} , updatedPorts={}, took={} ms", added.size(), updated.size(), stopWatch.getTime());

        updatedTimestampRepository.setUpdated(WINTER_NAVIGATION_PORTS.name(),
                                              Date.from(data.getDataValidTime().toGregorianCalendar().toInstant()),
                                              getClass().getSimpleName());
    }

    private void update(final Port port, final List<WinterNavigationPort> added, final List<WinterNavigationPort> updated) {
        final WinterNavigationPort old = winterNavigationRepository.findOne(port.getPortInfo().getPortId());

        if (old == null) {
            added.add(addNew(port));
        } else {
            updated.add(update(old, port));
        }
    }

    private static WinterNavigationPort addNew(final Port port) {
        WinterNavigationPort p = new WinterNavigationPort();

        updateData(p, port);

        return p;
    }

    private static WinterNavigationPort update(final WinterNavigationPort p, final Port port) {

        updateData(p, port);

        return p;
    }

    private static void updateData(final WinterNavigationPort p, final Port port) {
        p.setLocode(port.getPortInfo().getLocode());
        p.setName(port.getPortInfo().getName().getValue());
        p.setLongitude(port.getPortInfo().getLon().doubleValue());
        p.setLatitude(port.getPortInfo().getLat().doubleValue());
        p.setNationality(port.getPortInfo().getNationality());
        p.setSeaArea(port.getPortInfo().getSeaArea());
        p.setObsoleteDate(null);
        p.getPortRestrictions().clear();
        if (port.getRestrictions() != null) {
            updatePortRestrictions(p, port.getRestrictions().getRestriction());
        }
    }

    private static void updatePortRestrictions(final WinterNavigationPort p, final List<Restriction> restrictions) {

        int orderNumber = 1;
        for (final Restriction restriction : restrictions) {
            PortRestriction pr = new PortRestriction();
            pr.setLocode(p.getLocode());
            pr.setOrderNumber(orderNumber);
            pr.setCurrent(restriction.isIsCurrent());
            pr.setPortRestricted(restriction.isPortRestricted());
            pr.setPortClosed(restriction.isPortClosed());
            pr.setIssueTime(findTimestamp(restriction.getIssueTime())); // FIXME ZonedDateTime
            pr.setLastModified(findTimestamp(restriction.getTimeStamp())); // FIXME
            pr.setValidFrom(findDate(restriction.getValidFrom())); // FIXME
            pr.setValidUntil(findDate(restriction.getValidUntil())); // FIXME
            pr.setRawText(restriction.getRawText());
            pr.setFormattedText(restriction.getFormattedText());
            p.getPortRestrictions().add(pr);
            orderNumber++;
        }
    }

    public static Timestamp findTimestamp(final ZonedDateTime issueTime) { // FIXME
        return issueTime == null ? null : Timestamp.from(issueTime.toInstant());
    }

    public static Timestamp findTimestamp(final XMLGregorianCalendar cal) { // FIXME
        return cal == null ? null : Timestamp.from(cal.toGregorianCalendar().toInstant());
    }

    public static Date findDate(final XMLGregorianCalendar cal) {
        return cal == null ? null : Date.from(cal.toGregorianCalendar().toInstant());
    }
}
