package fi.livi.digitraffic.meri.config;

import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_BETA_BASE_PATH;
import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_V1_BASE_PATH;
import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_V2_BASE_PATH;
import static springfox.documentation.builders.PathSelectors.regex;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import fi.livi.digitraffic.meri.controller.MediaTypes;
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

    private final AisApiInfoService aisApiInfoService;

    private final String host;
    private final String scheme;

    @Autowired
    public SwaggerConfiguration(final AisApiInfoService aisApiInfoService,
                                final @Value("${dt.domain.url}") String domainUrl) throws URISyntaxException {
        Assert.notNull(aisApiInfoService, "AisApiInfoService can't be null");
        this.aisApiInfoService = aisApiInfoService;
        URI uri = new URI(domainUrl);

        final int port = uri.getPort();
        if (port > -1) {
            host = uri.getHost() + ":" + port;
        } else {
            host = uri.getHost();
        }
        scheme = uri.getScheme();
    }

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
            // There is bugs in online validator, so not use it at the moment ie. https://github.com/swagger-api/validator-badge/issues/97
            //.validatorUrl("https://online.swagger.io/validator")
            .build();
    }

    private Docket getDocket(final String groupName, final Predicate<String> apiPaths) {
        return new Docket(DocumentationType.SWAGGER_2)
            .host(host)
            .protocols(Set.of(scheme))
            .groupName(groupName)
            .produces(new HashSet<>(Collections.singletonList(MediaTypes.MEDIA_TYPE_APPLICATION_JSON)))
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
        return regex(API_V1_BASE_PATH +"/*.*").or(
               regex(API_V2_BASE_PATH +"/*.*"));
    }
}
