package fi.livi.digitraffic.meri.mqtt;

import com.fasterxml.jackson.annotation.JsonInclude;
import fi.livi.digitraffic.meri.model.ais.AISMessage;

import static fi.livi.digitraffic.meri.util.MqttUtil.roundToScale;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MqttVesselLocationMessageV2 {
    public final long timestamp;

    public final double sog;
    public final double cog;
    public final int navStat;
    public final int rot;
    public final boolean posAcc;
    public final boolean raim;
    public final int heading;

    public final double x;
    public final double y;

    public MqttVesselLocationMessageV2(final AISMessage ais) {
        this.timestamp = ais.attributes.timestampExternal;

        this.sog = ais.attributes.sog;
        this.cog = ais.attributes.cog;
        this.navStat = ais.attributes.navStat;
        this.rot = ais.attributes.rot;
        this.posAcc = ais.attributes.posAcc == 1;
        this.raim = ais.attributes.raim == 1;
        this.heading = ais.attributes.heading;

        this.x = roundToScale(ais.geometry.x, 6);
        this.y = roundToScale(ais.geometry.y, 6);
    }
}
