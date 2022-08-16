package fi.livi.digitraffic.meri.util;

import java.util.Arrays;
import java.util.Objects;

public final class NullUtil {
    private NullUtil() {}

    public static boolean allNullOrNoneNull(final Object... objects) {
        return
            Arrays.stream(objects).allMatch(Objects::isNull)
                ||
            Arrays.stream(objects).allMatch(Objects::nonNull);
    }
}
