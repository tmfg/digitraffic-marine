package fi.livi.digitraffic.meri.service.winternavigation;

import static fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository.UpdatedName.WINTER_NAVIGATION_SHIPS;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.dao.winternavigation.WinterNavigationShipRepository;
import fi.livi.digitraffic.meri.domain.winternavigation.ShipActivity;
import fi.livi.digitraffic.meri.domain.winternavigation.ShipActivityPK;
import fi.livi.digitraffic.meri.domain.winternavigation.ShipPlannedActivity;
import fi.livi.digitraffic.meri.domain.winternavigation.ShipState;
import fi.livi.digitraffic.meri.domain.winternavigation.ShipVoyage;
import fi.livi.digitraffic.meri.domain.winternavigation.WinterNavigationShip;
import fi.livi.digitraffic.meri.service.winternavigation.dto.ShipActivityDto;
import fi.livi.digitraffic.meri.service.winternavigation.dto.ShipDto;
import fi.livi.digitraffic.meri.service.winternavigation.dto.ShipPlannedActivityDto;
import fi.livi.digitraffic.meri.service.winternavigation.dto.ShipsDto;

@Service
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

    @Transactional
    public void updateWinterNavigationShips() {

        final ShipsDto data = winterNavigationClient.getWinterNavigationShips();

        final List<WinterNavigationShip> added = new ArrayList<>();
        final List<WinterNavigationShip> updated = new ArrayList<>();

        final Map<String, WinterNavigationShip> shipsByVesselPK =
            winterNavigationShipRepository.findDistinctByOrderByVesselPK().stream().collect(Collectors.toMap(s -> s.getVesselPK(), s -> s));

        final StopWatch stopWatch = StopWatch.createStarted();
        data.ships.forEach(ship -> update(ship, added, updated, shipsByVesselPK));
        winterNavigationShipRepository.save(added);
        stopWatch.stop();

        log.info("Added {} winter navigation ships(s), updated {}, took {} ms", added.size(), updated.size(), stopWatch.getTime());

        updatedTimestampRepository.setUpdated(WINTER_NAVIGATION_SHIPS.name(),
                                              Date.from(data.dataValidTime.toInstant()),
                                              getClass().getSimpleName());
    }

    private void update(final ShipDto ship, final List<WinterNavigationShip> added, final List<WinterNavigationShip> updated,
                        final Map<String, WinterNavigationShip> shipsByVesselPK) {

        final WinterNavigationShip old = shipsByVesselPK.get(ship.vesselPk);

        if (old == null) {
            added.add(addNew(ship));
        } else {
            updated.add(updateData(old, ship));
        }
    }

    private WinterNavigationShip addNew(final ShipDto ship) {
        final WinterNavigationShip s = new WinterNavigationShip();

        updateData(s, ship);
        return s;
    }

    private static WinterNavigationShip updateData(final WinterNavigationShip s, final ShipDto ship) {

        s.setVesselPK(ship.vesselPk);
        s.setVesselSource(ship.vesselSource);
        s.setMmsi(ship.shipData.mmsi);
        s.setName(ship.shipData.name);
        s.setImo(ship.shipData.imo);
        s.setNationality(ship.shipData.nationality);
        s.setNatCode(ship.shipData.natCode);
        s.setAisLength(ship.shipData.aisLength);
        s.setAisWidth(ship.shipData.aisWidth);
        s.setAisShipType(ship.shipData.aisShipType);
        s.setCallSign(ship.shipData.callSign);
        s.setDimensions(ship.shipData.dimensions);
        s.setDwt(ship.shipData.dwt);
        s.setIceClass(ship.shipData.iceClass);
        s.setNominalDraught(ship.shipData.nominalDraught);
        s.setLength(ship.shipData.length);
        s.setWidth(ship.shipData.width);
        s.setShipType(ship.shipData.shipType);

        updateShipState(s, ship);
        updateShipVoyage(s, ship);
        updateShipActivities(s, ship);
        updateShipPlannedActivities(s, ship);
        return s;
    }

    private static void updateShipState(final WinterNavigationShip s, final ShipDto ship) {

        final ShipState shipState = s.getShipState() != null ? s.getShipState() : new ShipState();
        shipState.setVesselPK(ship.vesselPk);
        if (ship.shipState != null) {
            shipState.setTimestamp(Timestamp.from(ship.shipState.timestamp.toInstant()));
            shipState.setLongitude(ship.shipState.longitude);
            shipState.setLatitude(ship.shipState.latitude);
            shipState.setPosPrintable(ship.shipState.posPrintable);
            shipState.setPosAccuracy(ship.shipState.posAccuracy);
            shipState.setPosSource(ship.shipState.posSource);
            shipState.setPosArea(ship.shipState.posArea);
            shipState.setSpeed(ship.shipState.speed);
            shipState.setCourse(ship.shipState.course);
            shipState.setHeading(ship.shipState.heading);
            shipState.setAisDraught(ship.shipState.aisDraught);
            shipState.setAisState(ship.shipState.aisState);
            shipState.setAisStateText(ship.shipState.aisStateText);
            shipState.setAisDestination(ship.shipState.aisDestination);
            shipState.setMovingSince(WinterNavigationPortUpdater.findTimestamp(ship.shipState.movingSince));
            shipState.setStoppedSince(WinterNavigationPortUpdater.findTimestamp(ship.shipState.stoppedSince));
            shipState.setInactiveSince(WinterNavigationPortUpdater.findTimestamp(ship.shipState.inactiveSince));
        }
        s.setShipState(shipState);
    }

    private static void updateShipVoyage(final WinterNavigationShip s, final ShipDto ship) {

        final ShipVoyage shipVoyage = s.getShipVoyage() != null ? s.getShipVoyage() : new ShipVoyage();
        shipVoyage.setVesselPK(ship.vesselPk);
        if (ship.shipVoyage != null) {
            shipVoyage.setInLocode(ship.shipVoyage.inLocode);
            shipVoyage.setInName(ship.shipVoyage.inName);
            shipVoyage.setInAta(WinterNavigationPortUpdater.findTimestamp(ship.shipVoyage.inAta));
            shipVoyage.setInEtd(WinterNavigationPortUpdater.findTimestamp(ship.shipVoyage.inEtd));
            shipVoyage.setFromLocode(ship.shipVoyage.fromLocode);
            shipVoyage.setFromName(ship.shipVoyage.fromName);
            shipVoyage.setFromAtd(WinterNavigationPortUpdater.findTimestamp(ship.shipVoyage.fromAtd));
            shipVoyage.setDestLocode(ship.shipVoyage.destLocode);
            shipVoyage.setDestName(ship.shipVoyage.destName);
            shipVoyage.setDestEta(WinterNavigationPortUpdater.findTimestamp(ship.shipVoyage.destEta));
        }
        s.setShipVoyage(shipVoyage);
    }

    private static void updateShipActivities(final WinterNavigationShip s, final ShipDto ship) {
        s.getShipActivities().clear();

        if (ship.shipActivities == null) {
            return;
        }

        int orderNumber = 1;
        for (final ShipActivityDto shipActivity : ship.shipActivities) {
            final ShipActivity activity = new ShipActivity();
            activity.setShipActivityPK(new ShipActivityPK(ship.vesselPk, orderNumber));
            activity.setActivityType(shipActivity.activityType);
            activity.setActivityText(shipActivity.activityText);
            activity.setBeginTime(WinterNavigationPortUpdater.findTimestamp(shipActivity.beginTime));
            activity.setEndTime(WinterNavigationPortUpdater.findTimestamp(shipActivity.endTime));
            activity.setActivityComment(shipActivity.comment);
            activity.setTimestampBegin(Timestamp.from(shipActivity.timestampBegin.toInstant()));
            activity.setTimestampEnd(WinterNavigationPortUpdater.findTimestamp(shipActivity.timestampEnd));
            activity.setTimestampCanceled(WinterNavigationPortUpdater.findTimestamp(shipActivity.timestampCanceled));
            activity.setConvoyOrder(shipActivity.convoyOrder);
            activity.setOperatedVesselPK(shipActivity.operatedVesselPK);
            activity.setOperatedVesselName(shipActivity.operatedVesselName);
            activity.setOperatingIcebreakerPK(shipActivity.operatingIcebreakerPK);
            activity.setOperatingIcebreakerName(shipActivity.operatingIcebreakerName);
            s.getShipActivities().add(activity);
            orderNumber++;
        }
    }

    private static void updateShipPlannedActivities(final WinterNavigationShip s, final ShipDto ship) {
        s.getShipPlannedActivities().clear();

        if (ship.plannedActivities == null) {
            return;
        }

        int orderNumber = 1;
        for (final ShipPlannedActivityDto plannedActivity : ship.plannedActivities) {
            final ShipPlannedActivity activity = new ShipPlannedActivity();
            activity.setShipActivityPK(new ShipActivityPK(ship.vesselPk, orderNumber));
            activity.setActivityType(plannedActivity.activityType);
            activity.setActivityText(plannedActivity.activityText);
            activity.setPlannedWhen(plannedActivity.plannedWhen);
            activity.setPlannedWhere(plannedActivity.plannedWhere);
            activity.setPlanComment(plannedActivity.planComment);
            activity.setOrdering(plannedActivity.ordering);
            activity.setPlannedVesselPK(plannedActivity.plannedVesselPK);
            activity.setPlanningVesselPK(plannedActivity.planningVesselPK);
            activity.setPlanTimestampCanceled(WinterNavigationPortUpdater.findTimestamp(plannedActivity.planTimestampCanceled));
            activity.setPlanTimestampRealized(WinterNavigationPortUpdater.findTimestamp(plannedActivity.planTimestampRealized));
            s.getShipPlannedActivities().add(activity);
            orderNumber++;
        }
    }
}
