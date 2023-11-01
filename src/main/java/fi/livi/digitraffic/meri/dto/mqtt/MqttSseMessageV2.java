package fi.livi.digitraffic.meri.dto.mqtt;

import com.fasterxml.jackson.annotation.JsonInclude;

import fi.livi.digitraffic.common.util.ObjectUtil;
import fi.livi.digitraffic.meri.dto.sse.v1.SseFeatureV1;
import fi.livi.digitraffic.meri.dto.sse.v1.SsePropertiesV1;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MqttSseMessageV2 {
    public final long timestamp;

    public final String seaState;
    public final String trend;
    public final Integer windWaveDir;
    public final String confidence;
    public final Double heelAngle;
    public final String lightStatus;
    public final int temperature;

    public MqttSseMessageV2(final SseFeatureV1 feature) {
        final SsePropertiesV1 p = feature.getProperties();

        this.timestamp = p.getLastUpdate().getEpochSecond();
        this.seaState = ObjectUtil.getEnumName(p.getSeaState());
        this.trend = ObjectUtil.getEnumName(p.getTrend());
        this.windWaveDir = p.getWindWaveDir();
        this.confidence = ObjectUtil.getEnumName(p.getConfidence());
        this.heelAngle = p.getHeelAngle() == null ? null : p.getHeelAngle().doubleValue();
        this.lightStatus = ObjectUtil.getEnumName(p.getLightStatus());
        this.temperature = p.getTemperature();
    }
}
