package fi.livi.digitraffic.meri.controller;

import static fi.livi.digitraffic.meri.config.AisApplicationConfiguration.API_V1_BASE_PATH;
import static fi.livi.digitraffic.meri.controller.NauticalWarningController.Status;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fi.livi.digitraffic.meri.AbstractTestBase;
import fi.livi.digitraffic.meri.AisApplication;
import fi.livi.digitraffic.util.RestUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AisApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Component
public class NauticalWarningControllerTest extends AbstractTestBase {
    private TestRestTemplate template = new TestRestTemplate();

    @Autowired
    private PookiDummyController pookiDummyController;

    @Autowired
    private NauticalWarningController nauticalWarningController;

    @Value("${local.server.port:#{null}}")
    private String serverPort;

    //private static final String LOCAL_PATH = "http://localhost:18080" + API_V1_BASE_PATH;
    private static final String API = "nautical-warnings";

    private String pookiBaseUrl;
    private String localUrl;
    private String dummyData;

    @Before
    public void createBaseUrl() throws IOException {
        pookiBaseUrl = String.format("http://localhost:%s/test/nautical-warnings/", serverPort);
        localUrl = String.format("http://localhost:%s%s", serverPort, API_V1_BASE_PATH);
        dummyData = readResourceContent("classpath:pooki/PUBLISHED.json");
    }

    @Test
    public void testAllNauticalWarnings() throws Exception {
        // Mock Pooki API identifier for this test
        final String key = new Object() {
        }.getClass().getEnclosingMethod().getName();

        // Responses
        final ResponseEntity<String> R1 = ResponseEntity.ok().body(dummyData);
        final ResponseEntity<String> R2 = ResponseEntity.ok().body(dummyData);
        final ResponseEntity<String> R3 = ResponseEntity.ok().body(dummyData);

        pookiDummyController.setResponseQueue(key, new LinkedList<>(Arrays.asList(R1, R2, R3)));

        // Use Mock Pooki API in implementation
        for (final Status status : new LinkedList<>(Arrays.asList(Status.DRAFT,
                Status.PUBLISHED,
                Status.ARCHIVED))) {

            final String s = status.toString().toLowerCase();
            final String pookiUrl = String.format(pookiBaseUrl + "%s/%s", s, key);
            nauticalWarningController.setPookiUrl(pookiUrl);

            final String requestUrl = String.format("%s/%s/%s", localUrl, API, s);
            final ResponseEntity<String> response = template.getForEntity(requestUrl, String.class);

            assertThat("Expected ok response", !RestUtil.isError(response.getStatusCode()));

            //TODO Assert actual JSON contents
            System.out.println(response.toString());
            assertThat(response.toString(), containsString("{\"id\":1162"));
            assertThat(response.toString(), containsString("\"areasFi\":\"AHVENANMERI\""));
            assertThat(response.toString(), containsString("\"givenTime\":\"2015-08-19T00:00:00+03:00\""));
            assertThat(response.toString(), containsString("\"type\":\"FeatureCollection\""));
            assertThat(response.toString(), containsString("\"type\":\"Feature\""));
            assertThat(response.toString(), containsString("\"type\":\"Polygon\""));

        }

    }

    @Test
    public void testForwardErrorIfErrorRepeats() {

        // Mock Pooki API identifier for this test
        final String key = new Object() {
        }.getClass().getEnclosingMethod().getName();

        // Responses
        final ResponseEntity<String> R1 = ResponseEntity.status(405).body(null);
        final ResponseEntity<String> R2 = ResponseEntity.status(405).body(null);
        final ResponseEntity<String> R3 = ResponseEntity.ok().body(dummyData);

        pookiDummyController.setResponseQueue(key, new LinkedList<>(Arrays.asList(R1, R2, R3)));

        // Use Mock Pooki API in implementation
        nauticalWarningController.setPookiUrl(pookiBaseUrl + "draft/" + key);

        // Execute get
        final String requestUrl = String.format("%s/%s/draft", localUrl, API);
        final ResponseEntity<String> response = template.getForEntity(requestUrl, String.class);

        assertThat("Expected error status code as result", RestUtil.isError(response.getStatusCode()));

        assertThat("Number of Pooki API calls should be 2 (one get and one retry, so queue should have one response left)",
                pookiDummyController.getResponseQueue(key).size(), equalTo(1));

    }

    @Test
    public void testRetryOnceIfErrorAndReturnOKIfRetryIsOk() {
        // Mock Pooki API identifier for this test
        final String key = new Object() {
        }.getClass().getEnclosingMethod().getName();

        // Responses
        final ResponseEntity<String> R1 = ResponseEntity.status(405).body(null);
        final ResponseEntity<String> R2 = ResponseEntity.ok().body(dummyData);
        final ResponseEntity<String> R3 = ResponseEntity.ok().body(dummyData);

        pookiDummyController.setResponseQueue(key, new LinkedList<>(Arrays.asList(R1, R2, R3)));

        // Use Mock Pooki API in implementation
        nauticalWarningController.setPookiUrl(pookiBaseUrl + "draft/" + key);

        // Execute get
        final ResponseEntity<String> response = template.getForEntity(String.format("%s/%s/draft", localUrl, API), String.class);

        assertThat("Expected ok result", !RestUtil.isError(response.getStatusCode()));

        assertThat("Number of Pooki API calls should be 2 (one get and one retry so queue should have one response left)",
                pookiDummyController.getResponseQueue(key).size(), equalTo(1));

    }



}
