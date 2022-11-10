package fi.livi.digitraffic.meri.model;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.model.geojson.FeatureCollection;
import fi.livi.digitraffic.meri.util.TimeUtil;

@JsonPropertyOrder({ "type",
                     "dataUpdatedTime",
                     "features" })
public class RootDataObjectDto<FeatureType> extends FeatureCollection<FeatureType> {

    public RootDataObjectDto(final ZonedDateTime dataUpdatedTime,
                             final List<FeatureType> features) {
        super(features, TimeUtil.toInstant(dataUpdatedTime));
    }

    public RootDataObjectDto(final Instant dataUpdatedTime,
                             final List<FeatureType> features) {
        super(features, dataUpdatedTime);
    }
}
