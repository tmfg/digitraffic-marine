package fi.livi.digitraffic.meri.dto;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.common.dto.LastModifiedSupport;
import fi.livi.digitraffic.meri.dto.geojson.FeatureCollection;

@JsonPropertyOrder({ "type",
                     "dataUpdatedTime",
                     "features" })
public class RootDataObjectDto<FeatureType extends LastModifiedSupport> extends FeatureCollection<FeatureType> {

    public RootDataObjectDto(final Instant dataUpdatedTime,
                             final List<FeatureType> features) {
        super(features, dataUpdatedTime);
    }
}
