package fi.livi.digitraffic.meri.dto.winternavigation.v1;

import java.time.ZonedDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.dto.RootDataObjectDto;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder({
        "type",
        "dataUpdatedTime",
        "features",
})
@Schema(description = "GeoJSON FeatureCollection object")
public class WinterNavigationPortFeatureCollectionV1 extends RootDataObjectDto<WinterNavigationPortFeatureV1> {

    public WinterNavigationPortFeatureCollectionV1(final ZonedDateTime dataLastUpdated, final List<WinterNavigationPortFeatureV1> features) {
        super(dataLastUpdated, features);
    }
}
