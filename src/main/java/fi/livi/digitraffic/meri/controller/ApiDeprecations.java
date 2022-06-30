package fi.livi.digitraffic.meri.controller;

/**
 * All deprecation dates
 *
 * I.e. Depracation on 1.1.2022 -> create two constants:
 * SINCE_2022_01_01    = "2022-01-01"
 * API_NOTE_2022_11_01 = DEPRECATED_TEXT + SINCE_2022_01_01;
 *
 * And add those values for deprecated APIs:
 * @Deprecated(forRemoval = true, since = SINCE_2022_01_01)
 * @Operation(summary = "Api description plaa plaa. " + ApiDeprecations.API_NOTE_2022_11_01)
 */
public final class ApiDeprecations {

    private static final String DEPRECATED_TEXT = "Will be removed after ";

    public static final String SINCE_2022_11_01 = "2022-11-01";
    public static final String API_NOTE_2022_11_01 = DEPRECATED_TEXT + SINCE_2022_11_01;

    private ApiDeprecations() {}

    public static String willBeDeprecated(final String sinceDate) {
        return "Will be deprecated from " + sinceDate;
    }
}
