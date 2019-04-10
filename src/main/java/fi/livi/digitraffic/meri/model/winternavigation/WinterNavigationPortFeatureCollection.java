package fi.livi.digitraffic.meri.model.winternavigation;

import java.time.ZonedDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.model.RootDataObjectDto;
import io.swagger.annotations.ApiModel;

@JsonPropertyOrder({
        "type",
        "dataUpdatedTime",
        "features",
})
@ApiModel(description = "GeoJSON FeatureCollection object")
public class WinterNavigationPortFeatureCollection extends RootDataObjectDto<WinterNavigationPortFeature> {

    public WinterNavigationPortFeatureCollection(final ZonedDateTime dataLastUpdated, final List<WinterNavigationPortFeature> features) {
        super(dataLastUpdated, features);
    }
}
