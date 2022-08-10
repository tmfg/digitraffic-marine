package fi.livi.digitraffic.meri.controller;

public final class ApiConstants {
    /** API base */
    public static final String API = "/api";
    /** API versions */
    public static final String V1 = "/v1";
    public static final String BETA = "/beta";

    /** Port Call */
    public static final String PORT_CALL_BETA_TAG = "Port Call (BETA)";

    public static final String API_PORT_CALL = API + "/port-call";

    /** SSE */

    public static final String SSE_BETA_TAG = "SSE (BETA)";
    public static final String API_SSE = API + "/sse";

    /** Winter Navigation */
    public static final String WINTER_NAVIGATION_BETA_TAG = "Winter Navigation (BETA)";
    public static final String API_WINTER_NAVIGATION = API + "/winter-navigation";

    private ApiConstants() {}
}
