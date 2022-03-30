package fi.livi.digitraffic.meri.mqtt;

import com.fasterxml.jackson.annotation.JsonInclude;
import fi.livi.digitraffic.meri.model.sse.SseFeature;
import fi.livi.digitraffic.meri.model.sse.SseProperties;

import static fi.livi.digitraffic.meri.util.MqttUtil.getEnum;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MqttSseMessageV2 {
    public final String seaState;
    public final String trend;
    public final Integer windWaveDir;
    public final String confidence;
    public final Double heelAngle;
    public final String lightStatus;
    public final int temperature;

    public MqttSseMessageV2(final SseFeature feature) {
        final SseProperties p = feature.getProperties();
        this.seaState = getEnum(p.getSeaState());
        this.trend = getEnum(p.getTrend());
        this.windWaveDir = p.getWindWaveDir();
        this.confidence = getEnum(p.getConfidence());
        this.heelAngle = p.getHeelAngle() == null ? null : p.getHeelAngle().doubleValue();
        this.lightStatus = getEnum(p.getLightStatus());
        this.temperature = p.getTemperature();
    }
}
