package fi.livi.digitraffic.meri.model.portnet.vesseldetails;

import java.math.BigInteger;
import java.sql.Timestamp;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.portnet.xsd.ConstructionData;
import fi.livi.digitraffic.meri.util.TypeUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;

@Schema(description = "Vessel construction", name = "VesselConstruction")
@JsonPropertyOrder({ "vesselId", "vesselTypeCode", "vesselTypeName", "iceClassCode", "iceClassIssueDate", "iceClassIssuePlace",
                     "iceClassEndDate", "classificationSociety", "doubleBottom", "inertGasSystem", "ballastTank" })
@Entity
@DynamicUpdate
public class VesselConstruction {

    @JsonIgnore
    @Id
    private Long vesselId;

    @JsonIgnore
    @OneToOne(targetEntity = fi.livi.digitraffic.meri.model.portnet.vesseldetails.VesselDetails.class)
    @JoinColumn(name = "vesselId", nullable = false)
    @MapsId
    private fi.livi.digitraffic.meri.model.portnet.vesseldetails.VesselDetails vesselDetails;

    @Schema(description = "Ship Vessel Type Code")
    private Integer vesselTypeCode;

    @Schema(description = "Ship Vessel Type Name")
    private String vesselTypeName;

    @Schema(description = "Ship ice class code")
    private String iceClassCode;

    @Schema(description = "Date when ice class endorsement was issued")
    private Timestamp iceClassIssueDate;

    @Schema(description = "Place where ice class endorsement was issued")
    private String iceClassIssuePlace;

    @Schema(description = "Date when ice class endorsement ends")
    private Timestamp iceClassEndDate;

    @Schema(description = "Always null")
    @JsonIgnore
    private String classificationSociety;

    @Schema(description = "Tells whether vessel has a double bottom")
    private Boolean doubleBottom;

    @Schema(description = "Tells whether vessel has an Inert gas system")
    private Boolean inertGasSystem;

    @Schema(description = "Tells whether vessel has a ballast tank")
    private Boolean ballastTank;

    public void setAll(final BigInteger vesselId, final ConstructionData cd) {
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

    public void setVesselDetails(final fi.livi.digitraffic.meri.model.portnet.vesseldetails.VesselDetails vesselDetails) {
        this.vesselDetails = vesselDetails;
    }
}
