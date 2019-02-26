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
package fi.livi.digitraffic.meri.controller.ais;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
//import java.util.function.Function;
//import java.util.stream.Collectors;

import static fi.livi.digitraffic.meri.controller.ais.AisRadioMsgParameters.*;

public class AisRadioMsg {
    public enum MessageType {POSITION, METADATA};
    public enum MessageClass {CLASS_A, CLASS_B};

    // maintains insertion order NOTE! Is this order important???
    private Map<String, Object> parameters = new LinkedHashMap<>();
    private List<String> rawDataParts;
    private String binaryMsg;
    private int readOffset = 0;

    private final long time;
    private final MessageType messageType;
    private final MessageClass messageClass;
    private boolean mmsiAllowed = false;

    public AisRadioMsg(String binaryMsg, List<String> rawDataParts, MessageType messageType, MessageClass messageClass) {
        this.rawDataParts = rawDataParts;
        this.binaryMsg = binaryMsg;

        this.time = Instant.now().toEpochMilli();
        this.messageType = messageType;
        this.messageClass = messageClass;

        add(MESSAGE_ID, getUnsignedInteger(6));
        add(REPEAT_INDICATOR, getUnsignedInteger(2));
        add(USER_ID, getUnsignedInteger(30));
    }

    protected final <T> void add(String name, T value) {
        parameters.put(name, value);
    }

    public final long getTimestamp() {
        return time;
    }

    public final boolean containsParam(String name) {
        return parameters.containsKey(name);
    }

    public final BigDecimal getDecimalParam(String name) {
        return (BigDecimal) parameters.get(name);
    }

    public final int getIntParam(String name) {
        return (int) parameters.get(name);
    }

    public final String getStringParam(String name) {
        return (String)parameters.get(name);
    }

    public double getDoubleParam(String name) {
        Object param = parameters.get(name);

        if (param instanceof BigDecimal) {
            return ((BigDecimal)param).doubleValue();
        } else if (param instanceof Integer) {
            return (double)(1.0 * (Integer)param);
        }

        throw new NumberFormatException("Failed to convert double value: " + param);
    }

    public final int getUserId() {
        return getIntParam(USER_ID);
    }

    public final boolean isMmsiAllowed() {
        return mmsiAllowed;
    }

    public final void setMmsiAllowed(boolean mmsiAllowed) {
        this.mmsiAllowed = mmsiAllowed;
    }

    public final MessageClass getMessageClass() {
        return messageClass;
    }

    public final MessageType getMessageType() {
        return messageType;
    }

    protected Set<Map.Entry<String, Object>> getParameterEntrySet() {
        return parameters.entrySet();
    }

    private String getNextSubstring(int size) {
        String ret = binaryMsg.substring(readOffset, readOffset + size);
        readOffset += size;
        return ret;
    }

    protected int getUnsignedInteger(int size) {
        return Integer.parseInt(getNextSubstring(size), 2);
    }

    protected int getSignedInteger(int size) {
        int UPPER_LIMIT = 2 << (size - 2);
        int ret = getUnsignedInteger(size);
        return ret >= UPPER_LIMIT ? ret - (UPPER_LIMIT << 1) : ret;
    }

    protected BigDecimal getUnsignedDecimal(int size, long divisor, int scale) {
        return getDecimal(getUnsignedInteger(size), divisor, scale);
    }

    protected BigDecimal getSignedDecimal(int size, long divisor, int scale) {
        return getDecimal(getSignedInteger(size), divisor, scale);
    }

    private BigDecimal getDecimal(int intVal, long divisor, int scale) {
        return BigDecimal.valueOf(intVal).divide(BigDecimal.valueOf(divisor), scale, BigDecimal.ROUND_HALF_UP);
    }

    protected String getStringValue(int size) {
        return Ais6BitConverter.to6BitEncodedString(getNextSubstring(size)).trim();
    }

    protected String getHexString(int size) {
        return Long.toHexString(Long.parseLong(getNextSubstring(size), 2)).toUpperCase(); // long for the 42 bits of Vendor ID
    }

    protected String getDimensionOfShip30bits() {
        return "A=" + getUnsignedInteger(9) +
            ",B=" + getUnsignedInteger(9) +
            ",C=" + getUnsignedInteger(6) +
            ",D=" + getUnsignedInteger(6);
    }

    protected int getA_DimensionOfShip() {
        return getUnsignedInteger(9);
    }

    protected int getB_DimensionOfShip() {
        return getUnsignedInteger(9);
    }

    protected int getC_DimensionOfShip() {
        return getUnsignedInteger(6);
    }

    protected int getD_DimensionOfShip() {
        return getUnsignedInteger(6);
    }

    protected String getEta20bits() {
        return getEtaPart(4) + getEtaPart(5) + getEtaPart(5) + getEtaPart(6);
    }

    private String getEtaPart(int size) {
        return String.format("%02d", getUnsignedInteger(size));
    }

    /**
    @Override
    public String toString() {
        return toRawAndParsedDataString();
    }

    public final String toRawAndParsedDataString() {
        return "{\"data\":{\"raw\":" + toRawDataString() + ",\"parsed\":\"" + toParsedDataString() + "\"}}";
    }

    private String toRawDataString() {
        return "[\"" + String.join("\",\"", rawDataParts) + "\"]";
    }

    public final String toParsedDataString() {
        return toDataString(v -> getParsedEntry(v.getKey(), v.getValue()));
    }

    public final boolean isPositionMsg() {
        return AisRadioMsgParser.isPositionMessage(binaryMsg);
    }

    private String toDataString(Function<Map.Entry<String, Object>, String> mapper) {
        return getParameterEntrySet().stream().map(mapper).collect(Collectors.joining("|"));
    }

    private String getParsedEntry(String key, Object value) {
        return key + "§" + value;
    }
    */
}
