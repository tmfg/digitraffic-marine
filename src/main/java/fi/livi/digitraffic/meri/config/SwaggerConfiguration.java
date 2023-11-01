package fi.livi.digitraffic.meri.config;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fi.livi.digitraffic.meri.controller.ApiConstants;
import fi.livi.digitraffic.meri.documentation.MarineApiInfo;
import fi.livi.digitraffic.meri.service.MarineApiInfoService;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@ConditionalOnWebApplication
@Configuration
public class SwaggerConfiguration {

    private final MarineApiInfo marineApiInfo;
    private final String host;
    private final String scheme;

    private final static String API_PATHS = ApiConstants.API + "/**";
    private final static String BETA_PATHS = "/**" + ApiConstants.BETA + "/**";

    @Autowired
    public SwaggerConfiguration(final MarineApiInfoService marineApiInfoService,
                                final @Value("${dt.domain.url}") String domainUrl) throws URISyntaxException {
        this.marineApiInfo = marineApiInfoService.getApiInfo();

        final URI uri = new URI(domainUrl);

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
            .pathsToMatch(API_PATHS)
            .pathsToExclude(BETA_PATHS)
            .addOpenApiCustomizer(openApiConfig())
            .build();
    }
    @Bean
    public GroupedOpenApi marineApiBeta() {
        return GroupedOpenApi.builder()
            .group("marine-api-beta")
            .pathsToMatch(BETA_PATHS)
            .addOpenApiCustomizer(openApiConfig())
            .build();
    }

    private OpenApiCustomizer openApiConfig() {
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

            openApi.setServers(Collections.singletonList(server));
        };
    }

}
