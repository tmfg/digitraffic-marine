package fi.livi.digitraffic.meri.controller;

import static fi.livi.digitraffic.meri.config.AisApplicationConfiguration.API_V1_BASE_PATH;
import static fi.livi.digitraffic.meri.controller.NauticalWarningController.Status;
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
import fi.livi.digitraffic.util.RestUtil;

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
    private static String api = "nautical-warnings";

    @Test
    public void testAllNauticalWarnings() throws Exception {

        // Mock Pooki API identifier for this test
        String key = new Object() {
        }.getClass().getEnclosingMethod().getName();

        // Responses
        ResponseEntity<String> R1 = ResponseEntity.ok().body(DUMMY_DATA);
        ResponseEntity<String> R2 = ResponseEntity.ok().body(DUMMY_DATA);
        ResponseEntity<String> R3 = ResponseEntity.ok().body(DUMMY_DATA);

        pookiDummyController.setResponseQueue(key, new LinkedList<>(Arrays.asList(R1, R2, R3)));

        // Use Mock Pooki API in implementation
        for (Status status : new LinkedList<>(Arrays.asList(Status.DRAFT,
                Status.PUBLISHED,
                Status.ARCHIVED))) {

            String s = status.toString().toLowerCase();
            final String pooki_url = String.format("http://localhost:18080/test/nautical-warnings/%s/%s", s, key);
            nauticalWarningController.setPOOKI_URL(pooki_url);

            final String requestUrl = String.format("%s/%s/%s", localPath, api, s);
            ResponseEntity<String> response = template.getForEntity(requestUrl, String.class);

            assertThat("Expected ok response", !RestUtil.isError(response.getStatusCode()));

            //TODO Assert actual JSON contents
            assertThat(response.toString(), containsString("{'type':'FeatureCollection','features':[type':'Feature','properties':ID':980,"));
        }

    }

    @Test
    public void testForwardErrorIfErrorRepeats() {

        // Mock Pooki API identifier for this test
        String key = new Object() {
        }.getClass().getEnclosingMethod().getName();

        // Responses
        ResponseEntity<String> R1 = ResponseEntity.status(405).body(null);
        ResponseEntity<String> R2 = ResponseEntity.status(405).body(null);
        ResponseEntity<String> R3 = ResponseEntity.ok().body(DUMMY_DATA);

        pookiDummyController.setResponseQueue(key, new LinkedList<>(Arrays.asList(R1, R2, R3)));

        // Use Mock Pooki API in implementation
        nauticalWarningController.setPOOKI_URL("http://localhost:18080/test/nautical-warnings/draft/" + key);

        // Execute get
        final String requestUrl = String.format("%s/%s/draft", localPath, api);
        ResponseEntity<String> response = template.getForEntity(requestUrl, String.class);

        assertThat("Expected error status code as result", RestUtil.isError(response.getStatusCode()));

        assertThat("Number of Pooki API calls should be 2 (one get and one retry, so queue should have one response left)",
                pookiDummyController.getResponseQueue(key).size(), equalTo(1));

    }

    @Test
    public void testRetryOnceIfErrorAndReturnOKIfRetryIsOk() {
        // Mock Pooki API identifier for this test
        String key = new Object() {
        }.getClass().getEnclosingMethod().getName();

        // Responses
        ResponseEntity<String> R1 = ResponseEntity.status(405).body(null);
        ResponseEntity<String> R2 = ResponseEntity.ok().body(DUMMY_DATA);
        ResponseEntity<String> R3 = ResponseEntity.ok().body(DUMMY_DATA);

        pookiDummyController.setResponseQueue(key, new LinkedList<>(Arrays.asList(R1, R2, R3)));

        // Use Mock Pooki API in implementation
        nauticalWarningController.setPOOKI_URL("http://localhost:18080/test/nautical-warnings/draft/" + key);

        // Execute get
        ResponseEntity<String> response = template.getForEntity(String.format("%s/%s/draft", localPath, api), String.class);

        assertThat("Expected ok result", !RestUtil.isError(response.getStatusCode()));

        assertThat("Number of Pooki API calls should be 2 (one get and one retry so queue should have one response left)",
                pookiDummyController.getResponseQueue(key).size(), equalTo(1));

    }

    private static String DUMMY_DATA = "{'type':'FeatureCollection','features':[type':'Feature','properties':ID':980,"
            + "{''TOOLTIP':' NAVTEX COASTAL [Tallennettu 01.09.2014]'},"
            + "{''geometry':type':'Point','coordinates':[2327921.0,8469808.99999796,0.0]}},"
            + "{'type':'Feature','properties':ID':1000,'TOOLTIP':' NAVTEX COASTAL"
            + "{'[Tallennettu 04.09.2014]'},'geometry':type':'Point',"
            + "{''coordinates':[2374205.9999999967,8465888.999997966,0.0]}},type':'Feature',"
            + "{''properties':ID':1040,'TOOLTIP':' NAVTEX COASTAL [Tallennettu 22.01.2015]'},"
            + "{''geometry':type':'Polygon','coordinates':[[[2341315.0000000028,"
            + "{'8467098.9999979511,0.0],[2341289.9999999949,8467034.9999979641,0.0],"
            + "{'[2341385.0000000042,8467034.9999979641,0.0],[2341427.9999999967,"
            + "{'8467081.9999979679,0.0],[2341315.0000000028,8467098.9999979511,0.0]]]}},"
            + "{'type':'Feature','properties':ID':1212,'TOOLTIP':'Varoituksia veneilijöille"
            + "{'[Tallennettu 18.09.2015]'},'geometry':type':'Point',"
            + "{''coordinates':[2332479.9999999981,8464195.9999979716,0.0]}},"
            + "{'type':'Feature','properties':ID':1214,'TOOLTIP':'NAVIGATIONAL WARNING"
            + "{'[Tallennettu 01.10.2015]'},'geometry':type':'Point',"
            + "{''coordinates':[2421004.0000000023,8424745.9999979716,0.0]}},"
            + "{'type':'Feature','properties':ID':1215,'TOOLTIP':'NAVIGATIONAL WARNING"
            + "{'[Tallennettu 01.10.2015]'},'geometry':type':'Point',"
            + "{''coordinates':[2421004.0000000023,8424745.9999979716,0.0]}},"
            + "{'type':'Feature','properties':ID':1221,'TOOLTIP':'Varoituksia veneilijöille"
            + "{'[Tallennettu 10.12.2015]'},'geometry':type':'Point',"
            + "{''coordinates':[2384048.0000000033,8433840.9999979753,0.0]}},"
            + "{'type':'Feature','properties':ID':1241,'TOOLTIP':'NAVIGATIONAL WARNING"
            + "{'[Tallennettu 03.02.2016]'},'geometry':type':'Point',"
            + "{''coordinates':[2340687.9999999949,8465569.9999979641,0.0]}},"
            + "{'type':'Feature','properties':ID':1242,'TOOLTIP':'NAVIGATIONAL WARNING"
            + "{'[Tallennettu 03.02.2016]'},'geometry':type':'Point',"
            + "{''coordinates':[2340478.0000000005,8467729.99999796,0.0]}},type':'Feature',"
            + "{''properties':ID':1244,'TOOLTIP':'NAVIGATIONAL WARNING [Tallennettu"
            + "{'03.02.2016]'},'geometry':type':'Point','coordinates':[2343413.0,"
            + "{'8468334.9999979548,0.0]}}]}";

}
