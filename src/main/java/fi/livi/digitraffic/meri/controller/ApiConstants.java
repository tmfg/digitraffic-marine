package fi.livi.digitraffic.meri.controller;

public final class ApiConstants {
    /** API base */
    public static final String API = "/api";
    /** API versions */
    public static final String V1 = "/v1";
    public static final String BETA = "/beta";

    /** Port Call */
    public static final String PORT_CALL_V1_TAG = "Port Call V1";

    public static final String API_PORT_CALL = API + "/port-call";

    /** SSE */

    public static final String SSE_V1_TAG = "SSE V1";
    public static final String API_SSE = API + "/sse";
    
    /** AIS */
    public static final String AIS_V1_TAG = "AIS V1";
    public static final String API_AIS = API + "/ais";

    /** Winter Navigation */
    public static final String WINTER_NAVIGATION_V1_TAG = "Winter Navigation V1";
    public static final String API_WINTER_NAVIGATION = API + "/winter-navigation";

    private ApiConstants() {}
}
