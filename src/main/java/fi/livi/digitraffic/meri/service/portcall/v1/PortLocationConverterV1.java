package fi.livi.digitraffic.meri.service.portcall.v1;

import java.time.Instant;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.livi.digitraffic.meri.dto.geojson.Point;
import fi.livi.digitraffic.meri.dto.portcall.v1.port.BerthCollection;
import fi.livi.digitraffic.meri.dto.portcall.v1.port.BerthProperties;
import fi.livi.digitraffic.meri.dto.portcall.v1.port.PortAreaFeature;
import fi.livi.digitraffic.meri.dto.portcall.v1.port.PortAreaFeatureCollection;
import fi.livi.digitraffic.meri.dto.portcall.v1.port.PortAreaProperties;
import fi.livi.digitraffic.meri.dto.portcall.v1.port.PortFeature;
import fi.livi.digitraffic.meri.dto.portcall.v1.port.PortFeatureCollection;
import fi.livi.digitraffic.meri.dto.portcall.v1.port.PortLocationDtoV1;
import fi.livi.digitraffic.meri.dto.portcall.v1.port.PortProperties;
import fi.livi.digitraffic.meri.model.portnet.Berth;
import fi.livi.digitraffic.meri.model.portnet.PortArea;
import fi.livi.digitraffic.meri.model.portnet.SsnLocation;

public final class PortLocationConverterV1 {
    private static final Logger log = LoggerFactory.getLogger(PortLocationConverterV1.class);
    private PortLocationConverterV1() {}

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
