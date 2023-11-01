package fi.livi.digitraffic.meri.dto.winternavigation.v1;

import java.time.ZonedDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.dto.RootDataObjectDto;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder({ "type",
                     "dataUpdatedTime",
                     "features" })
@Schema(description = "GeoJSON FeatureCollection object")
public class WinterNavigationDirwayFeatureCollectionV1 extends RootDataObjectDto<WinterNavigationDirwayFeatureV1> {

    public WinterNavigationDirwayFeatureCollectionV1(final ZonedDateTime dataUpdatedTime,
                                                     final List<WinterNavigationDirwayFeatureV1> features) {
        super(dataUpdatedTime, features);
    }
}
