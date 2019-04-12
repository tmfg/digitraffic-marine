package fi.livi.digitraffic.meri.service.portnet.vesseldetails;

import static fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository.UpdatedName.VESSEL_DETAILS;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.dao.portnet.VesselDetailsRepository;
import fi.livi.digitraffic.meri.domain.portnet.vesseldetails.VesselDetails;
import fi.livi.digitraffic.meri.portnet.vesseldetails.xsd.VesselList;

@Service
@ConditionalOnNotWebApplication
public class VesselDetailsUpdater {
    private final VesselDetailsRepository vesselDetailsRepository;
    private final UpdatedTimestampRepository updatedTimestampRepository;

    private final VesselDetailsClient vesselDetailsClient;

    private static final Logger log = LoggerFactory.getLogger(VesselDetailsUpdater.class);

    @Autowired
    public VesselDetailsUpdater(final VesselDetailsRepository vesselDetailsRepository,
                                final VesselDetailsClient vesselDetailsClient,
                                final UpdatedTimestampRepository updatedTimestampRepository) {
        this.vesselDetailsRepository = vesselDetailsRepository;
        this.vesselDetailsClient = vesselDetailsClient;
        this.updatedTimestampRepository = updatedTimestampRepository;
    }

    @Transactional
    public void update() {
        final ZonedDateTime lastUpdated = updatedTimestampRepository.findLastUpdated(VESSEL_DETAILS);

        updateVesselDetails(lastUpdated);
    }

    protected void updateVesselDetails(final ZonedDateTime lastUpdated) {
        final ZonedDateTime now = ZonedDateTime.now();
        final ZonedDateTime from = lastUpdated == null ? now.minus(1, ChronoUnit.DAYS) : lastUpdated;

        final VesselList vesselList = vesselDetailsClient.getVesselList(from);

        if (isListOk(vesselList)) {
            updatedTimestampRepository.setUpdated(VESSEL_DETAILS.name(), now, getClass().getSimpleName());

            final List<VesselDetails> added = new ArrayList<>();
            final List<VesselDetails> updated = new ArrayList<>();
            final StopWatch watch = new StopWatch();

            watch.start();
            vesselList.getVesselDetails().forEach(vesselDetails -> update(vesselDetails, added, updated));
            vesselDetailsRepository.saveAll(added);
            watch.stop();

            log.info("vesselDetailAddedCount={} vesselDetailUpdatedCount={} tookMs={} .", added.size(), updated.size(), watch.getTime());
        }
    }

    private void update(final fi.livi.digitraffic.meri.portnet.vesseldetails.xsd.VesselDetails vd, final List<VesselDetails> added, final List<VesselDetails> updated) {
        final VesselDetails old = vesselDetailsRepository.findById(vd.getIdentificationData().getVesselId().longValue()).orElse(null);

        if(old != null) {
            old.setAll(vd);
            updated.add(old);
        } else {
            final VesselDetails vesselDetails = new VesselDetails();
            vesselDetails.setAll(vd);
            added.add(vesselDetails);
        }
    }

    private static boolean isListOk(final VesselList list) {
        final String status = getStatusFromResponse(list);

        switch(status) {
        case "OK":
            log.info("vesselDetailFetchedCount={} vessel details", CollectionUtils.size(list.getVesselDetails()));
            break;
        case "NOT_FOUND":
            log.info("No vessel details from server");
            break;
        default:
            log.error("error with status={}", status);
            return false;
        }

        return true;
    }

    private static String getStatusFromResponse(final VesselList list) {
        if(list == null) {
            return "ERROR";
        }

        return list.getHeader() == null ? "NOT_FOUND" : list.getHeader().getResponseType().getStatus();
    }
}
