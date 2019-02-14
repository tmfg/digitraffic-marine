package fi.livi.digitraffic.meri.controller;

import fi.livi.digitraffic.meri.controller.ais.AisRadioMsg;
import fi.livi.digitraffic.meri.model.ais.AISMessage;
import fi.livi.digitraffic.meri.model.ais.VesselMessage;

import static fi.livi.digitraffic.meri.controller.ais.AisRadioMsgParameters.*;

public final class AisMessageConverter {
    private AisMessageConverter() {}

    /**
     * Location message
     * @param aisRadioMsg
     * @return
     */
    public static AISMessage convertLocation(final AisRadioMsg aisRadioMsg) {
        AISMessage.Geometry geometry = new AISMessage.Geometry(
            aisRadioMsg.getDoubleParam(LONGITUDE),
            aisRadioMsg.getDoubleParam(LATITUDE),
            new AISMessage.Geometry.SpatialReference(0));

        AISMessage.AISAttributes attributes = new AISMessage.AISAttributes(
            aisRadioMsg.getIntParam(USER_ID),
            aisRadioMsg.getTimestamp(),
            getDoubleValue(aisRadioMsg, SOG, 1023), // BigDecimal or int
            getDoubleValue(aisRadioMsg, COG, 3600), // BigDecimal or int
            getIntValue(aisRadioMsg, NAVIGATIONAL_STATUS, 15),
            getIntValue(aisRadioMsg, RATE_OF_TURN, -128),
            getIntValue(aisRadioMsg, POSITION_ACCURACY, 0),
            getIntValue(aisRadioMsg, TRUE_HEADING, 511),
            getIntValue(aisRadioMsg, RAIM_FLAG, 0),
            getIntValue(aisRadioMsg, TIME_STAMP, 60)
        );

        final AISMessage msg = new AISMessage(geometry, attributes);
        return msg;
    }

    /**
     * Metadata message
     * @param aisRadioMsg
     * @return
     */
    public static VesselMessage convertMetadata(final AisRadioMsg aisRadioMsg) {
        final VesselMessage.VesselAttributes attributes = new VesselMessage.VesselAttributes(
            aisRadioMsg.getIntParam(USER_ID), // int
            aisRadioMsg.getIntParam(IMO_NUMBER), // int
            aisRadioMsg.getTimestamp(),
            aisRadioMsg.getStringParam(CALL_SIGN), // String
            aisRadioMsg.getStringParam(NAME), // String
            getIntValue(aisRadioMsg, TYPE_OF_SHIP_AND_CARGO_TYPE, 0),
            getIntValue(aisRadioMsg, A_DIMENSION_OF_SHIP_REFERENCE_FOR_POSITION, 0),
            getIntValue(aisRadioMsg, B_DIMENSION_OF_SHIP_REFERENCE_FOR_POSITION, 0),
            getIntValue(aisRadioMsg, C_DIMENSION_OF_SHIP_REFERENCE_FOR_POSITION, 0),
            getIntValue(aisRadioMsg, C_DIMENSION_OF_SHIP_REFERENCE_FOR_POSITION, 0),
            getIntValue(aisRadioMsg, TYPE_OF_ELECTRONIC_POSITION_FIXING_DEVICE, 0),
            Integer.parseInt(getStringValue(aisRadioMsg, ETA, "2460")), // String
            aisRadioMsg.getDecimalParam(MAXIMUM_PRESENT_STATIC_DRAUGHT).intValue(), // BigDecimal
            aisRadioMsg.getStringParam(DESTINATION) // String
        );

        final VesselMessage msg = new VesselMessage(attributes);
        return msg;
    }

    private static int getIntValue(final AisRadioMsg aisRadioMsg, final String name, final int defaultValue) {
        if (!aisRadioMsg.containsParam(name)) {
            return defaultValue;
        }

        return aisRadioMsg.getIntParam(name);
    }

    private static double getDoubleValue(final AisRadioMsg aisRadioMsg, final String name, final double defaultValue) {
        if (!aisRadioMsg.containsParam(name)) {
            return defaultValue;
        }

        return aisRadioMsg.getDoubleParam(name);
    }

    private static String getStringValue(final AisRadioMsg aisRadioMsg, final String name, final String defaultValue) {
        if (!aisRadioMsg.containsParam(name)) {
            return defaultValue;
        }

        return aisRadioMsg.getStringParam(name);
    }
}
