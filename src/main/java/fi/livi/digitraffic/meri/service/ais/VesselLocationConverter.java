package fi.livi.digitraffic.meri.service.ais;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import fi.livi.digitraffic.meri.domain.ReadOnlyCreatedAndModifiedFields;
import fi.livi.digitraffic.meri.domain.ais.VesselLocation;
import fi.livi.digitraffic.meri.model.ais.AISMessage;
import fi.livi.digitraffic.meri.model.ais.VesselLocationFeature;
import fi.livi.digitraffic.meri.model.ais.VesselLocationFeatureCollection;
import fi.livi.digitraffic.meri.model.ais.VesselLocationProperties;
import fi.livi.digitraffic.meri.model.geojson.Point;

public final class VesselLocationConverter {
    private VesselLocationConverter() {}

    public static VesselLocationFeatureCollection createFeatureCollection(final List<VesselLocation> locations) {
        final Instant dataUpdatedTime = locations.stream().map(ReadOnlyCreatedAndModifiedFields::getModified).filter(Objects::nonNull).max(Instant::compareTo).orElse(Instant.EPOCH);
        return new VesselLocationFeatureCollection(features(locations), dataUpdatedTime);
    }

    public static List<VesselLocationFeature> features(final List<VesselLocation> locations) {
        return locations.stream().map(VesselLocationConverter::convert).collect(Collectors.toList());
    }

    public static VesselLocationFeature convert(final VesselLocation l) {
        final VesselLocationProperties properties = new VesselLocationProperties(l.getMmsi(), l.getSog(), l.getCog(), l.getNavStat(),
                l.getRot(), l.isPosAcc(), l.isRaim(), l.getHeading(), l.getTimestamp(), l.getTimestampExternal());
        final Point p = new Point(l.getX(), l.getY());

        return new VesselLocationFeature(l.getMmsi(), properties, p);
    }

    public static VesselLocationFeature convert(final AISMessage ais) {
        final AISMessage.AISAttributes a = ais.attributes;
        final VesselLocationProperties properties = new VesselLocationProperties(a.mmsi, a.sog, a.cog, a.navStat,
                a.rot, a.posAcc == 1, a.raim == 1, a.heading, a.timestamp, a.timestampExternal);
        final Point p = new Point(ais.geometry.x, ais.geometry.y);

        return new VesselLocationFeature(a.mmsi, properties, p);
    }
}
