package fi.livi.digitraffic.meri.controller;

import static fi.livi.digitraffic.meri.config.MarineApplicationConfiguration.API_V1_BASE_PATH;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.queryParam;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.ResponseActions;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import fi.livi.digitraffic.meri.AbstractTestBase;
import fi.livi.digitraffic.meri.service.nauticalwarning.NauticalWarningService.Status;
import fi.livi.digitraffic.meri.util.RestUtil;


@TestPropertySource(properties = "dt.pooki.url=/pooki_www/services/rest.ashx")
public class NauticalWarningControllerTest extends AbstractTestBase {

    private TestRestTemplate localRestTemplate = new TestRestTemplate();

    @Value("${local.server.port:#{null}}")
    private String serverPort;

    @Value("${dt.pooki.url}")
    private String pookiUrl;

    private String localNauticalWarningsUrl;
    private String dummyData;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer pookiMockServer;

    @BeforeEach
    public void createBaseUrl() throws IOException {
        localNauticalWarningsUrl = String.format("http://localhost:%s%s%s", serverPort, API_V1_BASE_PATH, "/nautical-warnings");
        dummyData = readResourceContent("classpath:pooki/PUBLISHED.json");
        pookiMockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void testAllNauticalWarnings() {
        pookiExpect(1, Status.PUBLISHED, true);
        pookiExpect(1, Status.ARCHIVED, true);

        // Use Mock Pooki API in implementation
        for (final Status status : Arrays.asList( Status.PUBLISHED, Status.ARCHIVED)) {
            // Execute get
            final ResponseEntity<String> response = requestWithStatus(status);
            assertThat("Expected ok response", !RestUtil.isError(response.getStatusCode()));

            final String body = response.getBody();

            assertThat(body, containsString("\"id\" : 1162"));
            assertThat(body, containsString("\"areasFi\" : \"AHVENANMERI\""));
            assertThat(body, containsString("\"creationTime\" : \"2015-08-19T00:00:00+03:00\""));
            assertThat(body, containsString("\"type\" : \"FeatureCollection\""));
            assertThat(body, containsString("\"type\" : \"Feature\""));
            assertThat(body, containsString("\"type\" : \"Polygon\""));
        }
        pookiMockServer.verify();
    }

    @Test
    public void testForwardErrorIfErrorRepeats() {
        pookiExpect(2, Status.PUBLISHED, false);

        // Execute get
        final ResponseEntity<String> response = requestWithStatus(Status.PUBLISHED);
        pookiMockServer.verify();

        assertThat("Expected error status code as result", RestUtil.isError(response.getStatusCode()));
    }

    @Test
    public void testRetryOnceIfErrorAndReturnOKIfRetryIsOk() {

        // Responses
        pookiExpect(1, Status.PUBLISHED, false);
        pookiExpect(1, Status.PUBLISHED, true);

        // Execute get
        final ResponseEntity<String> response = requestWithStatus(Status.PUBLISHED);
        pookiVerify();

        assertThat("Expected ok result", !RestUtil.isError(response.getStatusCode()));
    }

    @Test
    public void testReturnErrorIfIllegalStatusArgumentIsGiven() {
        pookiExpectNever();

        // Execute get with missing status argument
        final ResponseEntity<String> response = requestWithCustomStatus("wrong");
        pookiVerify();

        assertThat("Expected 404 result", response.getStatusCode().is4xxClientError());
    }

    @Test
    public void testReturnError404IfStatusArgumentIsMissing() {
        pookiExpectNever();

        // Execute get with missing status argument
        final ResponseEntity<String> response = requestWithCustomStatus(null);
        pookiVerify();

        assertThat("Expected 404 result", response.getStatusCode().is4xxClientError());
    }

    @Test
    public void testPooki500WithWrongCompressionHeadersWillNotBreakErrorHandlerDPO_90() {

        pookiExpectNoGzip(2, Status.PUBLISHED);

        final ResponseEntity<String> response = requestWithStatus(Status.PUBLISHED);
        pookiVerify();
        final String body = response.getBody();

        assertThat(body, containsString(String.format("\"status\" : %s", HttpStatus.BAD_GATEWAY.value())));
        assertThat(body, containsString(String.format("\"error\" : \"%s\"", HttpStatus.BAD_GATEWAY.getReasonPhrase())));
        assertThat(body, containsString("\"message\" : \"Bad Gateway. Pooki responded with error response.\""));
    }

    private ResponseEntity<String> requestWithStatus(final Status status) {
        return requestWithCustomStatus(status.toString().toLowerCase());
    }

    private ResponseEntity<String> requestWithCustomStatus(final String status) {
        final String requestUrl = status != null ?
                                  String.format("%s/%s", localNauticalWarningsUrl, status) : localNauticalWarningsUrl;
        return localRestTemplate.getForEntity(requestUrl, String.class);
    }

    private void pookiExpect(final int expectedCount, final Status expectedStatus, boolean returnOk) {

        final ResponseActions expected =
            pookiMockServer.expect(ExpectedCount.times(expectedCount), requestTo(startsWith(pookiUrl)))
                .andExpect(method(HttpMethod.GET))
                .andExpect(queryParam("crs", "EPSG:4326"))
                .andExpect(queryParam("layer", expectedStatus.layer));
        if (returnOk) {
            expected.andRespond(MockRestResponseCreators.withSuccess(dummyData, MediaType.APPLICATION_JSON));
        } else {
            expected.andRespond(MockRestResponseCreators.withStatus(METHOD_NOT_ALLOWED));
        }
    }

    private void pookiExpectNoGzip(int expectedCount, final Status expectedStatus) {
        final HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_ENCODING, "gzip");
        pookiMockServer.expect(ExpectedCount.times(expectedCount), requestTo(startsWith(pookiUrl)))
            .andExpect(method(HttpMethod.GET))
            .andExpect(queryParam("crs", "EPSG:4326"))
            .andExpect(queryParam("layer", expectedStatus.layer))
            .andRespond(MockRestResponseCreators.withStatus(INTERNAL_SERVER_ERROR)
                        .body("this body is not gzipped").headers(headers));
    }

    private void pookiExpectNever() {
        pookiMockServer.expect(ExpectedCount.never(), requestTo(startsWith(pookiUrl)));
    }

    private void pookiVerify() {
        pookiMockServer.verify();
    }
}
