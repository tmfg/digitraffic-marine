package fi.livi.digitraffic.meri.service.winternavigation;

import static fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository.UpdatedName.WINTER_NAVIGATION_PORTS;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import fi.livi.digitraffic.meri.service.winternavigation.dto.PortDto;
import fi.livi.digitraffic.meri.service.winternavigation.dto.PortRestrictionDto;
import fi.livi.digitraffic.meri.service.winternavigation.dto.PortsDto;

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

        final PortsDto data = winterNavigationClient.getWinterNavigationPorts();

        final Map<Boolean, List<PortDto>> ports =
                data.ports.stream().collect(Collectors.partitioningBy(p -> !StringUtils.isEmpty(p.portInfo.locode)));

        final List<PortDto> portsWithoutLocode = ports.get(false);
        if (!portsWithoutLocode.isEmpty()) {
            log.info("Received {} winter navigation port(s) with missing locode. PortIds: {}",
                     portsWithoutLocode.size(), portsWithoutLocode.stream().map(p -> p.portInfo.portId).collect(Collectors.joining(", ")));
        }

        final List<String> locodes = ports.get(true).stream().map(p -> p.portInfo.locode).collect(Collectors.toList());
        if (!locodes.isEmpty()) {
            winterNavigationRepository.setRemovedPortsObsolete(locodes);
        }

        final List<WinterNavigationPort> added = new ArrayList<>();
        final List<WinterNavigationPort> updated = new ArrayList<>();

        StopWatch stopWatch = StopWatch.createStarted();
        ports.get(true).forEach(p -> update(p, added, updated));
        winterNavigationRepository.save(added);
        stopWatch.stop();

        log.info("Added {} winter navigation port(s), updated {}, took {} ms", added.size(), updated.size(), stopWatch.getTime());

        updatedTimestampRepository.setUpdated(WINTER_NAVIGATION_PORTS.name(),
                                              Date.from(data.dataValidTime.toInstant()),
                                              getClass().getSimpleName());
    }

    private void update(final PortDto port, final List<WinterNavigationPort> added, final List<WinterNavigationPort> updated) {
        final WinterNavigationPort old = winterNavigationRepository.findOne(port.portInfo.portId);

        if (old == null) {
            added.add(addNew(port));
        } else {
            updated.add(update(old, port));
        }
    }

    private static WinterNavigationPort addNew(final PortDto port) {
        WinterNavigationPort p = new WinterNavigationPort();

        updateData(p, port);

        return p;
    }

    private static WinterNavigationPort update(final WinterNavigationPort p, final PortDto port) {

        updateData(p, port);

        return p;
    }

    private static void updateData(final WinterNavigationPort p, final PortDto port) {
        p.setLocode(port.portInfo.locode);
        p.setName(port.portInfo.name);
        p.setLongitude(port.portInfo.lon);
        p.setLatitude(port.portInfo.lat);
        p.setNationality(port.portInfo.nationality);
        p.setSeaArea(port.portInfo.seaArea);
        p.setObsoleteDate(null);
        if (port.restrictions != null) {
            updatePortRestrictions(p, port.restrictions);
        }
    }

    private static void updatePortRestrictions(final WinterNavigationPort p, final List<PortRestrictionDto> restrictions) {
        p.getPortRestrictions().clear();

        int orderNumber = 1;
        for (final PortRestrictionDto restriction : restrictions) {
            PortRestriction pr = new PortRestriction();
            pr.setLocode(p.getLocode());
            pr.setOrderNumber(orderNumber);
            pr.setCurrent(restriction.isCurrent);
            pr.setPortRestricted(restriction.portRestricted);
            pr.setPortClosed(restriction.portClosed);
            pr.setIssueTime(findTimestamp(restriction.issueTime));
            pr.setLastModified(findTimestamp(restriction.timeStamp));
            pr.setValidFrom(restriction.validFrom);
            pr.setValidUntil(restriction.validUntil);
            pr.setRawText(restriction.rawText);
            pr.setFormattedText(restriction.formattedText);
            p.getPortRestrictions().add(pr);
            orderNumber++;
        }
    }

    public static Timestamp findTimestamp(final ZonedDateTime issueTime) {
        return issueTime == null ? null : Timestamp.from(issueTime.toInstant());
    }
}
