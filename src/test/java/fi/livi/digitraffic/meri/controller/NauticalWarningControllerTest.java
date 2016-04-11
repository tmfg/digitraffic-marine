package fi.livi.digitraffic.meri.controller;

import static fi.livi.digitraffic.meri.config.AisApplicationConfiguration.API_V1_BASE_PATH;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;
import java.util.LinkedList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import fi.livi.digitraffic.meri.AisApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AisApplication.class)
@WebIntegrationTest({ "server.port=18080", "management.port=18081" })
@Component
public class NauticalWarningControllerTest {

    RestTemplate template = new TestRestTemplate();

    @Autowired
    public PookiDummyController pookiDummyController;

    @Autowired
    public NauticalWarningController nauticalWarningController;

    private String localPath = "http://localhost:18080" + API_V1_BASE_PATH;
    private static String api = "/nautical-warnings";

    @Test
    public void testNauticalWarnings() throws Exception {

        // Mock Pooki API identifier for this test
        String key = new Object() {
        }.getClass().getEnclosingMethod().getName();

        // Responses
        ResponseEntity<String> R1 = ResponseEntity.ok().body(PookiDummyController.DUMMY_DATA);

        pookiDummyController.setResponseQueue(key, new LinkedList<>(Arrays.asList(R1)));

        // Use Mock Pooki API in implementation
        nauticalWarningController.setPOOKI_URL("http://localhost:18080/test/nautical-warnings/" + key);

        ResponseEntity<String> response = template.getForEntity(localPath + api, String.class);

        assertThat("Expected ok response", !RestUtil.isError(response.getStatusCode()));

        //TODO Assert actual JSON contents
        assertThat(response.toString(), containsString("{'type':'FeatureCollection','features':[type':'Feature','properties':ID':980,"));

    }

    @Test
    public void testForwardErrorIfErrorRepeats() {

        // Mock Pooki API identifier for this test
        String key = new Object() {
        }.getClass().getEnclosingMethod().getName();

        // Responses
        ResponseEntity<String> R1 = ResponseEntity.status(405).body(null);
        ResponseEntity<String> R2 = ResponseEntity.status(405).body(null);
        ResponseEntity<String> R3 = ResponseEntity.ok().body(PookiDummyController.DUMMY_DATA);

        pookiDummyController.setResponseQueue(key, new LinkedList<>(Arrays.asList(R1, R2, R3)));

        // Use Mock Pooki API in implementation
        nauticalWarningController.setPOOKI_URL("http://localhost:18080/test/nautical-warnings/" + key);

        // Execute get
        ResponseEntity<String> response = template.getForEntity(localPath + api, String.class);

        assertThat("Expected error status code as result", RestUtil.isError(response.getStatusCode()));

        assertThat("Number of Pooki API calls should be 2 (one get and one retry)", pookiDummyController.getResponseQueue(key).size(), equalTo(1));

    }

    @Test
    public void testRetryOnceIfErrorAndReturnOKIfRetryIsOk() {
        // Mock Pooki API identifier for this test
        String key = new Object() {
        }.getClass().getEnclosingMethod().getName();

        // Responses
        ResponseEntity<String> R1 = ResponseEntity.status(405).body(null);
        ResponseEntity<String> R2 = ResponseEntity.ok().body(PookiDummyController.DUMMY_DATA);

        pookiDummyController.setResponseQueue(key, new LinkedList<>(Arrays.asList(R1, R2, R2)));

        // Use Mock Pooki API in implementation
        nauticalWarningController.setPOOKI_URL("http://localhost:18080/test/nautical-warnings/" + key);

        // Execute get
        ResponseEntity<String> response = template.getForEntity(localPath + api, String.class);

        assertThat("Expected ok result", !RestUtil.isError(response.getStatusCode()));

        assertThat("Number of Pooki API calls should be 2 (one get and one retry)", pookiDummyController.getResponseQueue(key).size(), equalTo(1));

    }

}
