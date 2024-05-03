package fi.livi.digitraffic.meri;

import static java.time.ZoneOffset.UTC;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Random;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.UnexpectedRollbackException;

import jakarta.persistence.EntityManager;

@TestPropertySource(properties = {
        "config.test=true",
        "logging.level.org.springframework.test.context.transaction.TransactionContext=WARN",
        "quartz.enabled=false",
        "cache.allowedMmsis = 200",
        "dt.scheduled.annotation.enabled=false",
        "marine.datasource.hikari.maximum-pool-size=2"
})
abstract class AbstractTestBase {

    private static final Logger log = LoggerFactory.getLogger(AbstractTestBase.class);
    @Autowired
    protected ResourceLoader resourceLoader;

    @Autowired // not for daemon tests
    protected EntityManager entityManager;

    @Autowired
    protected ConfigurableListableBeanFactory beanFactory;

    protected void addXmlResponseFromFile(final MockWebServer server, final String fileName) throws IOException {
        final String response = readFile(fileName);

        server.enqueue(new MockResponse().setBody(response).setHeader("Content-Type", "application/xml"));
    }

    protected void expectResponse(final MockWebServer server, final String urlPath) throws InterruptedException {
        final RecordedRequest request = server.takeRequest();

        Assertions.assertEquals(urlPath, request.getRequestUrl().encodedPath());
    }


    protected String readFile(final String filename) throws IOException {
        final ClassLoader classLoader = getClass().getClassLoader();
        final File file = new File(classLoader.getResource(filename).getFile());

        return FileUtils.readFileToString(file, "UTF-8");
    }

    protected Resource loadResource(final String pattern) {
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

    private ResultActions logResponse(final ResultActions result, final boolean debug) throws UnsupportedEncodingException {
        final String responseStr = result.andReturn().getResponse().getContentAsString();
        if (debug) {
            log.debug("\n" + responseStr);
        } else {
            log.info("\n" + responseStr);
        }
        return result;
    }

    public Instant getTransactionTimestamp() {
        return (Instant)entityManager.createNativeQuery("select now()").getSingleResult();
    }

    public static void commitAndEndTransactionAndStartNew() {
        if (!TestTransaction.isActive()) {
            TestTransaction.start();
        } else {
            TestTransaction.flagForCommit();
            try {
                TestTransaction.end();
            } catch (final UnexpectedRollbackException e) {
                // Don't care as now transaction is rolled back and ended
                // This sometimes happens in test cleanup as the transaction is marked as roll back only and is ok
            }
            TestTransaction.start();
            TestTransaction.flagForCommit();
        }
    }

    public <T> T loadBean(final Class<T> tClass) {
        return isBeanRegistered(tClass) ?
                    beanFactory.getBean(tClass) :
                    beanFactory.createBean(tClass);
    }


    protected boolean isBeanRegistered(final Class<?> c) {
        try {
            return beanFactory.getBean(c) != null;
        } catch (final NoSuchBeanDefinitionException e) {
            return false;
        }
    }
}