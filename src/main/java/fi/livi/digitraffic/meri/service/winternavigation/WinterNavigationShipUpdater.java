package fi.livi.digitraffic.meri.service.winternavigation;

import static fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository.UpdatedName.WINTER_NAVIGATION_SHIPS;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.dao.winternavigation.WinterNavigationShipRepository;
import fi.livi.digitraffic.meri.domain.winternavigation.ShipActivity;
import fi.livi.digitraffic.meri.domain.winternavigation.ShipPlannedActivity;
import fi.livi.digitraffic.meri.domain.winternavigation.ShipState;
import fi.livi.digitraffic.meri.domain.winternavigation.ShipVoyage;
import fi.livi.digitraffic.meri.domain.winternavigation.WinterNavigationShip;
import fi.livi.digitraffic.meri.service.winternavigation.dto.PositionAccuracy;
import fi.livi.digitraffic.meri.service.winternavigation.dto.PositionSource;
import ibnet_baltice_winterships.PlannedActivity;
import ibnet_baltice_winterships.WinterShip;
import ibnet_baltice_winterships.WinterShips;

@Service
@ConditionalOnNotWebApplication
public class WinterNavigationShipUpdater {

    private final static Logger log = LoggerFactory.getLogger(WinterNavigationShipUpdater.class);

    private final WinterNavigationClient winterNavigationClient;
    private final WinterNavigationShipRepository winterNavigationShipRepository;
    private final UpdatedTimestampRepository updatedTimestampRepository;

    @Autowired
    public WinterNavigationShipUpdater(final WinterNavigationClient winterNavigationClient,
                                       final WinterNavigationShipRepository winterNavigationShipRepository,
                                       final UpdatedTimestampRepository updatedTimestampRepository) {
        this.winterNavigationClient = winterNavigationClient;
        this.winterNavigationShipRepository = winterNavigationShipRepository;
        this.updatedTimestampRepository = updatedTimestampRepository;
    }

    /**
     * 1. Get winter navigation ships from an external source
     * 2. Insert / update database
     * @return total number of added or updated ships
     */
    @Transactional
    public int updateWinterNavigationShips() {
        final WinterShips data;

        try {
            data = winterNavigationClient.getWinterNavigationShips();
        } catch(final Exception e) {
            SoapFaultLogger.logException(log, e);

            return -1;
        }

        final List<WinterNavigationShip> added = new ArrayList<>();
        final List<WinterNavigationShip> updated = new ArrayList<>();

        final Map<String, WinterNavigationShip> shipsByVesselPK =
            winterNavigationShipRepository.findDistinctByOrderByVesselPK().collect(Collectors.toMap(s -> s.getVesselPK(), s -> s));

        final StopWatch stopWatch = StopWatch.createStarted();
        data.getWinterShip().forEach(ship -> update(ship, added, updated, shipsByVesselPK));
        winterNavigationShipRepository.saveAll(added);
        stopWatch.stop();

        log.info("method=updateWinterNavigationShips addedShips={} , updatedShips={} , tookMs={}", added.size(), updated.size(), stopWatch.getTime());

        updatedTimestampRepository.setUpdated(WINTER_NAVIGATION_SHIPS,
                                              data.getDataValidTime().toGregorianCalendar().toZonedDateTime(),
                                              getClass().getSimpleName());

        return added.size() + updated.size();
    }

    private void update(final WinterShip ship, final List<WinterNavigationShip> added, final List<WinterNavigationShip> updated,
                        final Map<String, WinterNavigationShip> shipsByVesselPK) {

        final WinterNavigationShip old = shipsByVesselPK.get(ship.getVesselPk());

        if (old == null) {
            added.add(addNew(ship));
        } else {
            updated.add(updateData(old, ship));
        }
    }

    private WinterNavigationShip addNew(final WinterShip ship) {
        final WinterNavigationShip s = new WinterNavigationShip();

        updateData(s, ship);
        return s;
    }

    private static WinterNavigationShip updateData(final WinterNavigationShip s, final WinterShip ship) {

        s.setVesselPK(ship.getVesselPk());
        s.setVesselSource(ship.getVesselSource());
        s.setMmsi(ship.getShipData().getMmsi());
        s.setName(ship.getShipData().getName());
        s.setImo(ship.getShipData().getImo());
        s.setNationality(ship.getShipData().getNationality());
        s.setNatCode(ship.getShipData().getNatcode());
        s.setAisLength(findDouble(ship.getShipData().getAisLength()));
        s.setAisWidth(findDouble(ship.getShipData().getAisWidth()));
        s.setAisShipType(findInteger(ship.getShipData().getAisShipType()));
        s.setCallSign(ship.getShipData().getCallsign());
        s.setDimensions(ship.getShipData().getDimensions());
        s.setDwt(findDouble(ship.getShipData().getDwt()));
        s.setIceClass(ship.getShipData().getIceclass());
        s.setNominalDraught(findDouble(ship.getShipData().getNominalDraught()));
        s.setLength(findDouble(ship.getShipData().getLength()));
        s.setWidth(findDouble(ship.getShipData().getWidth()));
        s.setShipType(ship.getShipData().getShipType());

        updateShipState(s, ship);
        updateShipVoyage(s, ship);
        updateShipActivities(s, ship);
        updateShipPlannedActivities(s, ship);
        return s;
    }

    private static void updateShipState(final WinterNavigationShip s, final WinterShip ship) {
        final ShipState shipState = s.getShipState() != null ? s.getShipState() : new ShipState();
        shipState.setWinterNavigationShip(s);

        if (ship.getShipState() != null) {
            shipState.setTimestamp(ship.getShipState().getTimestamp().toGregorianCalendar().toZonedDateTime());
            shipState.setLongitude(ship.getShipState().getLon().doubleValue());
            shipState.setLatitude(ship.getShipState().getLat().doubleValue());
            shipState.setPosPrintable(ship.getShipState().getPosPrintable());
            shipState.setPosAccuracy(PositionAccuracy.fromValue(ship.getShipState().getPosAccuracy().intValue()));
            shipState.setPosSource(PositionSource.valueOf(ship.getShipState().getPosSource()));
            shipState.setPosArea(ship.getShipState().getPosArea());
            shipState.setSpeed(findDouble(ship.getShipState().getSpeed()));
            shipState.setCourse(findDouble(ship.getShipState().getCourse()));
            shipState.setHeading(findDouble(ship.getShipState().getHeading()));
            shipState.setAisDraught(findDouble(ship.getShipState().getAisDraught()));
            shipState.setAisState(findInteger(ship.getShipState().getAisState()));
            shipState.setAisStateText(ship.getShipState().getAisStateText());
            shipState.setAisDestination(ship.getShipState().getAisDestination());
            shipState.setMovingSince(findZonedDateTime(ship.getShipState().getMovingSince()));
            shipState.setStoppedSince(findZonedDateTime(ship.getShipState().getStoppedSince()));
            shipState.setInactiveSince(findZonedDateTime(ship.getShipState().getInactiveSince()));
        }
        s.setShipState(shipState);
    }

    private static void updateShipVoyage(final WinterNavigationShip s, final WinterShip ship) {
        final ShipVoyage shipVoyage = s.getShipVoyage() != null ? s.getShipVoyage() : new ShipVoyage();
        shipVoyage.setWinterNavigationShip(s);

        if (ship.getShipVoyage() != null) {
            shipVoyage.setInLocode(ship.getShipVoyage().getInLocode());
            shipVoyage.setInName(ship.getShipVoyage().getInName());
            shipVoyage.setInAta(findZonedDateTime(ship.getShipVoyage().getInAta()));
            shipVoyage.setInEtd(findZonedDateTime(ship.getShipVoyage().getInEtd()));
            shipVoyage.setFromLocode(ship.getShipVoyage().getFromLocode());
            shipVoyage.setFromName(ship.getShipVoyage().getFromName());
            shipVoyage.setFromAtd(findZonedDateTime(ship.getShipVoyage().getFromAtd()));
            shipVoyage.setDestLocode(ship.getShipVoyage().getDestLocode());
            shipVoyage.setDestName(ship.getShipVoyage().getDestName());
            shipVoyage.setDestEta(findZonedDateTime(ship.getShipVoyage().getDestEta()));
        }
        s.setShipVoyage(shipVoyage);
    }

    private static void updateShipActivities(final WinterNavigationShip s, final WinterShip ship) {
        s.getShipActivities().clear();

        if (ship.getShipActivities() == null) {
            return;
        }

        int orderNumber = 1;
        for (final ibnet_baltice_winterships.ShipActivity shipActivity : ship.getShipActivities().getShipActivity()) {
            final ShipActivity activity = new ShipActivity();
            activity.setVesselPK(ship.getVesselPk());
            activity.setOrderNumber(orderNumber);
            activity.setActivityType(shipActivity.getActivityType());
            activity.setActivityText(shipActivity.getActivityText());
            activity.setBeginTime(findZonedDateTime(shipActivity.getBegintime()));
            activity.setEndTime(findZonedDateTime(shipActivity.getEndtime()));
            activity.setActivityComment(shipActivity.getComment());
            activity.setTimestampBegin(shipActivity.getTimestampBegin().toGregorianCalendar().toZonedDateTime());
            activity.setTimestampEnd(findZonedDateTime(shipActivity.getTimestampEnd()));
            activity.setTimestampCanceled(findZonedDateTime(shipActivity.getTimestampCanceled()));
            activity.setConvoyOrder(findInteger(shipActivity.getConvoyOrder()));
            activity.setOperatedVesselPK(shipActivity.getOperatedVesselPk());
            activity.setOperatedVesselName(shipActivity.getOperatedVesselName());
            activity.setOperatingIcebreakerPK(shipActivity.getOperatingIbPk());
            activity.setOperatingIcebreakerName(shipActivity.getOperatingIbName());
            s.getShipActivities().add(activity);
            orderNumber++;
        }
    }

    private static void updateShipPlannedActivities(final WinterNavigationShip s, final WinterShip ship) {
        s.getShipPlannedActivities().clear();

        if (ship.getPlannedActivities() == null) {
            return;
        }

        int orderNumber = 1;
        for (final PlannedActivity plannedActivity : ship.getPlannedActivities().getPlannedActivity()) {
            final ShipPlannedActivity activity = new ShipPlannedActivity();
            activity.setVesselPK(ship.getVesselPk());
            activity.setOrderNumber(orderNumber);
            activity.setActivityType(plannedActivity.getActivityType());
            activity.setActivityText(plannedActivity.getActivityText());
            activity.setPlannedWhen(plannedActivity.getPlannedWhen());
            activity.setPlannedWhere(plannedActivity.getPlannedWhere());
            activity.setPlanComment(plannedActivity.getPlanComment());
            activity.setOrdering(findInteger(plannedActivity.getOrdering()));
            activity.setPlannedVesselPK(plannedActivity.getPlannedVesselPk());
            activity.setPlanningVesselPK(plannedActivity.getPlanningVesselPk());
            activity.setPlanTimestamp(findZonedDateTime(plannedActivity.getPlanTimestamp()));
            activity.setPlanTimestampCanceled(findZonedDateTime(plannedActivity.getPlanTimestampCanceled()));
            activity.setPlanTimestampRealized(findZonedDateTime(plannedActivity.getPlanTimestampRealized()));
            s.getShipPlannedActivities().add(activity);
            orderNumber++;
        }
    }

    private static Double findDouble(final BigDecimal value) {
        return value == null ? null : value.doubleValue();
    }

    private static Integer findInteger(final BigInteger value) {
        return value == null ? null : value.intValue();
    }

    static ZonedDateTime findZonedDateTime(final XMLGregorianCalendar cal) {
        return cal == null ? null : cal.toGregorianCalendar().toZonedDateTime();
    }

    static java.util.Date findDate(final XMLGregorianCalendar cal) {
        return cal == null ? null : java.util.Date.from(cal.toGregorianCalendar().toInstant());
    }
}
