package fi.livi.digitraffic.meri.model.winternavigation;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import fi.livi.digitraffic.meri.model.RootDataObjectDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonPropertyOrder({ "type",
                     "dataUpdatedTime",
                     "features" })
@ApiModel(description = "GeoJSON FeatureCollection object")
public class WinterNavigationDirwayFeatureCollection extends RootDataObjectDto {

    @ApiModelProperty(required = true)
    public final List<WinterNavigationDirwayFeature> features;

    @ApiModelProperty(allowableValues = "FeatureCollection", required = true)
    public final String type = "FeatureCollection";

    public WinterNavigationDirwayFeatureCollection(final ZonedDateTime dataUpdatedTime,
                                                   final List<WinterNavigationDirwayFeature> features) {
        super(dataUpdatedTime);
        this.features = features;
    }
}
