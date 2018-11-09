package fi.livi.digitraffic.meri.model.winternavigation;

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
public class WinterNavigationShipFeatureCollection extends RootDataObjectDto {

    @ApiModelProperty(required = true)
    public final List<WinterNavigationShipFeature> features;

    @ApiModelProperty(allowableValues = "FeatureCollection", required = true)
    public final String type = "FeatureCollection";

    public WinterNavigationShipFeatureCollection(final ZonedDateTime dataLastUpdated, final List<WinterNavigationShipFeature> features) {
        super(dataLastUpdated);
        this.features = features;
    }
}
