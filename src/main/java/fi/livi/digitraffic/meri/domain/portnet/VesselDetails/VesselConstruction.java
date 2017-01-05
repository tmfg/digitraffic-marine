package fi.livi.digitraffic.meri.domain.portnet.VesselDetails;

import java.math.BigInteger;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.portnet.vesseldetails.xsd.VesselDetails;
import fi.livi.digitraffic.util.TypeUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="Vessel construction", value = "VesselConstruction")
@JsonPropertyOrder({ "vesselId", "vesselTypeCode", "vesselTypeName", "iceClassCode", "iceClassIssueDate", "iceClassIssuePlace",
                     "iceClassEndDate", "classificationSociety", "doubleBottom", "inertGasSystem", "ballastTank" })
@Entity
@DynamicUpdate
public class VesselConstruction {

    @JsonIgnore
    @Id
    private Long vesselId;

    @ApiModelProperty(value = "Ship Vessel Type Code")
    private Integer vesselTypeCode;

    @ApiModelProperty(value = "Ship Vessel Type Name")
    private String vesselTypeName;

    @ApiModelProperty(value = "Ship ice class code")
    private String iceClassCode;

    @ApiModelProperty(value = "Date when ice class endorsement was issued")
    private Timestamp iceClassIssueDate;

    @ApiModelProperty(value = "Place where ice class endorsement was issued")
    private String iceClassIssuePlace;

    @ApiModelProperty(value = "Date when ice class endorsement ends")
    private Timestamp iceClassEndDate;

    @ApiModelProperty(value = "Always null")
    @JsonIgnore
    private String classificationSociety;

    @ApiModelProperty(value = "Tells whether vessel has a double bottom")
    private Boolean doubleBottom;

    @ApiModelProperty(value = "Tells whether vessel has an Inert gas system")
    private Boolean inertGasSystem;

    @ApiModelProperty(value = "Tells whether vessel has a ballast tank")
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

    public Integer getVesselTypeCode() {
        return vesselTypeCode;
    }

    public String getVesselTypeName() {
        return vesselTypeName;
    }

    public String getIceClassCode() {
        return iceClassCode;
    }

    public Timestamp getIceClassIssueDate() {
        return iceClassIssueDate;
    }

    public String getIceClassIssuePlace() {
        return iceClassIssuePlace;
    }

    public Timestamp getIceClassEndDate() {
        return iceClassEndDate;
    }

    public String getClassificationSociety() {
        return classificationSociety;
    }

    public Boolean getDoubleBottom() {
        return doubleBottom;
    }

    public Boolean getInertGasSystem() {
        return inertGasSystem;
    }

    public Boolean getBallastTank() {
        return ballastTank;
    }
}
