package fi.livi.digitraffic.meri.controller.portnet;

import java.time.Instant;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private SsnLocationConverter() {}

    public static LocationFeatureCollections_V1 convert_V1(final Instant timestamp, final Stream<SsnLocation> locations,
                                                           final Stream<PortArea> portAreas, final Stream<Berth> berths) {
        return new LocationFeatureCollections_V1(timestamp,
                convertSsnLocations(locations, timestamp),
                convertPortAreas(portAreas, timestamp),
                convertBerths(berths, timestamp));
    }

    public static PortLocationDtoV1 convertV1(final Instant timestamp, final Stream<SsnLocation> locations,
                                              final Stream<PortArea> portAreas, final Stream<Berth> berths) {
        return new PortLocationDtoV1(timestamp,
            convertSsnLocations(locations, timestamp),
            convertPortAreas(portAreas, timestamp),
            convertToBertCollection(berths, timestamp));
    }

    public static BerthCollection convertToBertCollection(final Stream<Berth> berths, final Instant dataUpdatedTime) {
        return new BerthCollection(berths.map(SsnLocationConverter::convertBerthProperties).collect(Collectors.toList()), dataUpdatedTime);
    }
    public static BerthFeatureCollection convertBerths(final Stream<Berth> berths, final Instant dataUpdatedTime) {
        return new BerthFeatureCollection(berths.map(SsnLocationConverter::convertBerth).collect(Collectors.toList()), dataUpdatedTime);
    }

    private static BerthFeature convertBerth(final Berth b) {
        final BerthProperties p = convertBerthProperties(b);

        return new BerthFeature(b.getBerthKey().getLocode(), b.getBerthKey().getPortAreaCode(), b.getBerthKey().getBerthCode(), p);
    }

    public static BerthProperties convertBerthProperties(final Berth b) {
        return new BerthProperties(b.getBerthKey().getLocode(), b.getBerthKey().getPortAreaCode(), b.getBerthKey().getBerthCode(), b.getBerthName());
    }

    public static PortAreaFeatureCollection convertPortAreas(final Stream<PortArea> portAreas, final Instant dataUpdatedTime) {
        return new PortAreaFeatureCollection(portAreas.map(SsnLocationConverter::convertPortArea).collect(Collectors.toList()), dataUpdatedTime);
    }

    public static PortAreaFeature convertPortArea(final PortArea pa) {
        final PortAreaProperties p = new PortAreaProperties(pa.getPortAreaKey().getLocode(), pa.getPortAreaName());
        final Point g = geometry(pa.getWgs84Long(), pa.getWgs84Lat());
        return new PortAreaFeature(pa.getPortAreaKey().getLocode(), pa.getPortAreaKey().getPortAreaCode(), p, g);
    }

    public static PortFeatureCollection convertSsnLocations(final Stream<SsnLocation> locations, final Instant dataUpdatedTime) {
        return new PortFeatureCollection(locations.map(l -> convertSsnLocation(l, dataUpdatedTime)).collect(Collectors.toList()), dataUpdatedTime);
    }

    public static PortFeature convertSsnLocation(final SsnLocation l, final Instant dataUpdatedTime) {
        final PortProperties p = new PortProperties(l.getLocode(), l.getLocationName(), l.getCountry());
        final Point g = geometry(l.getWgs84Long(), l.getWgs84Lat());
        return new PortFeature(l.getLocode(), p, g, dataUpdatedTime);
    }

    private static Point geometry(final Double lon, final Double lat) {
        return lon == null || lat == null ? null : new Point(lon, lat);
    }
}
