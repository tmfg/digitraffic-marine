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


    /** Info api */
    public static final String INFO_BETA_TAG = "Info (BETA)";
    public static final String API_INFO = API + "/info";

    // Apis in AWS ApiGW with Lambda implementation
    public static final String API_NAUTICAL_WARNING_V1_WARNINGS = "/api/nautical-warning/v1/warnings";
    public static final String API_BRIDGE_LOCK_V1_DISRUPTIONS = "/api/bridge-lock/v1/disruptions";
    public static final String API_ATON_V1_FAULTS = "/api/aton/v1/faults";

    private ApiConstants() {}
}
