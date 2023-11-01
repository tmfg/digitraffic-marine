/**
 * -----
 * Copyright (C) 2018 Digia
 * -----
 *
 * Licensed under the EUPL, Version 1.2 or – as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * https://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 *
 * 2019.02.14: Original work is used here as an base implementation
 */
package fi.livi.digitraffic.meri.controller.ais.reader;

import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.AIS_VERSION_INDICATOR;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.ALTITUDE_GNSS;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.ALTITUDE_SENSOR;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.ASSIGNED_MODE_FLAG;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.A_DIMENSION_OF_SHIP_REFERENCE_FOR_POSITION;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.B_DIMENSION_OF_SHIP_REFERENCE_FOR_POSITION;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.CALL_SIGN;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.CLASS_B_BAND_FLAG;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.CLASS_B_DISPLAY_FLAG;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.CLASS_B_DSC_FLAG;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.CLASS_B_MESSAGE_22_FLAG;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.CLASS_B_UNIT_FLAG;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.COG;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.COMMUNICATION_STATE;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.COMMUNICATION_STATE_SELECTOR_FLAG;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.C_DIMENSION_OF_SHIP_REFERENCE_FOR_POSITION;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.DESTINATION;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.DTE;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.D_DIMENSION_OF_SHIP_REFERENCE_FOR_POSITION;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.ETA;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.IMO_NUMBER;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.LATITUDE;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.LONGITUDE;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.MAXIMUM_PRESENT_STATIC_DRAUGHT;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.NAME;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.NAVIGATIONAL_STATUS;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.PART_NUMBER;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.POSITION_ACCURACY;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.POSITION_LATENCY;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.RAIM_FLAG;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.RATE_OF_TURN;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.SOG;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.SPARE;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.SPARE2;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.SPARE3;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.SPECIAL_MANOEUVRE_INDICATOR;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.TIME_STAMP;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.TRUE_HEADING;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.TYPE_OF_ELECTRONIC_POSITION_FIXING_DEVICE;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.TYPE_OF_SHIP_AND_CARGO_TYPE;
import static fi.livi.digitraffic.meri.controller.ais.reader.AisRadioMsgParameters.VENDOR_ID;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AisRadioMsgParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(AisRadioMsgParser.class);
    private static final String SUPPORTED_MESSAGE_TYPE_REGEX = "!..VDM";
    private static final Set<String> SUPPORTED_MESSAGE_CLASS_SUFFIXES = new HashSet<>(Arrays.asList("1", "2", "3", "5", "9", "18", "19", "24A", "24B", "27"));
    private static final Set<String> SUPPORTED_METADATA_CLASS_SUFFIXES = new HashSet<>(Arrays.asList("5", "24A", "24B"));
    private static final Set<String> SUPPORTED_POSITION_CLASS_SUFFIXES = new HashSet<>(Arrays.asList("1", "2", "3", "9", "18", "19", "27"));
    private static final Set<String> CLASS_B_MESSAGE_SUFFIXES = new HashSet<>(Arrays.asList("18", "19", "24A", "24B"));

    private enum RAW_LINE_COLUMN {
        MESSAGE_TYPE, TOTAL_NUMBER_OF_SENTENCES_NEEDED, SENTENCE_NUMBER,
        SEQUENTIAL_MESSAGE_IDENTIFIER, AIS_CHANNEL, RADIO_MSG,
        NUMBER_OF_FILL_BITS_AND_CHECK_VALUE
    }

    public static boolean isSupportedMessageType(final String data) {
        try {
            final String msgType = getColumnValue(RAW_LINE_COLUMN.MESSAGE_TYPE, data);

            if (!msgType.matches(SUPPORTED_MESSAGE_TYPE_REGEX)) {
                LOGGER.info("Unsupported message type: {}", msgType);
                return false;
            }

            if (data.split(",").length < 6) {
                LOGGER.info("Too few columns: {}", data);
                return false;
            }

            return true;
        } catch (final Exception e) {
            LOGGER.info("Unsupported message structure: {}", data);
            return false;
        }
    }

    public static AisRadioMsg parseToAisRadioMessage(final String rawLine) {
        return parseToAisRadioMessage(Collections.singletonList(rawLine), getBinaryMsg(rawLine));
    }

    public static AisRadioMsg parseToAisRadioMessage(final String firstRawLine, final String secondRawLine) {
        return parseToAisRadioMessage(Arrays.asList(firstRawLine, secondRawLine), getBinaryMsg(firstRawLine).concat(getBinaryMsg(secondRawLine)));
    }

    private static AisRadioMsg parseToAisRadioMessage(final List<String> rawLines, final String binaryMsg) {
        final String msgClassSuffix = getMessageClassSuffix(binaryMsg);

        if (!SUPPORTED_MESSAGE_CLASS_SUFFIXES.contains(msgClassSuffix)) {
            //LOGGER.info("Unsupported message: {}", msgClassSuffix);
            return null;
        }

        final AisRadioMsg msg = new AisRadioMsg(
            binaryMsg,
            rawLines,
            getMessageType(msgClassSuffix),
            getMessageClass(msgClassSuffix));

        switch (msgClassSuffix) {
            case "1":
            case "2":
            case "3": //  Position message (class A)
                msg.add(NAVIGATIONAL_STATUS, msg.getUnsignedInteger(4));
                msg.add(RATE_OF_TURN, msg.getSignedInteger(8));
                msg.add(SOG, msg.getUnsignedDecimal(10, 10, 1));
                msg.add(POSITION_ACCURACY, msg.getUnsignedInteger(1));
                msg.add(LONGITUDE, msg.getSignedDecimal(28, 600000, 6));
                msg.add(LATITUDE, msg.getSignedDecimal(27, 600000, 6));
                msg.add(COG, msg.getUnsignedDecimal(12, 10, 1));
                msg.add(TRUE_HEADING, msg.getUnsignedInteger(9));
                msg.add(TIME_STAMP, msg.getUnsignedInteger(6));
                msg.add(SPECIAL_MANOEUVRE_INDICATOR, msg.getUnsignedInteger(2));
                msg.add(SPARE, msg.getUnsignedInteger(3));
                msg.add(RAIM_FLAG, msg.getUnsignedInteger(1));
                msg.add(COMMUNICATION_STATE, msg.getHexString(19));

                break;
            case "5": // Metadata message (class A)
                msg.add(AIS_VERSION_INDICATOR, msg.getUnsignedInteger(2));
                msg.add(IMO_NUMBER, msg.getUnsignedInteger(30));
                msg.add(CALL_SIGN, msg.getStringValue(42));
                msg.add(NAME, msg.getStringValue(120));
                msg.add(TYPE_OF_SHIP_AND_CARGO_TYPE, msg.getUnsignedInteger(8));
                //msg.add(DIMENSION_OF_SHIP_REFERENCE_FOR_POSITION, msg.getDimensionOfShip30bits());
                msg.add(A_DIMENSION_OF_SHIP_REFERENCE_FOR_POSITION, msg.getA_DimensionOfShip());
                msg.add(B_DIMENSION_OF_SHIP_REFERENCE_FOR_POSITION, msg.getB_DimensionOfShip());
                msg.add(C_DIMENSION_OF_SHIP_REFERENCE_FOR_POSITION, msg.getC_DimensionOfShip());
                msg.add(D_DIMENSION_OF_SHIP_REFERENCE_FOR_POSITION, msg.getD_DimensionOfShip());
                msg.add(TYPE_OF_ELECTRONIC_POSITION_FIXING_DEVICE, msg.getUnsignedInteger(4));
                //msg.add(ETA, msg.getEta20bits());
                msg.add(ETA, msg.getUnsignedInteger(20));
                msg.add(MAXIMUM_PRESENT_STATIC_DRAUGHT, msg.getUnsignedDecimal(8, 10, 1));
                msg.add(DESTINATION, msg.getStringValue(120));
                msg.add(DTE, msg.getUnsignedInteger(1));
                msg.add(SPARE, msg.getUnsignedInteger(1));

                break;
            case "9": // Standard Search and Rescue Aircraft Position Report
                msg.add(ALTITUDE_GNSS, msg.getUnsignedInteger(12));
                msg.add(SOG, msg.getUnsignedInteger(10));
                msg.add(POSITION_ACCURACY, msg.getUnsignedInteger(1));
                msg.add(LONGITUDE, msg.getSignedDecimal(28, 600000, 6));
                msg.add(LATITUDE, msg.getSignedDecimal(27, 600000, 6));
                msg.add(COG, msg.getUnsignedDecimal(12, 10, 1));
                msg.add(TIME_STAMP, msg.getUnsignedInteger(6));
                msg.add(ALTITUDE_SENSOR, msg.getUnsignedInteger(1));
                msg.add(SPARE, msg.getUnsignedInteger(7));
                msg.add(DTE, msg.getUnsignedInteger(1));
                msg.add(SPARE2, msg.getUnsignedInteger(3));
                msg.add(ASSIGNED_MODE_FLAG, msg.getUnsignedInteger(1));
                msg.add(RAIM_FLAG, msg.getUnsignedInteger(1));
                msg.add(COMMUNICATION_STATE_SELECTOR_FLAG, msg.getUnsignedInteger(1));
                msg.add(COMMUNICATION_STATE, msg.getHexString(19));

                break;
            case "18": // Position message (class B)
                msg.add(SPARE, msg.getUnsignedInteger(8));
                msg.add(SOG, msg.getUnsignedDecimal(10, 10, 1));
                msg.add(POSITION_ACCURACY, msg.getUnsignedInteger(1));
                msg.add(LONGITUDE, msg.getSignedDecimal(28, 600000, 6));
                msg.add(LATITUDE, msg.getSignedDecimal(27, 600000, 6));
                msg.add(COG, msg.getUnsignedDecimal(12, 10, 1));
                msg.add(TRUE_HEADING, msg.getUnsignedInteger(9));
                msg.add(TIME_STAMP, msg.getUnsignedInteger(6));
                msg.add(SPARE2, msg.getUnsignedInteger(2));
                msg.add(CLASS_B_UNIT_FLAG, msg.getUnsignedInteger(1));
                msg.add(CLASS_B_DISPLAY_FLAG, msg.getUnsignedInteger(1));
                msg.add(CLASS_B_DSC_FLAG, msg.getUnsignedInteger(1));
                msg.add(CLASS_B_BAND_FLAG, msg.getUnsignedInteger(1));
                msg.add(CLASS_B_MESSAGE_22_FLAG, msg.getUnsignedInteger(1));
                msg.add(ASSIGNED_MODE_FLAG, msg.getUnsignedInteger(1));
                msg.add(RAIM_FLAG, msg.getUnsignedInteger(1));
                msg.add(COMMUNICATION_STATE_SELECTOR_FLAG, msg.getUnsignedInteger(1));
                msg.add(COMMUNICATION_STATE, msg.getHexString(19));

                break;
            case "19": // Position message extended (class B)
                msg.add(SPARE, msg.getUnsignedInteger(8));
                msg.add(SOG, msg.getUnsignedDecimal(10, 10, 1));
                msg.add(POSITION_ACCURACY, msg.getUnsignedInteger(1));
                msg.add(LONGITUDE, msg.getSignedDecimal(28, 600000, 6));
                msg.add(LATITUDE, msg.getSignedDecimal(27, 600000, 6));
                msg.add(COG, msg.getUnsignedDecimal(12, 10, 1));
                msg.add(TRUE_HEADING, msg.getUnsignedInteger(9));
                msg.add(TIME_STAMP, msg.getUnsignedInteger(6));
                msg.add(SPARE2, msg.getUnsignedInteger(4));
                msg.add(NAME, msg.getStringValue(120));
                msg.add(TYPE_OF_SHIP_AND_CARGO_TYPE, msg.getUnsignedInteger(8));
                //msg.add(DIMENSION_OF_SHIP_REFERENCE_FOR_POSITION, msg.getDimensionOfShip30bits());
                msg.add(A_DIMENSION_OF_SHIP_REFERENCE_FOR_POSITION, msg.getA_DimensionOfShip());
                msg.add(B_DIMENSION_OF_SHIP_REFERENCE_FOR_POSITION, msg.getB_DimensionOfShip());
                msg.add(C_DIMENSION_OF_SHIP_REFERENCE_FOR_POSITION, msg.getC_DimensionOfShip());
                msg.add(D_DIMENSION_OF_SHIP_REFERENCE_FOR_POSITION, msg.getD_DimensionOfShip());
                msg.add(TYPE_OF_ELECTRONIC_POSITION_FIXING_DEVICE, msg.getUnsignedInteger(4));
                msg.add(RAIM_FLAG, msg.getUnsignedInteger(1));
                msg.add(DTE, msg.getUnsignedInteger(1));
                msg.add(ASSIGNED_MODE_FLAG, msg.getUnsignedInteger(1));
                msg.add(SPARE3, msg.getUnsignedInteger(4));

                break;
            case "24A": // Metadata part A (class B)
                msg.add(PART_NUMBER, msg.getUnsignedInteger(2));
                msg.add(NAME, msg.getStringValue(120));

                break;
            case "24B": // Metadata part B (class B)
                msg.add(PART_NUMBER, msg.getUnsignedInteger(2));
                msg.add(TYPE_OF_SHIP_AND_CARGO_TYPE, msg.getUnsignedInteger(8));
                msg.add(VENDOR_ID, msg.getHexString(42));
                msg.add(CALL_SIGN, msg.getStringValue(42));
                //msg.add(DIMENSION_OF_SHIP_REFERENCE_FOR_POSITION, msg.getDimensionOfShip30bits());
                msg.add(A_DIMENSION_OF_SHIP_REFERENCE_FOR_POSITION, msg.getA_DimensionOfShip());
                msg.add(B_DIMENSION_OF_SHIP_REFERENCE_FOR_POSITION, msg.getB_DimensionOfShip());
                msg.add(C_DIMENSION_OF_SHIP_REFERENCE_FOR_POSITION, msg.getC_DimensionOfShip());
                msg.add(D_DIMENSION_OF_SHIP_REFERENCE_FOR_POSITION, msg.getD_DimensionOfShip());
                msg.add(TYPE_OF_ELECTRONIC_POSITION_FIXING_DEVICE, msg.getUnsignedInteger(4));
                msg.add(SPARE, msg.getUnsignedInteger(2));

                break;
            case "27": // Long-range automatic identification system broadcast message (class A & B)
                msg.add(POSITION_ACCURACY, msg.getUnsignedInteger(1));
                msg.add(RAIM_FLAG, msg.getUnsignedInteger(1));
                msg.add(NAVIGATIONAL_STATUS, msg.getUnsignedInteger(4));
                msg.add(LONGITUDE, msg.getSignedDecimal(18, 600, 6));
                msg.add(LATITUDE, msg.getSignedDecimal(17, 600, 6));
                msg.add(SOG, msg.getUnsignedInteger(6));
                msg.add(COG, msg.getUnsignedInteger(9));
                msg.add(POSITION_LATENCY, msg.getUnsignedInteger(1));
                msg.add(SPARE, msg.getUnsignedInteger(1));

                break;
        }

        return msg;
    }

    public static boolean isMultipartRadioMessage(final String rawLine) {
        return Integer.parseInt(getColumnValue(RAW_LINE_COLUMN.TOTAL_NUMBER_OF_SENTENCES_NEEDED, rawLine)) > 1;
    }

    public static int getPartNumber(final String rawLine) {
        return Integer.parseInt(getColumnValue(RAW_LINE_COLUMN.SENTENCE_NUMBER, rawLine));
    }

    public static void validateChecksum(final String rawLine) {
        final String calculated = calculateChecksum(rawLine);
        final String received = getColumnValue(RAW_LINE_COLUMN.NUMBER_OF_FILL_BITS_AND_CHECK_VALUE, rawLine).split("\\*")[1];

        final String info = "[calculated=" + calculated + ", received=" + received + ", sentence=" + rawLine + "]";

        if (!calculated.equals(received)) {
            LOGGER.warn("Wrong checksum {}", info);
        }
    }

    private static String calculateChecksum(final String rawLine) {
        int sum = 0;
        for (int i = 1; i < rawLine.length(); i++) {
            final char ch = rawLine.charAt(i);
            if (ch == '*') {
                break;
            }
            sum = sum == 0 ? ch : sum ^ ch;
        }

        return String.format("%02X", sum);
    }

    private static String getColumnValue(final RAW_LINE_COLUMN column, final String rawLine) {
        return rawLine.split(",")[column.ordinal()];
    }

    private static String getBinaryMsg(final String rawLine) {
        return Ais6BitConverter.to6Bit(getColumnValue(RAW_LINE_COLUMN.RADIO_MSG, rawLine));
    }

    private static String getMessageClassSuffix(final String binaryMsg) {
        final int msgId = Integer.parseInt(binaryMsg.substring(0, 6), 2);
        final String partStr = getPartIdString(binaryMsg, msgId);
        return msgId + partStr;
    }

    private static String getPartIdString(final String binaryMsg, final int msgId) {
        if (msgId == 24) {
            return Integer.parseInt(binaryMsg.substring(38, 40), 2) == 1 ? "B" : "A";
        }
        return "";
    }

    private static AisRadioMsg.MessageType getMessageType(final String msgClassSuffix) {
        return SUPPORTED_METADATA_CLASS_SUFFIXES.contains(msgClassSuffix) ? AisRadioMsg.MessageType.METADATA : AisRadioMsg.MessageType.POSITION;
    }

    private static AisRadioMsg.MessageClass getMessageClass(final String msgClassSuffix) {
        return CLASS_B_MESSAGE_SUFFIXES.contains(msgClassSuffix) ? AisRadioMsg.MessageClass.CLASS_B : AisRadioMsg.MessageClass.CLASS_A;
    }
}
