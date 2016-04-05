package fi.livi.digitraffic.meri.controller;

import static fi.livi.digitraffic.meri.config.AisApplicationConfiguration.API_V1_BASE_PATH;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import fi.livi.digitraffic.meri.AisApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AisApplication.class)
@WebIntegrationTest({ "server.port=18080", "management.port=18081"})
public class NauticalWarningControllerTest {

    RestTemplate template = new TestRestTemplate();

    @Value("${local.server.port}")
    int port;

    @Test
    public void testNauticalWarnings() throws Exception {
        String localPath = "http://localhost:" + port + API_V1_BASE_PATH;
        String api = "/nautical-warnings";
        ResponseEntity<String> response = template.getForEntity(localPath + api, String.class);

        assertThat(response.toString(), containsString("http://localhost:18080"));

    }
}
