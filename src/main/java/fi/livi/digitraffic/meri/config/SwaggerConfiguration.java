package fi.livi.digitraffic.meri.config;

import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_BETA_BASE_PATH;
import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_V1_BASE_PATH;
import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_V2_BASE_PATH;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import fi.livi.digitraffic.meri.documentation.AisApiInfo;
import fi.livi.digitraffic.meri.service.AisApiInfoService;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springdoc.core.customizers.OpenApiCustomiser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnWebApplication
@Configuration
public class SwaggerConfiguration {

    private final AisApiInfoService aisApiInfoService;
    private final AisApiInfo aisApiInfo;
    private final String host;
    private final String scheme;

    @Autowired
    public SwaggerConfiguration(final AisApiInfoService aisApiInfoService,
                                final @Value("${dt.domain.url}") String domainUrl) throws URISyntaxException {
        this.aisApiInfoService = aisApiInfoService;
        this.aisApiInfo = aisApiInfoService.getApiInfo();

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
    public GroupedOpenApi metadataApi() {
        return GroupedOpenApi.builder()
            .group("metadata-api")
            .pathsToMatch(API_V1_BASE_PATH + "/**", API_V2_BASE_PATH + "/**")
            .addOpenApiCustomiser(openApiConfig())
            .build();
    }
    @Bean
    public GroupedOpenApi metadataApiBeta() {
        return GroupedOpenApi.builder()
            .group("metadata-api-beta")
            .pathsToMatch(API_BETA_BASE_PATH + "/**")
            .addOpenApiCustomiser(openApiConfig())
            .build();
    }

    // https://springdoc.org/#swagger-ui-properties
    @Bean
    public SwaggerUiConfigProperties swaggerUiConfig() {
        SwaggerUiConfigProperties config = new SwaggerUiConfigProperties();
        config.setDocExpansion("none");
        config.setDefaultModelRendering("example");
        return config;
    }

    private OpenApiCustomiser openApiConfig() {
        return openApi -> {
            openApi
                .setInfo(new Info()
                    .title(aisApiInfo.getTitle())
                    .description(aisApiInfo.getDescription())
                    .version(aisApiInfo.getVersion())
                    .contact(aisApiInfo.getContact())
                    .termsOfService(aisApiInfo.getTermsOfServiceUrl())
                    .license(aisApiInfo.getLicense()));

            Server server = new Server();
            server.setUrl(host);

            openApi
                .setServers(Arrays.asList(server));

        };
    }

}
