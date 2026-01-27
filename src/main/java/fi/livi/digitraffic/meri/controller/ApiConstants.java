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

    /** Info api */
    public static final String INFO_TAG_V1 = "Info V1";
    public static final String API_INFO = API + "/info";

    // Apis in AWS ApiGW with Lambda implementation
    public static final String API_BRIDGE_LOCK_V1_DISRUPTIONS = "/api/bridge-lock/v1/disruptions";
    public static final String API_ATON_V1_FAULTS = "/api/aton/v1/faults";
    public static final String API_WINTER_NAVIGATION_V2_LOCATIONS = "/api/winter-navigation/v2/locations";
    public static final String API_WINTER_NAVIGATION_V2_VESSELS = "/api/winter-navigation/v2/vessels";
    public static final String API_WINTER_NAVIGATION_V2_DIRWAYS = "/api/winter-navigation/v2/dirways";
    
    private ApiConstants() {}
}
