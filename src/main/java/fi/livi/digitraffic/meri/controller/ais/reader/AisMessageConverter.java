package fi.livi.digitraffic.meri.controller.ais.reader;

import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.A_DIMENSION_OF_SHIP_REFERENCE_FOR_POSITION;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.B_DIMENSION_OF_SHIP_REFERENCE_FOR_POSITION;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.CALL_SIGN;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.COG;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.C_DIMENSION_OF_SHIP_REFERENCE_FOR_POSITION;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.DESTINATION;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.D_DIMENSION_OF_SHIP_REFERENCE_FOR_POSITION;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.ETA;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.IMO_NUMBER;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.LATITUDE;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.LONGITUDE;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.MAXIMUM_PRESENT_STATIC_DRAUGHT;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.NAME;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.NAVIGATIONAL_STATUS;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.POSITION_ACCURACY;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.RAIM_FLAG;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.RATE_OF_TURN;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.SOG;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.TIME_STAMP;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.TRUE_HEADING;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.TYPE_OF_ELECTRONIC_POSITION_FIXING_DEVICE;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.TYPE_OF_SHIP_AND_CARGO_TYPE;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.USER_ID;

import java.math.BigDecimal;

import fi.livi.digitraffic.meri.dto.ais.external.AISMessage;
import fi.livi.digitraffic.meri.dto.ais.external.VesselMessage;

public final class AisMessageConverter {
    private AisMessageConverter() {}

    /**
     * Location message
     * @param aisRadioMsg
     * @return
     */
    public static AISMessage convertLocation(final AisRadioMsg aisRadioMsg) {
        final AISMessage.Geometry geometry = new AISMessage.Geometry(
            aisRadioMsg.getDoubleParam(LONGITUDE),
            aisRadioMsg.getDoubleParam(LATITUDE),
            new AISMessage.Geometry.SpatialReference(0));

        final AISMessage.AISAttributes attributes = new AISMessage.AISAttributes(
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

        return new AISMessage(geometry, attributes);
    }

    /**
     * Metadata message
     * @param aisRadioMsg
     * @return
     */
    public static VesselMessage convertMetadata(final AisRadioMsg aisRadioMsg) {
        final VesselMessage.VesselAttributes attributes = new VesselMessage.VesselAttributes(
            aisRadioMsg.getIntParam(USER_ID), // int
            getIntValue(aisRadioMsg, IMO_NUMBER, 0), //aisRadioMsg.getIntParam(IMO_NUMBER), // int
            aisRadioMsg.getTimestamp(),
            getTrimmedStringValue(aisRadioMsg, CALL_SIGN), //aisRadioMsg.getStringParam(CALL_SIGN), // String
            getTrimmedStringValue(aisRadioMsg, NAME), //aisRadioMsg.getStringParam(NAME), // String
            getMaskedShipAndCargoType(getShipType(aisRadioMsg)),
            getIntValue(aisRadioMsg, A_DIMENSION_OF_SHIP_REFERENCE_FOR_POSITION, 0),
            getIntValue(aisRadioMsg, B_DIMENSION_OF_SHIP_REFERENCE_FOR_POSITION, 0),
            getIntValue(aisRadioMsg, C_DIMENSION_OF_SHIP_REFERENCE_FOR_POSITION, 0),
            getIntValue(aisRadioMsg, D_DIMENSION_OF_SHIP_REFERENCE_FOR_POSITION, 0),
            getIntValue(aisRadioMsg, TYPE_OF_ELECTRONIC_POSITION_FIXING_DEVICE, 0),
            getIntValue(aisRadioMsg, ETA, 2460), //Integer.parseInt(getStringValue(aisRadioMsg, ETA, "2460")), // String
            getDraught(aisRadioMsg),  //aisRadioMsg.getDecimalParam(MAXIMUM_PRESENT_STATIC_DRAUGHT).intValue(), // BigDecimal
            getTrimmedStringValue(aisRadioMsg, DESTINATION) //aisRadioMsg.getStringParam(DESTINATION) // String
        );

        return new VesselMessage(attributes);
    }

    public static int getShipType(final AisRadioMsg aisRadioMsg) {
        if (aisRadioMsg == null) {
            return 0;
        }

        return getIntValue(aisRadioMsg, TYPE_OF_SHIP_AND_CARGO_TYPE, 0);
    }

    public static int getDraught(final AisRadioMsg aisRadioMsg) {
        if (aisRadioMsg == null || !aisRadioMsg.containsParam(MAXIMUM_PRESENT_STATIC_DRAUGHT)) {
            return 0;
        }

        return aisRadioMsg.getDecimalParam(MAXIMUM_PRESENT_STATIC_DRAUGHT).multiply(BigDecimal.TEN).intValue();
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

    private static String getTrimmedStringValue(final AisRadioMsg aisRadioMsg, final String name) {
        final String value = getStringValue(aisRadioMsg, name, "");

        return value.replaceAll("@*$", "");
    }

    private static String getStringValue(final AisRadioMsg aisRadioMsg, final String name, final String defaultValue) {
        if (!aisRadioMsg.containsParam(name)) {
            return defaultValue;
        }

        return aisRadioMsg.getStringParam(name);
    }

    /**
     * DPO-1028
     * Mask ship type with following rules:
     * 0 -> 0 (Default value or not in use)
     * 1 - 19 -> keep original value
     * 20 - 29 -> 20
     * 30 - 39 -> keep original value
     * 40 - 49 -> 40
     * 50 - 59 -> keep original value
     * 60 - 69 -> 60
     * 70 - 79 -> 70
     * 80 - 89 -> 80
     * 90 - 99 -> 90
     * > 99 -> keep original value
     * @param shipType
     * @return
     */
    private static int getMaskedShipAndCargoType(final int shipType) {
        if (shipType < 20 || shipType > 99) {
            return shipType;
        }

        final int tenth = shipType / 10;
        return (tenth == 3 || tenth == 5) ? shipType : tenth * 10;
    }
}
