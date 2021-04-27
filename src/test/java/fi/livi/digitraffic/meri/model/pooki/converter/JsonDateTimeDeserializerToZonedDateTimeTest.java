package fi.livi.digitraffic.meri.model.pooki.converter;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import fi.livi.digitraffic.meri.AbstractTestBase;

public class JsonDateTimeDeserializerToZonedDateTimeTest extends AbstractTestBase {
    private final JsonDateTimeDeserializerToZonedDateTime deserializer = new JsonDateTimeDeserializerToZonedDateTime();

    private void testParsing(final String expectedString, final String input) {
        final ZonedDateTime expected = expectedString == null ? null : ZonedDateTime.parse(expectedString);
        final ZonedDateTime actual = deserializer.parseDateQuietly(input);

        Assert.assertEquals(expected, actual);

    }

    @Test
    public void simple() {
        testParsing("2016-01-01T01:01:01+02:00[Europe/Helsinki]", "1.1.2016 1:1:1");
    }

    @Test
    public void full() {
        testParsing("2016-01-01T01:01:01+02:00[Europe/Helsinki]", "01.01.2016 01:01:01");
    }

    @Test
    public void simpleWithoutSeconds() {
        testParsing("2016-01-01T01:01:00+02:00[Europe/Helsinki]", "1.1.2016 1:1");
    }

    @Test
    public void fullWithoutSeconds() {
        testParsing("2016-01-01T01:01:00+02:00[Europe/Helsinki]", "01.01.2016 01:01");
    }

    @Test
    public void simpleWithoutTime() {
        testParsing("2016-01-01T00:00:00+02:00[Europe/Helsinki]", "1.1.2016");
    }

    @Test
    public void fullWithoutTime() {
        testParsing("2016-01-01T00:00:00+02:00[Europe/Helsinki]", "01.01.2016");
    }

    @Test
    public void rubbish() {
        testParsing(null, "rubbish");
    }
}
