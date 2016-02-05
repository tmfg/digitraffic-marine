package fi.livi.digitraffic.meri.config;

import static com.google.common.base.Predicates.or;
import static fi.livi.digitraffic.meri.config.AisApplicationConfiguration.*;
import static springfox.documentation.builders.PathSelectors.regex;

import com.google.common.base.Predicate;
import fi.livi.digitraffic.meri.service.AisApiInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Autowired
    AisApiInfoService aisApiInfoService;

    @Bean
    public Docket metadataApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("metadata-api")
                .apiInfo(aisApiInfoService.getApiInfo())
                .select()
                .paths(getMetadataApiPaths())
                .build();
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