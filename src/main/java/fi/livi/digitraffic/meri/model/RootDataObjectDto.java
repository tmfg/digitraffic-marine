package fi.livi.digitraffic.meri.model;

import java.time.ZonedDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.model.geojson.FeatureCollection;
import io.swagger.annotations.ApiModelProperty;

@JsonPropertyOrder({ "type",
                     "dataUpdatedTime",
                     "features" })
public class RootDataObjectDto<FeatureType> extends FeatureCollection<FeatureType> {

    @ApiModelProperty(value = "Data last updated timestamp in ISO 8601 format with time offsets from UTC (eg. 2016-04-20T12:38:16.328+03:00 or 2018-11-09T09:41:09Z)", required = true)
    private final ZonedDateTime dataUpdatedTime;

    public RootDataObjectDto(final ZonedDateTime dataUpdatedTime,
                             final List<FeatureType> features) {
        super(features);
        this.dataUpdatedTime = dataUpdatedTime;
    }

    public ZonedDateTime getDataUpdatedTime() {
        return dataUpdatedTime;
    }
}
