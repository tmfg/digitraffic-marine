package fi.livi.digitraffic.meri.model.winternavigation;

public enum PositionAccuracy {

    NO_DGPS(0),
    DGPS(1),
    INTERPOLATED(2),
    EXTRAPOLATED(3),
    USER_GIVEN(4);

    public final int value;

    PositionAccuracy(final int value) {
        this.value = value;
    }

    public static PositionAccuracy fromValue(final int value) {
        for (final PositionAccuracy positionAccuracy : values()) {
            if (positionAccuracy.value == value) {
                return positionAccuracy;
            }
        }
        throw new IllegalArgumentException("No matching PositionAccuracy for code={" + value + "}");
    }
}
