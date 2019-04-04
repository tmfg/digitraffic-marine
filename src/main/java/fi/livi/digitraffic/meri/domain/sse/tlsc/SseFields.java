
package fi.livi.digitraffic.meri.domain.sse.tlsc;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;

import fi.livi.digitraffic.meri.external.tlsc.sse.SSEFields;
import fi.livi.digitraffic.meri.util.StringUtil;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "lastUpdate",
    "seaState",
    "trend",
    "windWaveDir",
    "confidence"
})
public class SseFields {

    private ZonedDateTime lastUpdate;
    private SseFields.SeaState seaState;
    private SseFields.Trend trend;
    private Integer windWaveDir;
    private SseFields.Confidence confidence;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public SseFields() {
    }

    public SseFields(final ZonedDateTime lastUpdate, final SSEFields.SeaState seaState, final SSEFields.Trend trend, final Integer windWaveDir,
                     final SSEFields.Confidence confidence, Map<String, Object> additionalProperties) {
        this(lastUpdate, seaState, trend, windWaveDir, confidence);
        this.additionalProperties = additionalProperties;
    }

    @JsonCreator
    public SseFields(final ZonedDateTime lastUpdate, final SSEFields.SeaState seaState, final SSEFields.Trend trend, final Integer windWaveDir,
                     final SSEFields.Confidence confidence) {

        this.lastUpdate = lastUpdate;
        this.seaState = SeaState.fromValue(seaState.value());
        this.trend = Trend.fromValue(trend.value());
        this.windWaveDir = windWaveDir;
        this.confidence = Confidence.fromValue(confidence.value());
    }

    public ZonedDateTime getLastUpdate() {
        return lastUpdate;
    }
    public void setLastUpdate(final ZonedDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    public SseFields.SeaState getSeaState() {
        return seaState;
    }
    public void setSeaState(final SseFields.SeaState seaState) {
        this.seaState = seaState;
    }
    public SseFields.Trend getTrend() {
        return trend;
    }
    public void setTrend(final SseFields.Trend trend) {
        this.trend = trend;
    }
    public Integer getWindWaveDir() {
        return windWaveDir;
    }
    public void setWindWaveDir(final Integer windWaveDir) {
        this.windWaveDir = windWaveDir;
    }
    public SseFields.Confidence getConfidence() {
        return confidence;
    }
    public void setConfidence(final SseFields.Confidence confidence) {
        this.confidence = confidence;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(final String name, final Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return StringUtil.toJsonString(this);
    }

    public enum Confidence {

        POOR("POOR"),
        MODERATE("MODERATE"),
        GOOD("GOOD");
        private final String value;
        private final static Map<String, SseFields.Confidence> CONSTANTS = new HashMap<String, SseFields.Confidence>();

        static {
            for (SseFields.Confidence c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Confidence(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static SseFields.Confidence fromValue(String value) {
            SseFields.Confidence constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum SeaState {

        CALM("CALM"),
        LIGHT("LIGHT"),
        MODERATE("MODERATE"),
        BREEZE("BREEZE"),
        GALE("GALE"),
        STORM("STORM");
        private final String value;
        private final static Map<String, SseFields.SeaState> CONSTANTS = new HashMap<String, SseFields.SeaState>();

        static {
            for (SseFields.SeaState c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private SeaState(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static SseFields.SeaState fromValue(String value) {
            SseFields.SeaState constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum Trend {

        UNKNOWN("Unknown"),
        NO_CHANGE("NoChange"),
        ASCENDING("Ascending"),
        DESCENDING("Descending");
        private final String value;
        private final static Map<String, SseFields.Trend> CONSTANTS = new HashMap<String, SseFields.Trend>();

        static {
            for (SseFields.Trend c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Trend(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static SseFields.Trend fromValue(String value) {
            SseFields.Trend constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
