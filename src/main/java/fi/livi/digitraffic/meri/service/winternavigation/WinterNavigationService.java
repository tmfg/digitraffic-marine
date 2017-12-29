package fi.livi.digitraffic.meri.service.winternavigation;

import static fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository.UpdatedName.WINTER_NAVIGATION_PORTS;
import static fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository.UpdatedName.WINTER_NAVIGATION_SHIPS;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.dao.winternavigation.WinterNavigationPortRepository;
import fi.livi.digitraffic.meri.dao.winternavigation.WinterNavigationShipRepository;
import fi.livi.digitraffic.meri.domain.winternavigation.PortRestriction;
import fi.livi.digitraffic.meri.domain.winternavigation.ShipActivity;
import fi.livi.digitraffic.meri.domain.winternavigation.ShipPlannedActivity;
import fi.livi.digitraffic.meri.domain.winternavigation.ShipState;
import fi.livi.digitraffic.meri.domain.winternavigation.ShipVoyage;
import fi.livi.digitraffic.meri.domain.winternavigation.WinterNavigationPort;
import fi.livi.digitraffic.meri.domain.winternavigation.WinterNavigationShip;
import fi.livi.digitraffic.meri.model.geojson.Point;
import fi.livi.digitraffic.meri.model.winternavigation.PortRestrictionProperty;
import fi.livi.digitraffic.meri.model.winternavigation.ShipActivityProperty;
import fi.livi.digitraffic.meri.model.winternavigation.ShipPlannedActivityProperty;
import fi.livi.digitraffic.meri.model.winternavigation.ShipStateProperty;
import fi.livi.digitraffic.meri.model.winternavigation.ShipVoyageProperty;
import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationPortFeature;
import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationPortFeatureCollection;
import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationPortProperties;
import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationShipFeature;
import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationShipFeatureCollection;
import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationShipProperties;

@Service
public class WinterNavigationService {

    private final WinterNavigationPortRepository winterNavigationPortRepository;

    private final WinterNavigationShipRepository winterNavigationShipRepository;

    private final UpdatedTimestampRepository updatedTimestampRepository;

    @Autowired
    public WinterNavigationService(final WinterNavigationPortRepository winterNavigationPortRepository,
                                   final WinterNavigationShipRepository winterNavigationShipRepository,
                                   final UpdatedTimestampRepository updatedTimestampRepository) {
        this.winterNavigationPortRepository = winterNavigationPortRepository;
        this.winterNavigationShipRepository = winterNavigationShipRepository;
        this.updatedTimestampRepository = updatedTimestampRepository;
    }

    @Transactional(readOnly = true)
    public WinterNavigationPortFeatureCollection getWinterNavigationPorts() {

        final List<WinterNavigationPort> ports = winterNavigationPortRepository.findDistinctByObsoleteDateIsNullOrderByLocode();

        final Instant lastUpdated = updatedTimestampRepository.getLastUpdated(WINTER_NAVIGATION_PORTS.name());

        return new WinterNavigationPortFeatureCollection(
            lastUpdated == null ? null : ZonedDateTime.ofInstant(lastUpdated, ZoneId.of("Europe/Helsinki")),
            ports.stream().map(this::portFeature).collect(Collectors.toList()));
    }

    @Transactional(readOnly = true)
    public WinterNavigationShipFeatureCollection getWinterNavigationShips() {

        final List<WinterNavigationShip> ships = winterNavigationShipRepository.findDistinctByOrderByVesselPK();

        final Instant lastUpdated = updatedTimestampRepository.getLastUpdated(WINTER_NAVIGATION_SHIPS.name());

        final List<WinterNavigationShipFeature> shipFeatures =
            ships.stream().map(s -> new WinterNavigationShipFeature(s.getVesselPK(),
                                                                    shipProperties(s),
                                                                    new Point(s.getShipState().getLongitude(), s.getShipState().getLatitude())))
                 .collect(Collectors.toList());

        return new WinterNavigationShipFeatureCollection(lastUpdated == null ? null : ZonedDateTime.ofInstant(lastUpdated, ZoneId.of("Europe/Helsinki")),
                                                         shipFeatures);
    }

    private WinterNavigationPortFeature portFeature(final WinterNavigationPort p) {
        return new WinterNavigationPortFeature(p.getLocode(),
                                               new WinterNavigationPortProperties(p.getName(), p.getNationality(), p.getSeaArea(), portRestrictions(p.getPortRestrictions())),
                                               new Point(p.getLongitude(), p.getLatitude()));
    }

    private List<PortRestrictionProperty> portRestrictions(final List<PortRestriction> portRestrictions) {
        return portRestrictions.stream().map(pr -> new PortRestrictionProperty(pr.getCurrent(), pr.getPortRestricted(), pr.getPortClosed(),
                                                                               pr.getIssueTime(), pr.getLastModified(), pr.getValidFrom(),
                                                                               pr.getValidUntil(), pr.getRawText(), pr.getFormattedText()))
            .collect(Collectors.toList());
    }

    private WinterNavigationShipProperties shipProperties(final WinterNavigationShip s) {
        return new WinterNavigationShipProperties(s.getVesselSource(), s.getMmsi(), s.getName(), s.getCallSign(), s.getImo(), s.getDwt(),
                                                  s.getLength(), s.getWidth(), s.getAisLength(), s.getAisWidth(), s.getDimensions(),
                                                  s.getNominalDraught(), s.getIceClass(), s.getNatCode(), s.getNationality(), s.getShipType(),
                                                  s.getAisShipType(), shipState(s.getShipState()), shipVoyage(s.getShipVoyage()),
                                                  shipActivities(s.getShipActivities()), shipPlannedActivities(s.getShipPlannedActivities()));
    }

    private ShipStateProperty shipState(final ShipState st) {
        return new ShipStateProperty(st.getTimestamp(), st.getPosPrintable(), st.getPosAccuracy(), st.getPosSource(), st.getPosArea(), st.getSpeed(),
                                     st.getCourse(), st.getHeading(), st.getAisDraught(), st.getAisState(), st.getAisStateText(), st.getAisDestination(),
                                     st.getMovingSince(), st.getStoppedSince(), st.getInactiveSince());
    }

    private ShipVoyageProperty shipVoyage(final ShipVoyage sv) {
        return new ShipVoyageProperty(sv.getFromLocode(), sv.getFromName(), sv.getFromAtd(), sv.getInLocode(), sv.getInName(), sv.getInAta(),
                                      sv.getInEtd(), sv.getDestLocode(), sv.getDestName(), sv.getDestEta());
    }

    private List<ShipActivityProperty> shipActivities(final List<ShipActivity> shipActivities) {
        return shipActivities.stream()
            .map(a -> new ShipActivityProperty(a.getActivityType(), a.getActivityText(), a.getActivityComment(), a.getBeginTime(),
                                               a.getEndTime(), a.getTimestampBegin(), a.getTimestampEnd(), a.getTimestampCanceled(),
                                               a.getOperatingIcebreakerPK(), a.getOperatingIcebreakerName(), a.getOperatedVesselPK(),
                                               a.getOperatedVesselName(), a.getConvoyOrder())).collect(Collectors.toList());
    }

    private List<ShipPlannedActivityProperty> shipPlannedActivities(final List<ShipPlannedActivity> shipPlannedActivities) {
        return shipPlannedActivities.stream()
            .map(a -> new ShipPlannedActivityProperty(a.getActivityType(), a.getActivityText(), a.getPlannedVesselPK(), a.getPlanningVesselPK(),
                                                      a.getOrdering(), a.getPlannedWhen(), a.getPlannedWhere(), a.getPlanComment(),
                                                      a.getPlanTimestamp(), a.getPlanTimestampRealized(), a.getPlanTimestampCanceled())).collect(Collectors.toList());
    }
}
