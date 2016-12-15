package fi.livi.digitraffic.meri.domain.portnet.VesselDetails;

import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate
public class VesselConstruction {

    @Id
    private Long vesselId;

    private Integer vesselTypeCode;

    private String vesselTypeName;

    private String iceClassCode;

    private ZonedDateTime iceClassIssueDate;

    private String iceClassIssuePlace;

    private ZonedDateTime iceClassEndDate;

    private String classificationSociety;

    private Boolean doubleBottom;

    private Boolean inertGasSystem;

    private Boolean ballastTank;

    public Long getVesselId() {
        return vesselId;
    }

    public void setVesselId(Long vesselId) {
        this.vesselId = vesselId;
    }

    public Integer getVesselTypeCode() {
        return vesselTypeCode;
    }

    public void setVesselTypeCode(Integer vesselTypeCode) {
        this.vesselTypeCode = vesselTypeCode;
    }

    public String getVesselTypeName() {
        return vesselTypeName;
    }

    public void setVesselTypeName(String vesselTypeName) {
        this.vesselTypeName = vesselTypeName;
    }

    public String getIceClassCode() {
        return iceClassCode;
    }

    public void setIceClassCode(String iceClassCode) {
        this.iceClassCode = iceClassCode;
    }

    public ZonedDateTime getIceClassIssueDate() {
        return iceClassIssueDate;
    }

    public void setIceClassIssueDate(ZonedDateTime iceClassIssueDate) {
        this.iceClassIssueDate = iceClassIssueDate;
    }

    public String getIceClassIssuePlace() {
        return iceClassIssuePlace;
    }

    public void setIceClassIssuePlace(String iceClassIssuePlace) {
        this.iceClassIssuePlace = iceClassIssuePlace;
    }

    public ZonedDateTime getIceClassEndDate() {
        return iceClassEndDate;
    }

    public void setIceClassEndDate(ZonedDateTime iceClassEndDate) {
        this.iceClassEndDate = iceClassEndDate;
    }

    public String getClassificationSociety() {
        return classificationSociety;
    }

    public void setClassificationSociety(String classificationSociety) {
        this.classificationSociety = classificationSociety;
    }

    public Boolean getDoubleBottom() {
        return doubleBottom;
    }

    public void setDoubleBottom(Boolean doubleBottom) {
        this.doubleBottom = doubleBottom;
    }

    public Boolean getInertGasSystem() {
        return inertGasSystem;
    }

    public void setInertGasSystem(Boolean inertGasSystem) {
        this.inertGasSystem = inertGasSystem;
    }

    public Boolean getBallastTank() {
        return ballastTank;
    }

    public void setBallastTank(Boolean ballastTank) {
        this.ballastTank = ballastTank;
    }
}
