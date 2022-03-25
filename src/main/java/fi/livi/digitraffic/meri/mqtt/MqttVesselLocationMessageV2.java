package fi.livi.digitraffic.meri.mqtt;

import com.fasterxml.jackson.annotation.JsonInclude;
import fi.livi.digitraffic.meri.model.ais.AISMessage;

import static fi.livi.digitraffic.meri.util.MqttUtil.roundToScale;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MqttVesselLocationMessageV2 {
    public final long timestampExt;

    public final double x;
    public final double y;

    public MqttVesselLocationMessageV2(final AISMessage ais) {
        this.timestampExt = ais.attributes.timestampExternal;

        this.x = roundToScale(ais.geometry.x, 6);
        this.y = roundToScale(ais.geometry.y, 6);
    }
}
