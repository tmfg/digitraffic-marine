package fi.livi.digitraffic.meri.service.winternavigation;

import static fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository.UpdatedName.WINTER_NAVIGATION_DIRWAYS;
import static fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository.UpdatedName.WINTER_NAVIGATION_PORTS;
import static fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository.UpdatedName.WINTER_NAVIGATION_VESSELS;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.dao.winternavigation.WinterNavigationDirwayRepository;
import fi.livi.digitraffic.meri.dao.winternavigation.WinterNavigationPortRepository;
import fi.livi.digitraffic.meri.dao.winternavigation.WinterNavigationShipRepository;
import fi.livi.digitraffic.meri.domain.winternavigation.PortRestriction;
import fi.livi.digitraffic.meri.domain.winternavigation.ShipActivity;
import fi.livi.digitraffic.meri.domain.winternavigation.ShipPlannedActivity;
import fi.livi.digitraffic.meri.domain.winternavigation.ShipState;
import fi.livi.digitraffic.meri.domain.winternavigation.ShipVoyage;
import fi.livi.digitraffic.meri.domain.winternavigation.WinterNavigationDirway;
import fi.livi.digitraffic.meri.domain.winternavigation.WinterNavigationDirwayPoint;
import fi.livi.digitraffic.meri.domain.winternavigation.WinterNavigationPort;
import fi.livi.digitraffic.meri.domain.winternavigation.WinterNavigationShip;
import fi.livi.digitraffic.meri.model.geojson.Geometry;
import fi.livi.digitraffic.meri.model.geojson.LineString;
import fi.livi.digitraffic.meri.model.geojson.Point;
import fi.livi.digitraffic.meri.model.winternavigation.PortRestrictionProperty;
import fi.livi.digitraffic.meri.model.winternavigation.ShipActivityProperty;
import fi.livi.digitraffic.meri.model.winternavigation.ShipPlannedActivityProperty;
import fi.livi.digitraffic.meri.model.winternavigation.ShipStateProperty;
import fi.livi.digitraffic.meri.model.winternavigation.ShipVoyageProperty;
import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationDirwayFeature;
import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationDirwayFeatureCollection;
import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationDirwayProperties;
import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationPortFeature;
import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationPortFeatureCollection;
import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationPortProperties;
import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationShipFeature;
import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationShipFeatureCollection;
import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationShipProperties;
import fi.livi.digitraffic.meri.service.ObjectNotFoundException;

@Service
public class WinterNavigationService {

    private final WinterNavigationPortRepository winterNavigationPortRepository;

    private final WinterNavigationShipRepository winterNavigationShipRepository;

    private final WinterNavigationDirwayRepository winterNavigationDirwayRepository;

    private final UpdatedTimestampRepository updatedTimestampRepository;

    @Autowired
    public WinterNavigationService(final WinterNavigationPortRepository winterNavigationPortRepository,
                                   final WinterNavigationShipRepository winterNavigationShipRepository,
                                   final WinterNavigationDirwayRepository winterNavigationDirwayRepository,
                                   final UpdatedTimestampRepository updatedTimestampRepository) {
        this.winterNavigationPortRepository = winterNavigationPortRepository;
        this.winterNavigationShipRepository = winterNavigationShipRepository;
        this.winterNavigationDirwayRepository = winterNavigationDirwayRepository;
        this.updatedTimestampRepository = updatedTimestampRepository;
    }

    @Transactional(readOnly = true)
    public WinterNavigationPortFeatureCollection getWinterNavigationPorts() {
        final Stream<WinterNavigationPort> ports = winterNavigationPortRepository.findDistinctByObsoleteDateIsNullOrderByLocode();

        final ZonedDateTime lastUpdated = updatedTimestampRepository.findLastUpdated(WINTER_NAVIGATION_PORTS);

        return new WinterNavigationPortFeatureCollection(lastUpdated,
            ports.map(this::portFeature).collect(Collectors.toList()));
    }

    @Transactional(readOnly = true)
    public WinterNavigationShipFeatureCollection getWinterNavigationShips() {
        final Stream<WinterNavigationShip> ships = winterNavigationShipRepository.findDistinctByOrderByVesselPK();

        final ZonedDateTime lastUpdated = updatedTimestampRepository.findLastUpdated(WINTER_NAVIGATION_VESSELS);

        final List<WinterNavigationShipFeature> shipFeatures =
            ships.map(s -> new WinterNavigationShipFeature(s.getVesselPK(),
                                                                    shipProperties(s),
                                                                    new Point(s.getShipState().getLongitude(), s.getShipState().getLatitude())))
                 .collect(Collectors.toList());

        return new WinterNavigationShipFeatureCollection(lastUpdated, shipFeatures);
    }

    @Transactional(readOnly = true)
    public WinterNavigationDirwayFeatureCollection getWinterNavigationDirways() {
        final List<WinterNavigationDirway> dirways = winterNavigationDirwayRepository.findDistinctByOrderByName();

        final ZonedDateTime lastUpdated = updatedTimestampRepository.findLastUpdated(WINTER_NAVIGATION_DIRWAYS);

        return new WinterNavigationDirwayFeatureCollection(lastUpdated,
            dirways.stream().map(d -> new WinterNavigationDirwayFeature(d.getName(), dirwayProperties(d), dirwayGeometry(d.getDirwayPoints())))
                .collect(Collectors.toList()));
    }

    @Transactional(readOnly = true)
    public WinterNavigationShipFeature getWinterNavigationShipByVesselId(String vesselId) {
        final WinterNavigationShip ship = winterNavigationShipRepository.findById(vesselId).orElse(null);

        if (ship != null) {
            return new WinterNavigationShipFeature(ship.getVesselPK(), shipProperties(ship),
                new Point(ship.getShipState().getLongitude(), ship.getShipState().getLatitude()));
        }

        throw new ObjectNotFoundException(WinterNavigationShip.class, vesselId);
    }

    @Transactional(readOnly = true)
    public WinterNavigationPortFeature getWinterNavigationPortByLocode(final String locode) {
        final WinterNavigationPort port = winterNavigationPortRepository.findById(locode).orElse(null);

        if(port != null) {
            return portFeature(port);
        }

        throw new ObjectNotFoundException(WinterNavigationPort.class, locode);
    }

    private Geometry<?> dirwayGeometry(final List<WinterNavigationDirwayPoint> dirwayPoints) {
        Geometry<?> geometry;
        if (dirwayPoints.size() == 1) {
            geometry = new Point(dirwayPoints.get(0).getLongitude(), dirwayPoints.get(0).getLatitude());
        } else {
            geometry = new LineString();
            ((Geometry<List<List<Double>>>)geometry)
                .setCoordinates(dirwayPoints.stream()
                                            .sorted(Comparator.comparing(WinterNavigationDirwayPoint::getOrderNumber))
                                            .map(p -> Arrays.asList(p.getLongitude(), p.getLatitude()))
                                            .collect(Collectors.toList()));
        }
        geometry.setAdditionalProperty("seaAreas", dirwayPoints.stream().map(WinterNavigationDirwayPoint::getSeaArea).collect(Collectors.toList()));
        return geometry;
    }

    private WinterNavigationDirwayProperties dirwayProperties(final WinterNavigationDirway d) {
        return new WinterNavigationDirwayProperties(d.getName(), d.getIssueTime(), d.getIssuerCode(), d.getIssuerName(), d.getValidUntil(), d.getModified());
    }

    private WinterNavigationPortFeature portFeature(final WinterNavigationPort p) {
        return new WinterNavigationPortFeature(p.getLocode(),
                                               new WinterNavigationPortProperties(p.getLocode(), p.getName(), p.getNationality(), p.getSeaArea(), portRestrictions(p.getPortRestrictions()), p.getModified()),
                                               new Point(p.getLongitude(), p.getLatitude()));
    }

    private List<PortRestrictionProperty> portRestrictions(final List<PortRestriction> portRestrictions) {
        return portRestrictions.stream().map(pr -> new PortRestrictionProperty(pr.getCurrent(), pr.getPortRestricted(), pr.getPortClosed(),
                                                                               pr.getIssueTime(), pr.getLastModified(), pr.getValidFrom(),
                                                                               pr.getValidUntil(), pr.getRawText(), pr.getFormattedText()))
            .collect(Collectors.toList());
    }

    private WinterNavigationShipProperties shipProperties(final WinterNavigationShip s) {
        return new WinterNavigationShipProperties(s.getVesselPK(), s.getVesselSource(), s.getMmsi(), s.getName(), s.getCallSign(), s.getImo(), s.getDwt(),
                                                  s.getLength(), s.getWidth(), s.getAisLength(), s.getAisWidth(), s.getDimensions(),
                                                  s.getNominalDraught(), s.getIceClass(), s.getNatCode(), s.getNationality(), s.getShipType(),
                                                  s.getAisShipType(), shipState(s.getShipState()), shipVoyage(s.getShipVoyage()),
                                                  shipActivities(s.getShipActivities()), shipPlannedActivities(s.getShipPlannedActivities()), s.getModified());
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

    private List<ShipActivityProperty> shipActivities(final Set<ShipActivity> shipActivities) {
        return shipActivities.stream()
            .map(a -> new ShipActivityProperty(a.getActivityType(), a.getActivityText(), a.getActivityComment(), a.getBeginTime(),
                                               a.getEndTime(), a.getTimestampBegin(), a.getTimestampEnd(), a.getTimestampCanceled(),
                                               a.getOperatingIcebreakerPK(), a.getOperatingIcebreakerName(), a.getOperatedVesselPK(),
                                               a.getOperatedVesselName(), a.getConvoyOrder())).collect(Collectors.toList());
    }

    private List<ShipPlannedActivityProperty> shipPlannedActivities(final Set<ShipPlannedActivity> shipPlannedActivities) {
        return shipPlannedActivities.stream()
            .map(a -> new ShipPlannedActivityProperty(a.getActivityType(), a.getActivityText(), a.getPlannedVesselPK(), a.getPlanningVesselPK(),
                                                      a.getOrdering(), a.getPlannedWhen(), a.getPlannedWhere(), a.getPlanComment(),
                                                      a.getPlanTimestamp(), a.getPlanTimestampRealized(), a.getPlanTimestampCanceled())).collect(Collectors.toList());
    }
}
