package fi.livi.digitraffic.meri.service.portnet;

import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.dao.portnet.PortCallRepository;
import fi.livi.digitraffic.meri.domain.portnet.PortCall;
import fi.livi.digitraffic.meri.portnet.xsd.*;
import fi.livi.digitraffic.meri.util.StringUtil;
import fi.livi.digitraffic.meri.util.TimeUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository.UpdatedName.PORT_CALLS;
import static java.time.temporal.ChronoUnit.MILLIS;

@Service
@ConditionalOnNotWebApplication
public class PortCallUpdater {
    private final PortCallRepository portCallRepository;
    private final UpdatedTimestampRepository updatedTimestampRepository;

    private final PortCallClient portCallClient;
    private final PortcallEstimateUpdater portcallEstimateUpdater;

    private static final Logger log = LoggerFactory.getLogger(PortCallUpdater.class);

    private final int maxTimeFrameToFetch;
    // To make sure that no data is missed because of update turn changing from a node to another
    private final int overlapTimeFrame;

    private static final Timestamp MIN_TIMESTAMP = new Timestamp(Instant.parse("2016-01-01T00:00:00.00Z").toEpochMilli());

    public PortCallUpdater(final PortCallRepository portCallRepository,
                           final UpdatedTimestampRepository updatedTimestampRepository,
                           final PortCallClient portCallClient,
                           final Optional<PortcallEstimateUpdater> portcallEstimateUpdater,
                           @Value("${portCallUpdateJob.maxTimeFrameToFetch:0}") final int maxTimeFrameToFetch,
                           @Value("${portCallUpdateJob.overlapTimeFrame:0}") final int overlapTimeFrame) {
        this.portCallRepository = portCallRepository;
        this.updatedTimestampRepository = updatedTimestampRepository;
        this.portCallClient = portCallClient;
        this.portcallEstimateUpdater = portcallEstimateUpdater.orElse(null);
        this.maxTimeFrameToFetch = maxTimeFrameToFetch;
        this.overlapTimeFrame = overlapTimeFrame;
    }

    @Transactional
    public void update() {
        final ZonedDateTime lastUpdated = updatedTimestampRepository.findLastUpdated(PORT_CALLS);
        final ZonedDateTime now = ZonedDateTime.now().minusMinutes(1); // be sure not to go into future
        final ZonedDateTime from = lastUpdated == null ? now.minus(maxTimeFrameToFetch, MILLIS) : lastUpdated.minus(overlapTimeFrame, MILLIS);
        final ZonedDateTime to = TimeUtil.millisBetween(now, from) > maxTimeFrameToFetch ? from.plus(maxTimeFrameToFetch, MILLIS) : now;

        updatePortCalls(from, to);
    }

    @Transactional
    public void updatePortCalls(final ZonedDateTime from, final ZonedDateTime to) {
        // if timestampcheck failed, try again!
        if(!getAndUpdate(from, to, false)) {
            log.error("retrying port calls");

            // second time, set portcalls updated anyway
            getAndUpdate(from, to, true);
        }
    }

    /// return true if the timestampcheck was successful
    private boolean getAndUpdate(final ZonedDateTime from, final ZonedDateTime to, final boolean setUpdatedOnFail) {
        final PortCallList list = portCallClient.getList(from, to);

        // if error from server, no need to retry
        if(!isListOk(list)) {
            return true;
        }

        final boolean timeStampsOk = checkTimestamps(list);

        if(timeStampsOk) {
            updatePortCalls(list);
        }

        // set portcalls updated if timestamps were ok or tried second time
        if(timeStampsOk || setUpdatedOnFail) {
            updatedTimestampRepository.setUpdated(PORT_CALLS, to, getClass().getSimpleName());
        }

        return timeStampsOk;
    }

    private void updatePortCalls(final PortCallList list) {
        final List<PortCall> added = new ArrayList<>();
        final List<PortCall> updated = new ArrayList<>();

        final StopWatch watch = StopWatch.createStarted();
        list.getPortCallNotification().forEach(pcn -> {
            update(pcn, added, updated);
            if (portcallEstimateUpdater != null) {
                portcallEstimateUpdater.updatePortcallEstimate(pcn);
            }
        });
        portCallRepository.saveAll(added);

        log.info("portCallAddedCount={} portCallUpdatedCount={} tookMs={} .", added.size(), updated.size(), watch.getTime());
    }

    private boolean checkTimestamps(final PortCallList list) {
        for(final PortCallNotification pcn : list.getPortCallNotification()) {
            final Timestamp now = new Timestamp(Instant.now().toEpochMilli());
            final Timestamp timestamp = getTimestamp(pcn.getPortCallTimestamp());

            if(timestamp == null) {
                log.warn("method=checkTimestamps portCallId={} currentTimestamp={} portCallList={}",
                         pcn.getPortCallId().longValue(), now.getTime(), StringUtil.toJsonStringLogSafe(list));
            } else if(timestamp.after(now)) {
                log.warn("method=checkTimestamps portCallId={} futureTimestamp={} currentTimestamp={} portCallList={}",
                         pcn.getPortCallId().longValue(), timestamp.getTime(), now.getTime(), StringUtil.toJsonStringLogSafe(list));
            } else if(timestamp.before(MIN_TIMESTAMP)) {
                log.warn("method=checkTimestamps portCallId={} pastTimestamp={} portCallList={}",
                         pcn.getPortCallId().longValue(), timestamp.getTime(), StringUtil.toJsonStringLogSafe(list));
                return false;
            }
        }

        return true;
    }

    private static boolean isListOk(final PortCallList list) {
        final String status = getStatusFromResponse(list);

        switch(status) {
        case "OK":
            log.info("notificationsFetchedCount={}", CollectionUtils.size(list.getPortCallNotification()));
            break;
        case "NOT_FOUND":
            log.info("No port calls from server");
            break;
        default:
            log.error("error status={}", status);
            return false;
        }

        return true;
    }

    private static String getStatusFromResponse(final PortCallList list) {
        if(list == null) {
            return "ERROR";
        }

        return list.getHeader() == null ? "NOT_FOUND" : list.getHeader().getResponseType().getStatus();
    }

    private void update(final PortCallNotification pcn, final List<PortCall> added, final List<PortCall> updated) {
        final PortCall old = portCallRepository.findById(pcn.getPortCallId().longValue()).orElse(null);

        if(old == null) {
            final PortCall pc = new PortCall();

            updateData(pc, pcn);

            added.add(pc);
        } else {
            updateData(old, pcn);

            updated.add(old);
        }
    }

    private static void updateData(final PortCall pc, final PortCallNotification pcn) {
        final PortCallDetails det = pcn.getPortCallDetails();
        final VesselDetails.IdentificationData id = det.getVesselDetails().getIdentificationData();

        pc.setPortCallId(pcn.getPortCallId().longValue());
        pc.setPortCallTimestamp(getTimestamp(pcn.getPortCallTimestamp()));

        pc.setCustomsReference(det.getCustomsReference());
        pc.setPortToVisit(det.getPortToVisit());
        pc.setNextPort(det.getNextPort());
        pc.setPrevPort(det.getPrevPort());
        pc.setDomesticTrafficArrival(det.isDomesticTrafficArrival());
        pc.setDomesticTrafficDeparture(det.isDomesticTrafficDeparture());
        pc.setArrivalWithCargo(det.isArrivalWithCargo());
        pc.setNotLoading(det.isNotLoading());
        pc.setDischarge(det.getDischarge() == null ? null : det.getDischarge().intValue());

        //pc.setShipMasterArrival(det.getShipMasterArrival());
        //pc.setShipMasterDeparture(det.getShipMasterDeparture());
        //pc.setManagementNameArrival(det.getManagementNameArrival());
        //pc.setManagementNameDeparture(det.getManagementNameDeparture());
        //pc.setForwarderNameArrival(det.getForwarderNameArrival());
        //pc.setForwarderNameDeparture(det.getForwarderNameDeparture());
        pc.setFreeTextArrival(det.getFreeTextArrival());
        pc.setFreeTextDeparture(det.getFreeTextDeparture());

        pc.setVesselName(id.getName());
        pc.setVesselNamePrefix(id.getNamePrefix());
        pc.setRadioCallSign(id.getRadioCallSign());
        pc.setRadioCallSignType(id.getRadioCallSignType().value());
        pc.setImoLloyds(getInteger(id.getImoLloyds()));
        pc.setMmsi(getInteger(id.getMmsi()));
        pc.setNationality(det.getVesselDetails().getRegistrationData().getNationality());
        pc.setVesselTypeCode(getInteger(det.getVesselDetails().getConstructionData().getVesselTypeCode()));

        pc.setCertificateIssuer(det.getSecurityMeasures().getCertificateIssuer());
        pc.setCertificateStartDate(getTimestamp(det.getSecurityMeasures().getCertificateStartDate()));
        pc.setCertificateEndDate(getTimestamp(det.getSecurityMeasures().getCertificateEndDate()));
        pc.setCurrentSecurityLevel(getInteger(det.getSecurityMeasures().getCurrentSecurityLevel()));

        updatePortAreaDetails(pc, pcn);
        updateImoInformation(pc, det);
        updateAgentInfo(pc, det);
    }

    private static void updateAgentInfo(final PortCall pc, final PortCallDetails det) {
        pc.getAgentInfo().clear();

        for(final AgentInfo ai : det.getAgentInfo()) {
            final fi.livi.digitraffic.meri.domain.portnet.AgentInfo newAi = new fi.livi.digitraffic.meri.domain.portnet.AgentInfo();

            newAi.setEdiNumber(ai.getEdiNumber());
            newAi.setName(ai.getName());
            newAi.setPortCallDirection(portCallDirection(ai.getPortCallDirection()));
            newAi.setRole(getInteger(ai.getRole()));

            pc.getAgentInfo().add(newAi);
        }
    }

    private static String portCallDirection(final PortCallDirection pcd) {
        return pcd == null ? null : pcd.value();
    }

    private static void updateImoInformation(final PortCall pc, final PortCallDetails det) {
        pc.getImoInformation().clear();

        for(final ImoInformation ii : det.getImoInformation()) {
            final fi.livi.digitraffic.meri.domain.portnet.ImoInformation newImo = new fi.livi.digitraffic.meri.domain.portnet.ImoInformation();

            newImo.setImoGeneralDeclaration(ii.getImoGeneralDeclaration());
            newImo.setBriefParticularsVoyage(ii.getBriefParticularsVoyage());
            newImo.setPortOfDischarge(ii.getPortOfDischarge());
            newImo.setNumberOfCrew(getInteger(ii.getNumberOfCrew()));
            newImo.setNumberOfPassangers(getInteger(ii.getNumberOfPassengers()));
            newImo.setCargoDeclarationOb(getInteger(ii.getCargoDeclarationsOnBoard()));
            newImo.setCrewListsOb(getInteger(ii.getCrewListsOnBoard()));
            newImo.setCrewsEffectsDeclarationsOb(getInteger(ii.getCrewsEffectsDeclarationsOnBoard()));
            newImo.setShipStoresDeclarationsOb(getInteger(ii.getShipStoresDeclarationsOnBoard()));
            newImo.setPassangerListsOb(getInteger(ii.getPassengerListsOnBoard()));
            newImo.setHealthDeclarationsOb(getInteger(ii.getHealthDeclarationsOnBoard()));

            pc.getImoInformation().add(newImo);
        }
    }

    private static void updatePortAreaDetails(final PortCall pc, final PortCallNotification pcn) {
        // port area details have no natural id, so they must be remade every time
        pc.getPortAreaDetails().clear();

        for(final PortAreaDetails pad : pcn.getPortCallDetails().getPortAreaDetails()) {
            final fi.livi.digitraffic.meri.domain.portnet.PortAreaDetails newPad = new fi.livi.digitraffic.meri.domain.portnet.PortAreaDetails();
            final BerthDetails bd = pad.getBerthDetails();

            newPad.setPortAreaCode(pad.getPortAreaCode());
            newPad.setPortAreaName(pad.getPortAreaName());
            newPad.setBerthCode(bd.getBerthCode());
            newPad.setBerthName(bd.getBerthName());

            newPad.setEta(getTimestamp(bd.getEta()));
            newPad.setEtaTimestamp(getTimestamp(bd.getEtaTimeStamp()));
            newPad.setEtaSource(getSource(bd.getEtaSource()));

            newPad.setEtd(getTimestamp(bd.getEtd()));
            newPad.setEtdTimestamp(getTimestamp(bd.getEtdTimeStamp()));
            newPad.setEtdSource(getSource(bd.getEtdSource()));

            newPad.setAta(getTimestamp(bd.getAta()));
            newPad.setAtaTimestamp(getTimestamp(bd.getAtaTimeStamp()));
            newPad.setAtaSource(getSource(bd.getAtaSource()));

            newPad.setAtd(getTimestamp(bd.getAtd()));
            newPad.setAtdTimestamp(getTimestamp(bd.getAtdTimeStamp()));
            newPad.setAtdSource(getSource(bd.getAtdSource()));

            newPad.setArrivalDraught(bd.getArrivalDraught());
            newPad.setDepartureDraught(bd.getDepartureDraught());

            updateCargoInfo(newPad, bd);

            pc.getPortAreaDetails().add(newPad);
        }
    }

    private static void updateCargoInfo(final fi.livi.digitraffic.meri.domain.portnet.PortAreaDetails newPad, final BerthDetails bd) {
        newPad.getCargoInfo().clear();

        for(final CargoInfo ci : bd.getCargoInfo()) {
            final fi.livi.digitraffic.meri.domain.portnet.CargoInfo newCi = new fi.livi.digitraffic.meri.domain.portnet.CargoInfo();

            newCi.setCargoAmount(ci.getCargoAmount());
            newCi.setCargoDescription(ci.getCargoDescription());
            newCi.setCargoDischargeCode(getInteger(ci.getCargoDischargeCode()));

            if(newCi.isNotEmpty()) {
                newPad.getCargoInfo().add(newCi);
            }
        }
    }

    private static Integer getInteger(final BigInteger bi) {
        return bi == null ? null : bi.intValue();
    }

    private static String getSource(final TimeSource tc) {
        return tc == null ? null : tc.value();
    }

    private static Timestamp getTimestamp(final XMLGregorianCalendar cal) {
        return cal == null ? null : new Timestamp(cal.toGregorianCalendar().getTimeInMillis());
    }
}
