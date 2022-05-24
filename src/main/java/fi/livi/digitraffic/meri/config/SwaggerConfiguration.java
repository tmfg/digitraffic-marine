package fi.livi.digitraffic.meri.config;

import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_BETA_BASE_PATH;
import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_V1_BASE_PATH;
import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_V2_BASE_PATH;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import fi.livi.digitraffic.meri.documentation.MarineApiInfo;
import fi.livi.digitraffic.meri.service.MarineApiInfoService;

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

    private final MarineApiInfoService marineApiInfoService;
    private final MarineApiInfo marineApiInfo;
    private final String host;
    private final String scheme;

    @Autowired
    public SwaggerConfiguration(final MarineApiInfoService marineApiInfoService,
                                final @Value("${dt.domain.url}") String domainUrl) throws URISyntaxException {
        this.marineApiInfoService = marineApiInfoService;
        this.marineApiInfo = marineApiInfoService.getApiInfo();

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
    public GroupedOpenApi marineApi() {
        return GroupedOpenApi.builder()
            .group("marine-api")
            .pathsToMatch(API_V1_BASE_PATH + "/**", API_V2_BASE_PATH + "/**")
            .addOpenApiCustomiser(openApiConfig())
            .build();
    }
    @Bean
    public GroupedOpenApi marineApiBeta() {
        return GroupedOpenApi.builder()
            .group("marine-api-beta")
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
                    .title(marineApiInfo.getTitle())
                    .description(marineApiInfo.getDescription())
                    .version(marineApiInfo.getVersion())
                    .contact(marineApiInfo.getContact())
                    .termsOfService(marineApiInfo.getTermsOfServiceUrl())
                    .license(marineApiInfo.getLicense()));

            final Server server = new Server();
            final String url = scheme + "://" + host;
            server.setUrl(url);

            openApi.setServers(Arrays.asList(server));
        };
    }

}
