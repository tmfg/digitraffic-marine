package fi.livi.digitraffic.meri.service.winternavigation.dto;

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
}
