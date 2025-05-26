package fi.livi.digitraffic.meri.dto.winternavigation.v1;

import java.time.Instant;

import io.swagger.v3.oas.annotations.media.Schema;

public class WinterNavigationShipActivityV1 {

    @Schema(description = "('FRI','FREE') = 'Moving freely'; " +
                              "('STL','STOP') = 'Stopped'; " +
                              "('VLG','WAIT') = 'Waiting for icebreaker assistance'; " +
                              "('HMN','PORT') = 'In port'; " +
                              "('LED')        = 'Being led in a convoy'; " +
                              "('BOG','TOW')  = 'Being towed'; " +
                              "('VLH','WAIP') = 'Waiting for IB in port'; " +
                              "('OVE','MON')  = 'Monitored'; " +
                              "('DIR')        = 'Received waypoints'; " +
                              "('NOT')        = 'Not active'; " +
                              "('FFL','MOVE') = 'Moving'; " +
                              "('LOK','LOC')  = 'Icebreaking locally'; " +
                              "('ASS')        = 'Assisting ships'; " +
                              "('OUT')        = 'Out of operation'; " +
                              "('PLA')        = 'Planned assistance'; " +
                              "('CCH')        = 'Planned crew change'; " +
                              "('BUN')        = 'Planned bunkering'")
    public final String activityType;

    @Schema(description = "See \"activityType\"'")
    public final String activityText;

    @Schema(description = "User-given comment about the activity")
    public final String activityComment;

    @Schema(description = "When the activity was started; the start of the validity period of the activity")
    public final Instant beginTime;

    @Schema(description = "When activity was ended (available only when querying past events); the end of the validity period of the activity")
    public final Instant endTime;

    @Schema(description = "When the begintime was entered into the source system")
    public final Instant timestampBegin;

    @Schema(description = "When the endtime was entered into the source system")
    public final Instant timestampEnd;

    @Schema(description = "When an erroneous activity was canceled (if ever)")
    public final Instant timestampCanceled;

    @Schema(description = "Key of icebreaker that is giving assistance (or supervising)")
    public final String operatingIcebreakerPK;

    @Schema(description = "Name of icebreaker that is giving assistance (or supervising)")
    public final String operatingIcebreakerName;

    @Schema(description = "For icebreakers, key of ship that is being assisted (or supervised)")
    public final String operatedVesselPK;

    @Schema(description = "For icebreakers, name of ship that is being assisted (or supervised)")
    public final String operatedVesselName;

    @Schema(description = "Position in icebreaker convoy, if any (with 0 reserved for a towed ship); results should be given in this order")
    public final Integer convoyOrder;

    public WinterNavigationShipActivityV1(final String activityType, final String activityText, final String activityComment, final Instant beginTime, final Instant endTime,
                                          final Instant timestampBegin, final Instant timestampEnd, final Instant timestampCanceled, final String operatingIcebreakerPK,
                                          final String operatingIcebreakerName, final String operatedVesselPK, final String operatedVesselName, final Integer convoyOrder) {
        this.activityType = activityType;
        this.activityText = activityText;
        this.activityComment = activityComment;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.timestampBegin = timestampBegin;
        this.timestampEnd = timestampEnd;
        this.timestampCanceled = timestampCanceled;
        this.operatingIcebreakerPK = operatingIcebreakerPK;
        this.operatingIcebreakerName = operatingIcebreakerName;
        this.operatedVesselPK = operatedVesselPK;
        this.operatedVesselName = operatedVesselName;
        this.convoyOrder = convoyOrder;
    }
}
