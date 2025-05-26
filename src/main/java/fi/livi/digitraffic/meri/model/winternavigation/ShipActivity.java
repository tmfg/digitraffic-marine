package fi.livi.digitraffic.meri.model.winternavigation;

import java.time.Instant;

import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity
@DynamicUpdate
public class ShipActivity {

    @Id
    @SequenceGenerator(name = "SEQ_SHIP_ACTIVITY", sequenceName = "SEQ_SHIP_ACTIVITY", allocationSize = 1)
    @GeneratedValue(generator = "SEQ_SHIP_ACTIVITY")
    private Long id;

    @Column(name = "vessel_pk")
    private String vesselPK;

    private Integer orderNumber;

    private String activityType;

    private String activityText;

    private String activityComment;

    private Instant beginTime;

    private Instant endTime;

    private Instant timestampBegin;

    private Instant timestampEnd;

    private Instant timestampCanceled;

    @Column(name = "operating_icebreaker_pk")
    private String operatingIcebreakerPK;

    private String operatingIcebreakerName;

    @Column(name = "operated_vessel_pk")
    private String operatedVesselPK;

    private String operatedVesselName;

    private Integer convoyOrder;

    public Long getId() {
        return id;
    }

    public String getVesselPK() {
        return vesselPK;
    }

    public void setVesselPK(final String vesselPK) {
        this.vesselPK = vesselPK;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(final Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(final String activityType) {
        this.activityType = activityType;
    }

    public String getActivityText() {
        return activityText;
    }

    public void setActivityText(final String activityText) {
        this.activityText = activityText;
    }

    public String getActivityComment() {
        return activityComment;
    }

    public void setActivityComment(final String activityComment) {
        this.activityComment = activityComment;
    }

    public Instant getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(final Instant beginTime) {
        this.beginTime = beginTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(final Instant endTime) {
        this.endTime = endTime;
    }

    public Instant getTimestampBegin() {
        return timestampBegin;
    }

    public void setTimestampBegin(final Instant timestampBegin) {
        this.timestampBegin = timestampBegin;
    }

    public Instant getTimestampEnd() {
        return timestampEnd;
    }

    public void setTimestampEnd(final Instant timestampEnd) {
        this.timestampEnd = timestampEnd;
    }

    public Instant getTimestampCanceled() {
        return timestampCanceled;
    }

    public void setTimestampCanceled(final Instant timestampCanceled) {
        this.timestampCanceled = timestampCanceled;
    }

    public String getOperatingIcebreakerPK() {
        return operatingIcebreakerPK;
    }

    public void setOperatingIcebreakerPK(final String operatingIcebreakerPK) {
        this.operatingIcebreakerPK = operatingIcebreakerPK;
    }

    public String getOperatingIcebreakerName() {
        return operatingIcebreakerName;
    }

    public void setOperatingIcebreakerName(final String operatingIcebreakerName) {
        this.operatingIcebreakerName = operatingIcebreakerName;
    }

    public String getOperatedVesselPK() {
        return operatedVesselPK;
    }

    public void setOperatedVesselPK(final String operatedVesselPK) {
        this.operatedVesselPK = operatedVesselPK;
    }

    public String getOperatedVesselName() {
        return operatedVesselName;
    }

    public void setOperatedVesselName(final String operatedVesselName) {
        this.operatedVesselName = operatedVesselName;
    }

    public Integer getConvoyOrder() {
        return convoyOrder;
    }

    public void setConvoyOrder(final Integer convoyOrder) {
        this.convoyOrder = convoyOrder;
    }
}
