package fi.livi.digitraffic.meri.model.pooki.converter;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Test;

import fi.livi.digitraffic.meri.AbstractTestBase;

public class JsonDateTimeDeserializerToZonedDateTimeTest extends AbstractTestBase {

    @Test
    public void testJsonParsing() throws IOException {

        List<Pair<String, String >> values =
                Arrays.asList(
                        // expected, parsed
                        Pair.of("2016-01-01T01:01:01+02:00[Europe/Helsinki]", "1.1.2016 1:1:1"),
                        Pair.of("2016-01-01T01:01:01+02:00[Europe/Helsinki]", "01.01.2016 01:01:01"),
                        Pair.of("2016-01-01T01:01:00+02:00[Europe/Helsinki]", "1.1.2016 1:1"),
                        Pair.of("2016-01-01T01:01:00+02:00[Europe/Helsinki]", "01.01.2016 01:01"),
                        Pair.of("2016-01-01T00:00:00+02:00[Europe/Helsinki]", "1.1.2016"),
                        Pair.of("2016-01-01T00:00:00+02:00[Europe/Helsinki]", "01.01.2016"));

        values.stream().forEach(p -> {
            ZonedDateTime expected = ZonedDateTime.parse(p.getLeft());
            ZonedDateTime actual = JsonDateTimeDeserializerToZonedDateTime.parseDateQuietly(p.getRight());
            Assert.assertEquals(expected, actual);
        });
    }
}
