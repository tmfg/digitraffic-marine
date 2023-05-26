package fi.livi.digitraffic.meri.controller.portcall;

import java.time.Instant;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.livi.digitraffic.meri.domain.portnet.Berth;
import fi.livi.digitraffic.meri.domain.portnet.PortArea;
import fi.livi.digitraffic.meri.domain.portnet.SsnLocation;
import fi.livi.digitraffic.meri.dto.portcall.v1.PortLocationDtoV1;
import fi.livi.digitraffic.meri.model.geojson.Point;
import fi.livi.digitraffic.meri.model.portnet.metadata.BerthCollection;
import fi.livi.digitraffic.meri.model.portnet.metadata.BerthFeature;
import fi.livi.digitraffic.meri.model.portnet.metadata.BerthFeatureCollection;
import fi.livi.digitraffic.meri.model.portnet.metadata.BerthProperties;
import fi.livi.digitraffic.meri.model.portnet.metadata.LocationFeatureCollections_V1;
import fi.livi.digitraffic.meri.model.portnet.metadata.PortAreaFeature;
import fi.livi.digitraffic.meri.model.portnet.metadata.PortAreaFeatureCollection;
import fi.livi.digitraffic.meri.model.portnet.metadata.PortAreaProperties;
import fi.livi.digitraffic.meri.model.portnet.metadata.PortFeature;
import fi.livi.digitraffic.meri.model.portnet.metadata.PortFeatureCollection;
import fi.livi.digitraffic.meri.model.portnet.metadata.PortProperties;

public final class SsnLocationConverter {
    private static final Logger log = LoggerFactory.getLogger(SsnLocationConverter.class);
    private SsnLocationConverter() {}

    public static LocationFeatureCollections_V1 convert_V1(final Instant timestamp, final Stream<SsnLocation> locations,
                                                           final Stream<PortArea> portAreas, final Stream<Berth> berths) {
        try {
            return new LocationFeatureCollections_V1(timestamp,
                convertSsnLocations(locations, timestamp),
                convertPortAreas(portAreas, timestamp),
                convertBerths(berths, timestamp));
        } catch (final Exception e) {
            log.error("method=convert_V1 failed with error", e);
            throw e;
        }
    }

    public static PortLocationDtoV1 convertV1(final Instant timestamp, final Stream<SsnLocation> locations,
                                              final Stream<PortArea> portAreas, final Stream<Berth> berths) {
        try {
        return new PortLocationDtoV1(timestamp,
            convertSsnLocations(locations, timestamp),
            convertPortAreas(portAreas, timestamp),
            convertToBertCollection(berths, timestamp));
        } catch (final Exception e) {
            log.error("method=convertV1 failed with error", e);
            throw e;
        }
    }

    public static BerthCollection convertToBertCollection(final Stream<Berth> berths, final Instant dataUpdatedTime) {
        return new BerthCollection(berths.map(b -> convertBerthProperties(b, dataUpdatedTime)).collect(Collectors.toList()), dataUpdatedTime);
    }
    public static BerthFeatureCollection convertBerths(final Stream<Berth> berths, final Instant dataUpdatedTime) {
        return new BerthFeatureCollection(berths.map(b -> convertBerth(b, dataUpdatedTime)).collect(Collectors.toList()), dataUpdatedTime);
    }

    private static BerthFeature convertBerth(final Berth b, final Instant dataUpdatedTime) {
        final BerthProperties p = convertBerthProperties(b, dataUpdatedTime);

        return new BerthFeature(b.getBerthKey().getLocode(), b.getBerthKey().getPortAreaCode(), b.getBerthKey().getBerthCode(), p);
    }

    public static BerthProperties convertBerthProperties(final Berth b, final Instant dataUpdatedTime) {
        return new BerthProperties(b.getBerthKey().getLocode(), b.getBerthKey().getPortAreaCode(), b.getBerthKey().getBerthCode(), b.getBerthName(), dataUpdatedTime);
    }

    public static PortAreaFeatureCollection convertPortAreas(final Stream<PortArea> portAreas, final Instant dataUpdatedTime) {
        return new PortAreaFeatureCollection(portAreas.map(pa -> convertPortArea(pa, dataUpdatedTime)).collect(Collectors.toList()), dataUpdatedTime);
    }

    public static PortAreaFeature convertPortArea(final PortArea pa,  final Instant dataUpdatedTime) {
        final PortAreaProperties p = new PortAreaProperties(pa.getPortAreaKey().getLocode(), pa.getPortAreaName(), dataUpdatedTime);
        final Point g = geometry(pa.getWgs84Long(), pa.getWgs84Lat());
        return new PortAreaFeature(pa.getPortAreaKey().getLocode(), pa.getPortAreaKey().getPortAreaCode(), p, g);
    }

    public static PortFeatureCollection convertSsnLocations(final Stream<SsnLocation> locations, final Instant dataUpdatedTime) {
        return new PortFeatureCollection(locations.map(l -> convertSsnLocation(l, dataUpdatedTime)).collect(Collectors.toList()), dataUpdatedTime);
    }

    public static PortFeature convertSsnLocation(final SsnLocation l, final Instant dataUpdatedTime) {
        final PortProperties p = new PortProperties(l.getLocode(), l.getLocationName(), l.getCountry(), dataUpdatedTime);
        final Point g = geometry(l.getWgs84Long(), l.getWgs84Lat());
        return new PortFeature(l.getLocode(), p, g);
    }

    private static Point geometry(final Double lon, final Double lat) {
        return lon == null || lat == null ? null : new Point(lon, lat);
    }
}
