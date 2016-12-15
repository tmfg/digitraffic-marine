package fi.livi.digitraffic.meri.domain.portnet.VesselDetails;

import java.math.BigInteger;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicUpdate;

import fi.livi.digitraffic.meri.portnet.vesseldetails.xsd.VesselDetails;
import fi.livi.digitraffic.util.TypeUtil;

@Entity
@DynamicUpdate
public class VesselConstruction {

    @Id
    private Long vesselId;

    private Integer vesselTypeCode;

    private String vesselTypeName;

    private String iceClassCode;

    private Timestamp iceClassIssueDate;

    private String iceClassIssuePlace;

    private Timestamp iceClassEndDate;

    private String classificationSociety;

    private Boolean doubleBottom;

    private Boolean inertGasSystem;

    private Boolean ballastTank;

    public void setAll(BigInteger vesselId, VesselDetails.ConstructionData cd) {
        this.vesselId = vesselId.longValue();
        this.vesselTypeCode = TypeUtil.getInteger(cd.getVesselTypeCode());
        this.vesselTypeName = cd.getVesselTypeName();
        this.iceClassCode = TypeUtil.getEnum(cd.getIceClassCode());
        this.iceClassIssueDate = TypeUtil.getTimestamp(cd.getIceClassIssueDate());
        this.iceClassIssuePlace = cd.getIceClassIssuePlace();
        this.iceClassEndDate = TypeUtil.getTimestamp(cd.getIceClassEndDate());
        this.classificationSociety = cd.getClassificationSociety();
        this.doubleBottom = cd.isDoubleBottom();
        this.inertGasSystem = cd.isInertGasSystem();
        this.ballastTank = cd.isBallastTank();
    }

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

    public Timestamp getIceClassIssueDate() {
        return iceClassIssueDate;
    }

    public void setIceClassIssueDate(Timestamp iceClassIssueDate) {
        this.iceClassIssueDate = iceClassIssueDate;
    }

    public String getIceClassIssuePlace() {
        return iceClassIssuePlace;
    }

    public void setIceClassIssuePlace(String iceClassIssuePlace) {
        this.iceClassIssuePlace = iceClassIssuePlace;
    }

    public Timestamp getIceClassEndDate() {
        return iceClassEndDate;
    }

    public void setIceClassEndDate(Timestamp iceClassEndDate) {
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
