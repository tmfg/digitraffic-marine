package fi.livi.digitraffic.meri.service.portnet;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.dao.portnet.PortCallRepository;
import fi.livi.digitraffic.meri.domain.portnet.PortCall;
import fi.livi.digitraffic.meri.portnet.xsd.PortCallList;
import fi.livi.digitraffic.meri.portnet.xsd.PortCallNotification;

@Service
public class PortCallUpdater {
    private final PortCallRepository portCallRepository;
    private final UpdatedTimestampRepository updatedTimestampRepository;

    private final PortCallFetcher portCallFetcher;

    private static final Logger log = LoggerFactory.getLogger(PortCallUpdater.class);

    public PortCallUpdater(final PortCallRepository portCallRepository,
                           final UpdatedTimestampRepository updatedTimestampRepository,
                           final PortCallFetcher portCallFetcher) {
        this.portCallRepository = portCallRepository;
        this.updatedTimestampRepository = updatedTimestampRepository;
        this.portCallFetcher = portCallFetcher;
    }

    @Transactional
    public void update() {
        log.info("Fetching port calls from server");

        final Instant lastUpdated = updatedTimestampRepository.getLastUpdated("PORT_CALLS");
        final Instant now = Instant.now();

        final PortCallList list = portCallFetcher.getList(lastUpdated, now);
        final String status = getStatusFromResponse(list);

        switch(status) {
        case "OK":
            break;
        case "NOT_FOUND":
            log.info("No port calls from server");
            break;
        default:
            log.error("error with status " + status);
            return;
        }

        updatedTimestampRepository.setUpdated("PORT_CALLS", Date.from(now), "PortCallUpdater");

        final List<PortCall> added = new ArrayList<>();
        final List<PortCall> updated = new ArrayList<>();
        final StopWatch watch = new StopWatch();

        watch.start();
        list.getPortCallNotification().forEach(pcn -> update(pcn, added, updated));
        portCallRepository.save(added);
        watch.stop();

        log.info(String.format("Added %d port call, updated %d, took %d ms.", added.size(), updated.size(), watch.getTime()));
    }

    private String getStatusFromResponse(final PortCallList list) {
        if(list.getHeader() == null) return "OK";

        return list.getHeader().getResponseType().getStatus();
    }

    private void update(final PortCallNotification pcn, final List<PortCall> added, final List<PortCall> updated) {
        final PortCall old = portCallRepository.findOne(pcn.getPortCallId().longValue());

        if(old == null) {
            added.add(addNew(pcn));
        } else {
            updated.add(update(old, pcn));
        }
    }

    private PortCall update(final PortCall pc, final PortCallNotification pcn) {
        // update timestamp

        updateData(pc, pcn);

        return pc;
    }

    private PortCall addNew(final PortCallNotification pcn) {
        final PortCall pc = new PortCall();

        updateData(pc, pcn);

        return pc;
    }

    private void updateData(final PortCall pc, final PortCallNotification pcn) {
        pc.setPortCallId(pcn.getPortCallId().longValue());
        pc.setPortToVisit(pcn.getPortCallDetails().getPortToVisit());
        pc.setNextPort(pcn.getPortCallDetails().getNextPort());
        pc.setPrevPort(pcn.getPortCallDetails().getPrevPort());
    }
}
