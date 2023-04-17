package fi.livi.digitraffic.meri;

import static java.time.ZoneOffset.UTC;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Random;

import javax.persistence.EntityManager;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { "config.test=true", "logging.level.org.springframework.test.context.transaction.TransactionContext=WARN",
                                   "quartz.enabled=false", "cache.allowedMmsis = 200", "dt.scheduled.annotation.enabled=false",
                                   "marine.datasource.hikari.maximum-pool-size=2" })
@AutoConfigureMockMvc
public abstract class AbstractTestBase {

    private static final Logger log = LoggerFactory.getLogger(AbstractTestBase.class);
    @Autowired
    protected ResourceLoader resourceLoader;

    @Autowired(required = false) // not for daemon tests
    protected MockMvc mockMvc;

    @Autowired(required = true) // not for daemon tests
    protected EntityManager entityManager;


    protected String readFile(final String filename) throws IOException {
        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource(filename).getFile());

        return FileUtils.readFileToString(file, "UTF-8");
    }

    protected Resource loadResource(final String pattern) throws IOException {
        return resourceLoader.getResource(pattern);
    }

    protected String readResourceContent(final String resourcePattern) throws IOException {
        final Resource datex2Resource = loadResource(resourcePattern);
        return FileUtils.readFileToString(datex2Resource.getFile(), StandardCharsets.UTF_8);
    }

    protected void assertTimesEqual(final ZonedDateTime t1, final ZonedDateTime t2) {
        if(t1 == null && t2 == null) return;

        if(t1 == null && t2 != null) {
            fail("was asserted to be null, was not");
        }

        if(t1 != null && t2 == null) {
            fail("given value was null");
        }

        final ZonedDateTime tz1 = t1.withZoneSameInstant(UTC);
        final ZonedDateTime tz2 = t2.withZoneSameInstant(UTC);

        assertEquals(tz1, tz2);
    }

    protected static int getRandom(final int minInclusive, final int maxExclusive) {
        final Random random = new Random();
        return random.ints(minInclusive, maxExclusive).findFirst().orElseThrow();
    }

    protected ResultActions logInfoResponse(final ResultActions result) throws UnsupportedEncodingException {
        return logResponse(result, false);
    }

    protected ResultActions logDebugResponse(final ResultActions result) throws UnsupportedEncodingException {
        return logResponse(result, true);
    }
    private ResultActions logResponse(final ResultActions result, boolean debug) throws UnsupportedEncodingException {
        final String responseStr = result.andReturn().getResponse().getContentAsString();
        if (debug) {
            log.debug("\n" + responseStr);
        } else {
            log.info("\n" + responseStr);
        }
        return result;
    }

    protected ResultActions executeGet(final String url) throws Exception {
        final MockHttpServletRequestBuilder get = MockMvcRequestBuilders.get(url);
        get.contentType(MediaType.APPLICATION_JSON);
        return mockMvc.perform(get);
    }

    protected ResultActions expectOk(final ResultActions rs) throws Exception {
        return rs.andExpect(status().isOk());
    }
}