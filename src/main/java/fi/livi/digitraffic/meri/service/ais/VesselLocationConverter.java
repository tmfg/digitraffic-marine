package fi.livi.digitraffic.meri.service.ais;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import fi.livi.digitraffic.meri.dto.ais.v1.VesselLocationFeatureCollectionV1;
import fi.livi.digitraffic.meri.dto.ais.v1.VesselLocationFeatureV1;
import fi.livi.digitraffic.meri.dto.ais.v1.VesselLocationPropertiesV1;
import fi.livi.digitraffic.meri.dto.geojson.Point;
import fi.livi.digitraffic.meri.model.ReadOnlyCreatedAndModifiedFields;
import fi.livi.digitraffic.meri.model.ais.VesselLocation;

public final class VesselLocationConverter {
    private VesselLocationConverter() {}

    public static VesselLocationFeatureCollectionV1 createFeatureCollection(final List<VesselLocation> locations) {
        final Instant dataUpdatedTime = locations.stream().map(ReadOnlyCreatedAndModifiedFields::getModified).filter(Objects::nonNull).max(Instant::compareTo).orElse(Instant.EPOCH);
        return new VesselLocationFeatureCollectionV1(features(locations), dataUpdatedTime);
    }

    public static List<VesselLocationFeatureV1> features(final List<VesselLocation> locations) {
        return locations.stream().map(VesselLocationConverter::convert).collect(Collectors.toList());
    }

    public static VesselLocationFeatureV1 convert(final VesselLocation l) {
        final VesselLocationPropertiesV1 properties = new VesselLocationPropertiesV1(l.getMmsi(), l.getSog(), l.getCog(), l.getNavStat(),
                l.getRot(), l.isPosAcc(), l.isRaim(), l.getHeading(), l.getTimestamp(), l.getTimestampExternal(), l.getModified());
        final Point p = new Point(l.getX(), l.getY());

        return new VesselLocationFeatureV1(l.getMmsi(), properties, p);
    }
}
