package fi.livi.digitraffic.meri.service.winternavigation.dto;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShipActivityDto {

    public final String activityType;

    public final String activityText;

    public final String comment;

    public final ZonedDateTime beginTime;

    public final ZonedDateTime endTime;

    public final ZonedDateTime timestampBegin;

    public final ZonedDateTime timestampEnd;

    public final ZonedDateTime timestampCanceled;

    public final String operatingIcebreakerPK;

    public final String operatingIcebreakerName;

    public final String operatedVesselPK;

    public final String operatedVesselName;

    public final Integer convoyOrder;

    public ShipActivityDto(@JsonProperty("activityType") final String activityType,
                           @JsonProperty("activityText") final String activityText,
                           @JsonProperty("comment") final String comment,
                           @JsonProperty("begintime") final ZonedDateTime beginTime,
                           @JsonProperty("endtime") final ZonedDateTime endTime,
                           @JsonProperty("timestamp_begin") final ZonedDateTime timestampBegin,
                           @JsonProperty("timestamp_end") final ZonedDateTime timestampEnd,
                           @JsonProperty("timestamp_canceled") final ZonedDateTime timestampCanceled,
                           @JsonProperty("operating_ib_pk") final String operatingIcebreakerPK,
                           @JsonProperty("operating_ib_name") final String operatingIcebreakerName,
                           @JsonProperty("operated_vessel_pk") final String operatedVesselPK,
                           @JsonProperty("operated_vessel_name") final String operatedVesselName,
                           @JsonProperty("convoy_order") final Integer convoyOrder) {
        this.activityType = activityType;
        this.activityText = activityText;
        this.comment = comment;
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
