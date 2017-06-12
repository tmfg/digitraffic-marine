package fi.livi.digitraffic.meri.domain.winternavigation;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
public class ShipPlannedActivity {

    @Id
    @GenericGenerator(name = "SEQ_SHIP_PLANNED_ACTIVITY", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
                      parameters = @Parameter(name = "sequence_name", value = "SEQ_SHIP_PLANNED_ACTIVITY"))
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

    private Timestamp planTimestamp;

    private Timestamp planTimestampRealized;

    private Timestamp planTimestampCanceled;

    public Long getId() {
        return id;
    }

    public String getVesselPK() {
        return vesselPK;
    }

    public void setVesselPK(String vesselPK) {
        this.vesselPK = vesselPK;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
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

    public String getPlannedVesselPK() {
        return plannedVesselPK;
    }

    public void setPlannedVesselPK(String plannedVesselPK) {
        this.plannedVesselPK = plannedVesselPK;
    }

    public String getPlanningVesselPK() {
        return planningVesselPK;
    }

    public void setPlanningVesselPK(String planningVesselPK) {
        this.planningVesselPK = planningVesselPK;
    }

    public Integer getOrdering() {
        return ordering;
    }

    public void setOrdering(Integer ordering) {
        this.ordering = ordering;
    }

    public String getPlannedWhen() {
        return plannedWhen;
    }

    public void setPlannedWhen(String plannedWhen) {
        this.plannedWhen = plannedWhen;
    }

    public String getPlannedWhere() {
        return plannedWhere;
    }

    public void setPlannedWhere(String plannedWhere) {
        this.plannedWhere = plannedWhere;
    }

    public String getPlanComment() {
        return planComment;
    }

    public void setPlanComment(String planComment) {
        this.planComment = planComment;
    }

    public Timestamp getPlanTimestamp() {
        return planTimestamp;
    }

    public void setPlanTimestamp(Timestamp planTimestamp) {
        this.planTimestamp = planTimestamp;
    }

    public Timestamp getPlanTimestampRealized() {
        return planTimestampRealized;
    }

    public void setPlanTimestampRealized(Timestamp planTimestampRealized) {
        this.planTimestampRealized = planTimestampRealized;
    }

    public Timestamp getPlanTimestampCanceled() {
        return planTimestampCanceled;
    }

    public void setPlanTimestampCanceled(Timestamp planTimestampCanceled) {
        this.planTimestampCanceled = planTimestampCanceled;
    }
}
