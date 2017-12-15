package fi.livi.digitraffic.meri.service.portnet;

import static fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository.UpdatedName.PORT_CALLS;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.dao.portnet.PortCallRepository;
import fi.livi.digitraffic.meri.domain.portnet.PortCall;
import fi.livi.digitraffic.meri.portnet.xsd.AgentInfo;
import fi.livi.digitraffic.meri.portnet.xsd.BerthDetails;
import fi.livi.digitraffic.meri.portnet.xsd.CargoInfo;
import fi.livi.digitraffic.meri.portnet.xsd.ImoInformation;
import fi.livi.digitraffic.meri.portnet.xsd.PortAreaDetails;
import fi.livi.digitraffic.meri.portnet.xsd.PortCallDetails;
import fi.livi.digitraffic.meri.portnet.xsd.PortCallDirection;
import fi.livi.digitraffic.meri.portnet.xsd.PortCallList;
import fi.livi.digitraffic.meri.portnet.xsd.PortCallNotification;
import fi.livi.digitraffic.meri.portnet.xsd.TimeSource;
import fi.livi.digitraffic.meri.portnet.xsd.VesselDetails;

@Service
public class PortCallUpdater {
    private final PortCallRepository portCallRepository;
    private final UpdatedTimestampRepository updatedTimestampRepository;

    private final PortCallClient portCallClient;

    private static final Logger log = LoggerFactory.getLogger(PortCallUpdater.class);

    private final int maxTimeFrameToFetch;
    // To make sure that no data is missed because of update turn changing from a node to another
    private final int overlapTimeFrame;

    public PortCallUpdater(final PortCallRepository portCallRepository,
                           final UpdatedTimestampRepository updatedTimestampRepository,
                           final PortCallClient portCallClient,
                           @Value("${portCallUpdateJob.maxTimeFrameToFetch}") final int maxTimeFrameToFetch,
                           @Value("${portCallUpdateJob.overlapTimeFrame}") final int overlapTimeFrame) {
        this.portCallRepository = portCallRepository;
        this.updatedTimestampRepository = updatedTimestampRepository;
        this.portCallClient = portCallClient;
        this.maxTimeFrameToFetch = maxTimeFrameToFetch;
        this.overlapTimeFrame = overlapTimeFrame;
    }

    @Transactional
    public void update() {
        final Instant lastUpdated = updatedTimestampRepository.getLastUpdated(PORT_CALLS.name());
        final Instant now = Instant.now();
        final Instant from = lastUpdated == null ? now.minusMillis(maxTimeFrameToFetch) : lastUpdated.minusMillis(overlapTimeFrame);
        final Instant to = now.toEpochMilli() - from.toEpochMilli() > maxTimeFrameToFetch ? from.plusMillis(maxTimeFrameToFetch) : now;

        updatePortCalls(from, to);
    }

    @Transactional
    public void updatePortCalls(final Instant from, final Instant to) {
        log.info("Fetching port calls from server");

        final PortCallList list = portCallClient.getList(from, to);

        if(isListOk(list)) {
            updatedTimestampRepository.setUpdated(PORT_CALLS.name(), ZonedDateTime.from(to), getClass().getSimpleName());

            final List<PortCall> added = new ArrayList<>();
            final List<PortCall> updated = new ArrayList<>();
            final StopWatch watch = new StopWatch();

            watch.start();
            list.getPortCallNotification().forEach(pcn -> update(pcn, added, updated));
            portCallRepository.save(added);
            watch.stop();

            log.info("portCallAddedCount={} port call, portCallUpdatedCount={}, tookMs={} .", added.size(), updated.size(), watch.getTime());
        }
    }

    private static boolean isListOk(final PortCallList list) {
        final String status = getStatusFromResponse(list);

        switch(status) {
        case "OK":
            log.info("notificationsFetchedCount={} notifications", CollectionUtils.size(list.getPortCallNotification()));
            break;
        case "NOT_FOUND":
            log.info("No port calls from server");
            break;
        default:
            log.error("error with status={}", status);
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
        final PortCall old = portCallRepository.findOne(pcn.getPortCallId().longValue());

        if(old == null) {
            added.add(addNew(pcn));
        } else {
            updated.add(update(old, pcn));
        }
    }

    private static PortCall update(final PortCall pc, final PortCallNotification pcn) {
        // update timestamp

        updateData(pc, pcn);

        return pc;
    }

    private static PortCall addNew(final PortCallNotification pcn) {
        final PortCall pc = new PortCall();

        updateData(pc, pcn);

        return pc;
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

        pc.setShipMasterArrival(det.getShipMasterArrival());
        pc.setShipMasterDeparture(det.getShipMasterDeparture());
        pc.setManagementNameArrival(det.getManagementNameArrival());
        pc.setManagementNameDeparture(det.getManagementNameDeparture());
        pc.setForwarderNameArrival(det.getForwarderNameArrival());
        pc.setForwarderNameDeparture(det.getForwarderNameDeparture());
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
