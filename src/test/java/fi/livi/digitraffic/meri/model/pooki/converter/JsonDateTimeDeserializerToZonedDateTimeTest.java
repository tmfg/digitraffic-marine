package fi.livi.digitraffic.meri.model.pooki.converter;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import fi.livi.digitraffic.meri.AbstractTestBase;

@RunWith(Theories.class)
public class JsonDateTimeDeserializerToZonedDateTimeTest extends AbstractTestBase {
    public static @DataPoints Pair[] values = {
                        // expected, parsed
                        Pair.of("2016-01-01T01:01:01+02:00[Europe/Helsinki]", "1.1.2016 1:1:1"),
                        Pair.of("2016-01-01T01:01:01+02:00[Europe/Helsinki]", "01.01.2016 01:01:01"),
                        Pair.of("2016-01-01T01:01:00+02:00[Europe/Helsinki]", "1.1.2016 1:1"),
                        Pair.of("2016-01-01T01:01:00+02:00[Europe/Helsinki]", "01.01.2016 01:01"),
                        Pair.of("2016-01-01T00:00:00+02:00[Europe/Helsinki]", "1.1.2016"),
                        Pair.of("2016-01-01T00:00:00+02:00[Europe/Helsinki]", "01.01.2016"),
                        Pair.of(null, "rubbish")
    };

    @Theory
    public void parse(final Pair<String,String> p) {
        final JsonDateTimeDeserializerToZonedDateTime deserializer = new JsonDateTimeDeserializerToZonedDateTime();

        final ZonedDateTime expected = p.getLeft() == null ? null : ZonedDateTime.parse(p.getLeft());
        final ZonedDateTime actual = deserializer.parseDateQuietly(p.getRight());

        assertTimesEqual(expected, actual);
    }
}
