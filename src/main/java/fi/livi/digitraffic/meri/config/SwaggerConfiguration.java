package fi.livi.digitraffic.meri.config;

import static com.google.common.base.Predicates.or;
import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_BETA_BASE_PATH;
import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_V1_BASE_PATH;
import static springfox.documentation.builders.PathSelectors.regex;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Predicate;

import fi.livi.digitraffic.meri.model.geojson.GeoJsonObject;
import fi.livi.digitraffic.meri.model.geojson.Geometry;
import fi.livi.digitraffic.meri.model.geojson.LineString;
import fi.livi.digitraffic.meri.model.geojson.MultiLineString;
import fi.livi.digitraffic.meri.model.geojson.MultiPoint;
import fi.livi.digitraffic.meri.model.geojson.MultiPolygon;
import fi.livi.digitraffic.meri.model.geojson.Point;
import fi.livi.digitraffic.meri.model.geojson.Polygon;
import fi.livi.digitraffic.meri.service.AisApiInfoService;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.DocExpansion;
import springfox.documentation.swagger.web.ModelRendering;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@ConditionalOnWebApplication
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Autowired
    AisApiInfoService aisApiInfoService;

    @Bean
    public Docket metadataApi() {
        return getDocket("metadata-api", getMetadataApiPaths());
    }

    @Bean
    public Docket betaApi() {
        return getDocket("metadata-api-beta", regex(API_BETA_BASE_PATH + "/*.*"));
    }

    @Bean
    UiConfiguration uiConfiguration() {
        return UiConfigurationBuilder.builder()
            .docExpansion(DocExpansion.LIST)
            .defaultModelRendering(ModelRendering.MODEL)
            .build();
    }

    private Docket getDocket(final String groupName, final Predicate<String> apiPaths) {
        final TypeResolver typeResolver = new TypeResolver();
        return new Docket(DocumentationType.SWAGGER_2)
            .groupName(groupName)
            .directModelSubstitute(ZonedDateTime.class, String.class)
            .directModelSubstitute(LocalDateTime.class, String.class)
            .directModelSubstitute(LocalDate.class, String.class)
            .directModelSubstitute(Date.class, String.class)
            // Inheritance not working as expected
            // https://github.com/springfox/springfox/issues/2407#issuecomment-462319647
            .additionalModels(typeResolver.resolve(GeoJsonObject.class),
                typeResolver.resolve(Geometry.class),
                typeResolver.resolve(LineString.class),
                typeResolver.resolve(MultiLineString.class),
                typeResolver.resolve(MultiPoint.class),
                typeResolver.resolve(MultiPolygon.class),
                typeResolver.resolve(Point.class),
                typeResolver.resolve(Polygon.class)
            )
            .produces(new HashSet<>(Collections.singletonList(MediaType.APPLICATION_JSON_UTF8_VALUE)))
            .apiInfo(aisApiInfoService.getApiInfo())
            .select()
            .paths(apiPaths)
            .build()
            .useDefaultResponseMessages(false);
    }

    /**
     * Declares api paths to document by Swagger
     * @return api paths
     */
    private static Predicate<String> getMetadataApiPaths() {
        return or(
                regex(API_V1_BASE_PATH +"/*.*")
                //, regex(API_V2_PATH +"/*.*")
        );
    }
}
