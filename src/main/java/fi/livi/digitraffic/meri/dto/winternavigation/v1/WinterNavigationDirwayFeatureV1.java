package fi.livi.digitraffic.meri.dto.winternavigation.v1;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.dto.geojson.Feature;
import fi.livi.digitraffic.meri.dto.geojson.Geometry;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder({ "name",
                     "type",
                     "geometry",
                     "properties" })
public class WinterNavigationDirwayFeatureV1 extends Feature<Geometry, WinterNavigationDirwayPropertiesV1> {

    @Schema(description = "Name of the dirway", requiredMode = Schema.RequiredMode.REQUIRED)
    public final String name;

    public WinterNavigationDirwayFeatureV1(final String name,
                                           final WinterNavigationDirwayPropertiesV1 properties,
                                           final Geometry geometry) {
        super(geometry, properties);
        this.name = name;
    }
}
