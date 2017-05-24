package fi.livi.digitraffic.meri.domain.winternavigation;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class ShipActivity {

    @EmbeddedId
    private ShipActivityPK shipActivityPK;

    private String activityType;

    private String activityText;

    private String activityComment;

    private Timestamp beginTime;

    private Timestamp endTime;

    private Timestamp timestampBegin;

    private Timestamp timestampEnd;

    private Timestamp timestampCanceled;

    @Column(name = "operating_icebreaker_pk")
    private String operatingIcebreakerPK;

    private String operatingIcebreakerName;

    @Column(name = "operated_vessel_pk")
    private String operatedVesselPK;

    private String operatedVesselName;

    private Integer convoyOrder;

    public ShipActivityPK getShipActivityPK() {
        return shipActivityPK;
    }

    public void setShipActivityPK(ShipActivityPK shipActivityPK) {
        this.shipActivityPK = shipActivityPK;
    }

    public String getVesselPK() {
        return this.shipActivityPK != null ? this.shipActivityPK.getVesselPK() : null;
    }

    public Integer getOrderNumber() {
        return this.shipActivityPK != null ? this.shipActivityPK.getOrderNumber() : null;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getActivityText() {
        return activityText;
    }

    public void setActivityText(String activityText) {
        this.activityText = activityText;
    }

    public String getActivityComment() {
        return activityComment;
    }

    public void setActivityComment(String activityComment) {
        this.activityComment = activityComment;
    }

    public Timestamp getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Timestamp beginTime) {
        this.beginTime = beginTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Timestamp getTimestampBegin() {
        return timestampBegin;
    }

    public void setTimestampBegin(Timestamp timestampBegin) {
        this.timestampBegin = timestampBegin;
    }

    public Timestamp getTimestampEnd() {
        return timestampEnd;
    }

    public void setTimestampEnd(Timestamp timestampEnd) {
        this.timestampEnd = timestampEnd;
    }

    public Timestamp getTimestampCanceled() {
        return timestampCanceled;
    }

    public void setTimestampCanceled(Timestamp timestampCanceled) {
        this.timestampCanceled = timestampCanceled;
    }

    public String getOperatingIcebreakerPK() {
        return operatingIcebreakerPK;
    }

    public void setOperatingIcebreakerPK(String operatingIcebreakerPK) {
        this.operatingIcebreakerPK = operatingIcebreakerPK;
    }

    public String getOperatingIcebreakerName() {
        return operatingIcebreakerName;
    }

    public void setOperatingIcebreakerName(String operatingIcebreakerName) {
        this.operatingIcebreakerName = operatingIcebreakerName;
    }

    public String getOperatedVesselPK() {
        return operatedVesselPK;
    }

    public void setOperatedVesselPK(String operatedVesselPK) {
        this.operatedVesselPK = operatedVesselPK;
    }

    public String getOperatedVesselName() {
        return operatedVesselName;
    }

    public void setOperatedVesselName(String operatedVesselName) {
        this.operatedVesselName = operatedVesselName;
    }

    public Integer getConvoyOrder() {
        return convoyOrder;
    }

    public void setConvoyOrder(Integer convoyOrder) {
        this.convoyOrder = convoyOrder;
    }
}
