package fi.livi.digitraffic.meri.controller.portnet;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import fi.livi.digitraffic.meri.domain.portnet.Berth;
import fi.livi.digitraffic.meri.domain.portnet.PortArea;
import fi.livi.digitraffic.meri.domain.portnet.SsnLocation;
import fi.livi.digitraffic.meri.model.geojson.Point;
import fi.livi.digitraffic.meri.model.portnet.metadata.BerthFeature;
import fi.livi.digitraffic.meri.model.portnet.metadata.BerthFeatureCollection;
import fi.livi.digitraffic.meri.model.portnet.metadata.BerthProperties;
import fi.livi.digitraffic.meri.model.portnet.metadata.FeatureCollectionList;
import fi.livi.digitraffic.meri.model.portnet.metadata.PortAreaFeature;
import fi.livi.digitraffic.meri.model.portnet.metadata.PortAreaFeatureCollection;
import fi.livi.digitraffic.meri.model.portnet.metadata.PortAreaProperties;
import fi.livi.digitraffic.meri.model.portnet.metadata.SsnLocationFeature;
import fi.livi.digitraffic.meri.model.portnet.metadata.SsnLocationFeatureCollection;
import fi.livi.digitraffic.meri.model.portnet.metadata.SsnLocationProperties;

public final class SsnLocationConverter {
    private SsnLocationConverter() {}

    public static FeatureCollectionList convert(final Instant timestamp, final List<SsnLocation> locations, final List<PortArea> portAreas, final List<Berth> berths) {
        return new FeatureCollectionList(timestamp == null ? null : timestamp.atZone(ZoneId.systemDefault()),
                convertSsnLocations(locations),
                convertPortAreas(portAreas),
                convertBerths(berths));
    }

    private static BerthFeatureCollection convertBerths(final List<Berth> berths) {
        if(berths.isEmpty()) {
            return null;
        }

        return new BerthFeatureCollection(berths.stream().map(SsnLocationConverter::convertBerth).collect(Collectors.toList()));
    }

    private static BerthFeature convertBerth(final Berth b) {
        final BerthProperties p = new BerthProperties(b.getBerthName());

        return new BerthFeature(b.getBerthKey().getLocode(), b.getBerthKey().getPortAreaCode(), b.getBerthKey().getBerthCode(), p);
    }

    private static PortAreaFeatureCollection convertPortAreas(final List<PortArea> portAreas) {
        if(portAreas.isEmpty()) {
            return null;
        }

        return new PortAreaFeatureCollection(portAreas.stream().map(SsnLocationConverter::convertPortArea).collect(Collectors.toList()));
    }

    private static PortAreaFeature convertPortArea(final PortArea pa) {
        final PortAreaProperties p = new PortAreaProperties(pa.getPortAreaName());
        final Point g = null; // TODO: DPO-89
        return new PortAreaFeature(pa.getPortAreaKey().getLocode(), pa.getPortAreaKey().getPortAreaCode(), p, g);
    }

    private static SsnLocationFeatureCollection convertSsnLocations(final List<SsnLocation> locations) {
        if(locations.isEmpty()) {
            return null;
        }

        return new SsnLocationFeatureCollection(locations.stream().map(SsnLocationConverter::convertLocation).collect(Collectors.toList()));
    }

    private static SsnLocationFeature convertLocation(final SsnLocation l) {
        final SsnLocationProperties p = new SsnLocationProperties(l.getLocationName(), l.getCountry());
        final Point g = geometry(l.getWgs84Long(), l.getWgs84Lat());
        return new SsnLocationFeature(l.getLocode(), p, g);
    }

    private static Point geometry(final Double lon, final Double lat) {
        return lon == null || lat == null ? null : new Point(lon, lat);
    }
}
