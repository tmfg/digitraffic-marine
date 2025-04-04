package fi.livi.digitraffic.meri.service.winternavigation;

import static fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository.UpdatedName.WINTER_NAVIGATION_PORTS;
import static fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository.UpdatedName.WINTER_NAVIGATION_PORTS_CHECK;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.dao.winternavigation.WinterNavigationPortRepository;
import fi.livi.digitraffic.meri.model.winternavigation.PortRestriction;
import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationPort;
import ibnet_baltice_ports.Port;
import ibnet_baltice_ports.Ports;
import ibnet_baltice_ports.Restriction;
import ibnet_baltice_ports.Restrictions;

@Service
@ConditionalOnNotWebApplication
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

    /**
     * 1. Get winter navigation ports from an external source
     * 2. Insert / update database
     *
     * @return total number of added or updated ports
     */
    @Transactional
    public int updateWinterNavigationPorts() {
        final Ports data;

        try {
            data = winterNavigationClient.getWinterNavigationPorts();
        } catch (final Exception e) {
            SoapFaultLogger.logException(log, e);

            return -1;
        }

        final Map<Boolean, List<Port>> ports =
            data.getPort().stream().collect(Collectors.partitioningBy(p -> !StringUtils.isEmpty(p.getPortInfo().getLocode())));

        final List<Port> portsWithoutLocode = ports.get(false);
        final List<Port> portsWithLocode = ports.get(true);

        if (!portsWithoutLocode.isEmpty()) {
            log.warn("method=updateWinterNavigationPorts Received invalidPortCount={} with missing locode. PortIds={}",
                portsWithoutLocode.size(), portsWithoutLocode.stream().map(p -> p.getPortInfo().getPortId()).collect(Collectors.joining(", ")));
        }

        // Make all ports obsolete before update
        final List<String> locodes = portsWithLocode.stream().map(p -> p.getPortInfo().getLocode()).collect(Collectors.toList());
        if (!locodes.isEmpty()) {
            winterNavigationRepository.setRemovedPortsObsolete(locodes);
        }

        final List<WinterNavigationPort> added = new ArrayList<>();
        final List<WinterNavigationPort> updated = new ArrayList<>();

        final StopWatch stopWatch = StopWatch.createStarted();
        portsWithLocode.forEach(p -> update(p, added, updated));

        winterNavigationRepository.saveAll(added);
        stopWatch.stop();

        log.info("method=updateWinterNavigationPorts receivedPorts={} addedPorts={} , updatedPorts={} , tookMs={}",
            data.getPort().size(), added.size(), updated.size(), stopWatch.getTime());

        final Instant now = Instant.now();
        if (!added.isEmpty() || !updated.isEmpty()) {
            updatedTimestampRepository.setUpdated(WINTER_NAVIGATION_PORTS, now, getClass().getSimpleName());
        }
        updatedTimestampRepository.setUpdated(WINTER_NAVIGATION_PORTS_CHECK, now, getClass().getSimpleName());

        return added.size() + updated.size();
    }

    private void update(final Port port, final List<WinterNavigationPort> added, final List<WinterNavigationPort> updated) {
        // filter out ports with invalid LOCODEs
        if (port.getPortInfo().getLocode().length() > 7) {
            log.warn("method=updateWinterNavigationPorts received invalid (reason: over 7 characters) LOCODE: {}", port.getPortInfo().getLocode());
            return;
        }
        final WinterNavigationPort old = winterNavigationRepository.findById(port.getPortInfo().getLocode()).orElse(null);

        if (old == null) {
            added.add(addNew(port));
        } else {
            updated.add(update(old, port));
        }
    }

    private static WinterNavigationPort addNew(final Port port) {
        final WinterNavigationPort p = new WinterNavigationPort();

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

        updatePortRestrictions(p, port.getRestrictions());
    }

    private static void updatePortRestrictions(final WinterNavigationPort p, final Restrictions restrictions) {
        p.getPortRestrictions().clear();

        if (restrictions == null) {
            return;
        }

        int orderNumber = 1;
        for (final Restriction restriction : restrictions.getRestriction()) {
            final PortRestriction pr = new PortRestriction();
            pr.setLocode(p.getLocode());
            pr.setOrderNumber(orderNumber);
            pr.setCurrent(restriction.isIsCurrent());
            pr.setPortRestricted(restriction.isPortRestricted());
            pr.setPortClosed(restriction.isPortClosed());
            pr.setIssueTime(UpdaterService.findZonedDateTime(restriction.getIssueTime()));
            pr.setLastModified(UpdaterService.findZonedDateTime(restriction.getTimeStamp()));
            pr.setValidFrom(UpdaterService.findDate(restriction.getValidFrom()));
            pr.setValidUntil(UpdaterService.findDate(restriction.getValidUntil()));
            pr.setRawText(restriction.getRawText());
            pr.setFormattedText(restriction.getFormattedText());
            p.getPortRestrictions().add(pr);
            orderNumber++;
        }
    }

}
