package fi.livi.digitraffic.meri.dto.mqtt;

import com.fasterxml.jackson.annotation.JsonInclude;

import fi.livi.digitraffic.common.util.MathUtil;
import fi.livi.digitraffic.meri.dto.ais.external.AISMessage;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MqttVesselLocationMessageV2 {
    public final long time;

    public final double sog;
    public final double cog;
    public final int navStat;
    public final int rot;
    public final boolean posAcc;
    public final boolean raim;
    public final int heading;

    public final double lon;
    public final double lat;

    public MqttVesselLocationMessageV2(final AISMessage ais) {
        this.time = ais.attributes.timestampExternal / 1000;

        this.sog = ais.attributes.sog;
        this.cog = ais.attributes.cog;
        this.navStat = ais.attributes.navStat;
        this.rot = ais.attributes.rot;
        this.posAcc = ais.attributes.posAcc == 1;
        this.raim = ais.attributes.raim == 1;
        this.heading = ais.attributes.heading;

        this.lon = MathUtil.roundToScale(ais.geometry.x, 6);
        this.lat = MathUtil.roundToScale(ais.geometry.y, 6);
    }
}
