package fi.livi.digitraffic.meri.model.winternavigation;

import java.time.ZonedDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "ShipActivity")
public class ShipActivityProperty {

    @ApiModelProperty(value = "('FRI','FREE') = 'Moving freely'; " +
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

    @ApiModelProperty(value = "See \"activityType\"'")
    public final String activityText;

    @ApiModelProperty(value = "User-given comment about the activity")
    public final String activityComment;

    @ApiModelProperty(value = "When the activity was started; the start of the validity period of the activity")
    public final ZonedDateTime beginTime;

    @ApiModelProperty(value = "When activity was ended (available only when querying past events); the end of the validity period of the activity")
    public final ZonedDateTime endTime;

    @ApiModelProperty(value = "When the begintime was entered into the source system")
    public final ZonedDateTime timestampBegin;

    @ApiModelProperty(value = "When the endtime was entered into the source system")
    public final ZonedDateTime timestampEnd;

    @ApiModelProperty(value = "When an erroneous activity was canceled (if ever)")
    public final ZonedDateTime timestampCanceled;

    @ApiModelProperty(value = "Key of icebreaker that is giving assistance (or supervising)")
    public final String operatingIcebreakerPK;

    @ApiModelProperty(value = "Name of icebreaker that is giving assistance (or supervising)")
    public final String operatingIcebreakerName;

    @ApiModelProperty(value = "For icebreakers, key of ship that is being assisted (or supervised)")
    public final String operatedVesselPK;

    @ApiModelProperty(value = "For icebreakers, name of ship that is being assisted (or supervised)")
    public final String operatedVesselName;

    @ApiModelProperty(value = "Position in icebreaker convoy, if any (with 0 reserved for a towed ship); results should be given in this order")
    public final Integer convoyOrder;

    public ShipActivityProperty(String activityType, String activityText, String activityComment, ZonedDateTime beginTime, ZonedDateTime endTime,
                                ZonedDateTime timestampBegin, ZonedDateTime timestampEnd, ZonedDateTime timestampCanceled, String operatingIcebreakerPK,
                                String operatingIcebreakerName, String operatedVesselPK, String operatedVesselName, Integer convoyOrder) {
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
