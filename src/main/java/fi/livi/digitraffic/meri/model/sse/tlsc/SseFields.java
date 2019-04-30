
package fi.livi.digitraffic.meri.model.sse.tlsc;

import static fi.livi.digitraffic.meri.model.sse.SseProperties.Confidence;
import static fi.livi.digitraffic.meri.model.sse.SseProperties.SeaState;
import static fi.livi.digitraffic.meri.model.sse.SseProperties.Trend;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

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
    private SeaState seaState;
    private Trend trend;
    private Integer windWaveDir;
    private Confidence confidence;
    private Map<String, Object> additionalProperties = new HashMap<>();

    public SseFields(final ZonedDateTime lastUpdate, final SeaState seaState, final Trend trend, final Integer windWaveDir,
                     final Confidence confidence, Map<String, Object> additionalProperties) {
        this(lastUpdate, seaState, trend, windWaveDir, confidence);
        this.additionalProperties = additionalProperties;
    }

    @JsonCreator
    public SseFields(final ZonedDateTime lastUpdate, final SeaState seaState, final Trend trend, final Integer windWaveDir,
                     final Confidence confidence) {

        this.lastUpdate = lastUpdate;
        this.seaState = seaState;
        this.trend = trend;
        this.windWaveDir = windWaveDir;
        this.confidence = confidence;
    }

    public ZonedDateTime getLastUpdate() {
        return lastUpdate;
    }
    public void setLastUpdate(final ZonedDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    public SeaState getSeaState() {
        return seaState;
    }
    public void setSeaState(final SeaState seaState) {
        this.seaState = seaState;
    }
    public Trend getTrend() {
        return trend;
    }
    public void setTrend(final Trend trend) {
        this.trend = trend;
    }
    public Integer getWindWaveDir() {
        return windWaveDir;
    }
    public void setWindWaveDir(final Integer windWaveDir) {
        this.windWaveDir = windWaveDir;
    }
    public Confidence getConfidence() {
        return confidence;
    }
    public void setConfidence(final Confidence confidence) {
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
}
