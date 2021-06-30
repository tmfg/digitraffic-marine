package fi.livi.digitraffic.meri.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.Random;

public final class TimeUtilTest {
    @Test
    public void millisBetweenNulls() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> TimeUtil.millisBetween(null, ZonedDateTime.now()));
        Assertions.assertThrows(IllegalArgumentException.class, () -> TimeUtil.millisBetween(ZonedDateTime.now(), null));
    }

    @Test
    public void millisBetween() {
        final ZonedDateTime zdt1 = ZonedDateTime.now();
        final ZonedDateTime zdt2 = zdt1.plusSeconds(new Random().nextInt(1000));

        Assertions.assertTrue(TimeUtil.millisBetween(zdt1, zdt2) < 0);
        Assertions.assertTrue(TimeUtil.millisBetween(zdt2, zdt1) > 0);
    }

    @Test
    public void dateToStringNull() {
        Assertions.assertEquals("", TimeUtil.dateToString("PREFIX", null));
    }

    @Test
    public void timeToStringNull() {
        Assertions.assertEquals("", TimeUtil.timeToString("PREFIX", null));
    }
}