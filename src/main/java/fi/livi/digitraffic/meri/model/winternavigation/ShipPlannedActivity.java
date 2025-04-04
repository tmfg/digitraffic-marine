package fi.livi.digitraffic.meri.model.winternavigation;

import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate
public class ShipPlannedActivity {

    @Id
    @SequenceGenerator(name = "SEQ_SHIP_PLANNED_ACTIVITY", sequenceName = "SEQ_SHIP_PLANNED_ACTIVITY", allocationSize = 1)
    @GeneratedValue(generator = "SEQ_SHIP_PLANNED_ACTIVITY")
    private Long id;

    @Column(name = "vessel_pk")
    private String vesselPK;

    private Integer orderNumber;

    private String activityType;

    private String activityText;

    @Column(name = "planned_vessel_pk")
    private String plannedVesselPK;

    @Column(name = "planning_vessel_pk")
    private String planningVesselPK;

    private Integer ordering;

    private String plannedWhen;

    private String plannedWhere;

    private String planComment;

    private ZonedDateTime planTimestamp;

    private ZonedDateTime planTimestampRealized;

    private ZonedDateTime planTimestampCanceled;

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

    public String getPlannedVesselPK() {
        return plannedVesselPK;
    }

    public void setPlannedVesselPK(final String plannedVesselPK) {
        this.plannedVesselPK = plannedVesselPK;
    }

    public String getPlanningVesselPK() {
        return planningVesselPK;
    }

    public void setPlanningVesselPK(final String planningVesselPK) {
        this.planningVesselPK = planningVesselPK;
    }

    public Integer getOrdering() {
        return ordering;
    }

    public void setOrdering(final Integer ordering) {
        this.ordering = ordering;
    }

    public String getPlannedWhen() {
        return plannedWhen;
    }

    public void setPlannedWhen(final String plannedWhen) {
        this.plannedWhen = plannedWhen;
    }

    public String getPlannedWhere() {
        return plannedWhere;
    }

    public void setPlannedWhere(final String plannedWhere) {
        this.plannedWhere = plannedWhere;
    }

    public String getPlanComment() {
        return planComment;
    }

    public void setPlanComment(final String planComment) {
        this.planComment = planComment;
    }

    public ZonedDateTime getPlanTimestamp() {
        return planTimestamp;
    }

    public void setPlanTimestamp(final ZonedDateTime planTimestamp) {
        this.planTimestamp = planTimestamp;
    }

    public ZonedDateTime getPlanTimestampRealized() {
        return planTimestampRealized;
    }

    public void setPlanTimestampRealized(final ZonedDateTime planTimestampRealized) {
        this.planTimestampRealized = planTimestampRealized;
    }

    public ZonedDateTime getPlanTimestampCanceled() {
        return planTimestampCanceled;
    }

    public void setPlanTimestampCanceled(final ZonedDateTime planTimestampCanceled) {
        this.planTimestampCanceled = planTimestampCanceled;
    }
}
