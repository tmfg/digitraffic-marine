package fi.livi.digitraffic.meri.service.winternavigation.v1;

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
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.livi.digitraffic.meri.dao.UpdatedTimestampRepository;
import fi.livi.digitraffic.meri.dao.winternavigation.WinterNavigationDirwayRepository;
import fi.livi.digitraffic.meri.dao.winternavigation.WinterNavigationPortRepository;
import fi.livi.digitraffic.meri.dao.winternavigation.WinterNavigationShipRepository;
import fi.livi.digitraffic.meri.dto.geojson.Geometry;
import fi.livi.digitraffic.meri.dto.geojson.LineString;
import fi.livi.digitraffic.meri.dto.geojson.Point;
import fi.livi.digitraffic.meri.dto.winternavigation.v1.WinterNavigationDirwayFeatureCollectionV1;
import fi.livi.digitraffic.meri.dto.winternavigation.v1.WinterNavigationDirwayFeatureV1;
import fi.livi.digitraffic.meri.dto.winternavigation.v1.WinterNavigationDirwayPropertiesV1;
import fi.livi.digitraffic.meri.dto.winternavigation.v1.WinterNavigationPortFeatureCollectionV1;
import fi.livi.digitraffic.meri.dto.winternavigation.v1.WinterNavigationPortFeatureV1;
import fi.livi.digitraffic.meri.dto.winternavigation.v1.WinterNavigationPortPropertiesV1;
import fi.livi.digitraffic.meri.dto.winternavigation.v1.WinterNavigationPortRestrictionV1;
import fi.livi.digitraffic.meri.dto.winternavigation.v1.WinterNavigationShipActivityV1;
import fi.livi.digitraffic.meri.dto.winternavigation.v1.WinterNavigationShipFeatureCollectionV1;
import fi.livi.digitraffic.meri.dto.winternavigation.v1.WinterNavigationShipFeatureV1;
import fi.livi.digitraffic.meri.dto.winternavigation.v1.WinterNavigationShipPlannedActivityV1;
import fi.livi.digitraffic.meri.dto.winternavigation.v1.WinterNavigationShipPropertiesV1;
import fi.livi.digitraffic.meri.dto.winternavigation.v1.WinterNavigationShipStateV1;
import fi.livi.digitraffic.meri.dto.winternavigation.v1.WinterNavigationShipVoyageV1;
import fi.livi.digitraffic.meri.model.winternavigation.PortRestriction;
import fi.livi.digitraffic.meri.model.winternavigation.ShipActivity;
import fi.livi.digitraffic.meri.model.winternavigation.ShipPlannedActivity;
import fi.livi.digitraffic.meri.model.winternavigation.ShipState;
import fi.livi.digitraffic.meri.model.winternavigation.ShipVoyage;
import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationDirway;
import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationDirwayPoint;
import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationPort;
import fi.livi.digitraffic.meri.model.winternavigation.WinterNavigationShip;
import fi.livi.digitraffic.meri.service.ObjectNotFoundException;

@ConditionalOnWebApplication
@Service
public class WinterNavigationWebServiceV1 {

    private final WinterNavigationPortRepository winterNavigationPortRepository;

    private final WinterNavigationShipRepository winterNavigationShipRepository;

    private final WinterNavigationDirwayRepository winterNavigationDirwayRepository;

    private final UpdatedTimestampRepository updatedTimestampRepository;

    @Autowired
    public WinterNavigationWebServiceV1(final WinterNavigationPortRepository winterNavigationPortRepository,
                                        final WinterNavigationShipRepository winterNavigationShipRepository,
                                        final WinterNavigationDirwayRepository winterNavigationDirwayRepository,
                                        final UpdatedTimestampRepository updatedTimestampRepository) {
        this.winterNavigationPortRepository = winterNavigationPortRepository;
        this.winterNavigationShipRepository = winterNavigationShipRepository;
        this.winterNavigationDirwayRepository = winterNavigationDirwayRepository;
        this.updatedTimestampRepository = updatedTimestampRepository;
    }

    @Transactional(readOnly = true)
    public WinterNavigationPortFeatureCollectionV1 getWinterNavigationPorts() {
        final Stream<WinterNavigationPort> ports = winterNavigationPortRepository.findDistinctByObsoleteDateIsNullOrderByLocode();

        final ZonedDateTime lastUpdated = updatedTimestampRepository.findLastUpdated(WINTER_NAVIGATION_PORTS);

        return new WinterNavigationPortFeatureCollectionV1(lastUpdated,
            ports.map(this::portFeature).collect(Collectors.toList()));
    }

    @Transactional(readOnly = true)
    public WinterNavigationShipFeatureCollectionV1 getWinterNavigationShips() {
        final Stream<WinterNavigationShip> ships = winterNavigationShipRepository.findDistinctByOrderByVesselPK();

        final ZonedDateTime lastUpdated = updatedTimestampRepository.findLastUpdated(WINTER_NAVIGATION_VESSELS);

        final List<WinterNavigationShipFeatureV1> shipFeatures =
            ships.map(s -> new WinterNavigationShipFeatureV1(s.getVesselPK(),
                                                                    shipProperties(s),
                                                                    new Point(s.getShipState().getLongitude(), s.getShipState().getLatitude())))
                 .collect(Collectors.toList());

        return new WinterNavigationShipFeatureCollectionV1(lastUpdated, shipFeatures);
    }

    @Transactional(readOnly = true)
    public WinterNavigationDirwayFeatureCollectionV1 getWinterNavigationDirways() {
        final List<WinterNavigationDirway> dirways = winterNavigationDirwayRepository.findDistinctByOrderByName();

        final ZonedDateTime lastUpdated = updatedTimestampRepository.findLastUpdated(WINTER_NAVIGATION_DIRWAYS);

        return new WinterNavigationDirwayFeatureCollectionV1(lastUpdated,
            dirways.stream().map(d -> new WinterNavigationDirwayFeatureV1(d.getName(), dirwayProperties(d), dirwayGeometry(d.getDirwayPoints())))
                .collect(Collectors.toList()));
    }

    @Transactional(readOnly = true)
    public WinterNavigationShipFeatureV1 getWinterNavigationShipByVesselId(final String vesselId) {
        final WinterNavigationShip ship = winterNavigationShipRepository.findById(vesselId).orElse(null);

        if (ship != null) {
            return new WinterNavigationShipFeatureV1(ship.getVesselPK(), shipProperties(ship),
                new Point(ship.getShipState().getLongitude(), ship.getShipState().getLatitude()));
        }

        throw new ObjectNotFoundException(WinterNavigationShip.class, vesselId);
    }

    @Transactional(readOnly = true)
    public WinterNavigationPortFeatureV1 getWinterNavigationPortByLocode(final String locode) {
        final WinterNavigationPort port = winterNavigationPortRepository.findById(locode).orElse(null);

        if(port != null) {
            return portFeature(port);
        }

        throw new ObjectNotFoundException(WinterNavigationPort.class, locode);
    }

    private Geometry<?> dirwayGeometry(final List<WinterNavigationDirwayPoint> dirwayPoints) {
        final Geometry<?> geometry;
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

    private WinterNavigationDirwayPropertiesV1 dirwayProperties(final WinterNavigationDirway d) {
        return new WinterNavigationDirwayPropertiesV1(d.getName(), d.getIssueTime(), d.getIssuerCode(), d.getIssuerName(), d.getValidUntil(), d.getModified());
    }

    private WinterNavigationPortFeatureV1 portFeature(final WinterNavigationPort p) {
        return new WinterNavigationPortFeatureV1(p.getLocode(),
                                               new WinterNavigationPortPropertiesV1(p.getLocode(), p.getName(), p.getNationality(), p.getSeaArea(), portRestrictions(p.getPortRestrictions()), p.getModified()),
                                               new Point(p.getLongitude(), p.getLatitude()));
    }

    private List<WinterNavigationPortRestrictionV1> portRestrictions(final List<PortRestriction> portRestrictions) {
        return portRestrictions.stream().map(pr -> new WinterNavigationPortRestrictionV1(pr.getCurrent(), pr.getPortRestricted(), pr.getPortClosed(),
                                                                               pr.getIssueTime(), pr.getLastModified(), pr.getValidFrom(),
                                                                               pr.getValidUntil(), pr.getRawText(), pr.getFormattedText()))
            .collect(Collectors.toList());
    }

    private WinterNavigationShipPropertiesV1 shipProperties(final WinterNavigationShip s) {
        return new WinterNavigationShipPropertiesV1(s.getVesselPK(), s.getVesselSource(), s.getMmsi(), s.getName(), s.getCallSign(), s.getImo(), s.getDwt(),
                                                  s.getLength(), s.getWidth(), s.getAisLength(), s.getAisWidth(), s.getDimensions(),
                                                  s.getNominalDraught(), s.getIceClass(), s.getNatCode(), s.getNationality(), s.getShipType(),
                                                  s.getAisShipType(), shipState(s.getShipState()), shipVoyage(s.getShipVoyage()),
                                                  shipActivities(s.getShipActivities()), shipPlannedActivities(s.getShipPlannedActivities()), s.getModified());
    }

    private WinterNavigationShipStateV1 shipState(final ShipState st) {
        return new WinterNavigationShipStateV1(st.getTimestamp(), st.getPosPrintable(), st.getPosAccuracy(), st.getPosSource(), st.getPosArea(), st.getSpeed(),
                                     st.getCourse(), st.getHeading(), st.getAisDraught(), st.getAisState(), st.getAisStateText(), st.getAisDestination(),
                                     st.getMovingSince(), st.getStoppedSince(), st.getInactiveSince());
    }

    private WinterNavigationShipVoyageV1 shipVoyage(final ShipVoyage sv) {
        return new WinterNavigationShipVoyageV1(sv.getFromLocode(), sv.getFromName(), sv.getFromAtd(), sv.getInLocode(), sv.getInName(), sv.getInAta(),
                                      sv.getInEtd(), sv.getDestLocode(), sv.getDestName(), sv.getDestEta());
    }

    private List<WinterNavigationShipActivityV1> shipActivities(final Set<ShipActivity> shipActivities) {
        return shipActivities.stream()
            .map(a -> new WinterNavigationShipActivityV1(a.getActivityType(), a.getActivityText(), a.getActivityComment(), a.getBeginTime(),
                                               a.getEndTime(), a.getTimestampBegin(), a.getTimestampEnd(), a.getTimestampCanceled(),
                                               a.getOperatingIcebreakerPK(), a.getOperatingIcebreakerName(), a.getOperatedVesselPK(),
                                               a.getOperatedVesselName(), a.getConvoyOrder())).collect(Collectors.toList());
    }

    private List<WinterNavigationShipPlannedActivityV1> shipPlannedActivities(final Set<ShipPlannedActivity> shipPlannedActivities) {
        return shipPlannedActivities.stream()
            .map(a -> new WinterNavigationShipPlannedActivityV1(a.getActivityType(), a.getActivityText(), a.getPlannedVesselPK(), a.getPlanningVesselPK(),
                                                      a.getOrdering(), a.getPlannedWhen(), a.getPlannedWhere(), a.getPlanComment(),
                                                      a.getPlanTimestamp(), a.getPlanTimestampRealized(), a.getPlanTimestampCanceled())).collect(Collectors.toList());
    }
}
