package fi.livi.digitraffic.meri.domain.portnet.VesselDetails;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.util.TypeUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="Vessel details", value = "VesselDetails")
@JsonPropertyOrder({ "vesselId", "mmsi", "name", "namePrefix", "imoLloyds", "radioCallSign", "radioCallSignType", "updateTimestamp",
                     "dataSource", "vesselConstruction", "vesselDimensions", "vesselRegistration", "vesselSystem" })
@Entity
@DynamicUpdate
public class VesselDetails {

    @Id
    private Long vesselId;

    @ApiModelProperty(value = "Ship MMSI (Maritime Mobile Service Identity)")
    private Integer mmsi;

    @ApiModelProperty(value = "Vessel name")
    private String name;

    @ApiModelProperty(value = "Vessel name prefix")
    private String namePrefix;

    @ApiModelProperty(value = "Ship IMO / Lloyds")
    private Integer imoLloyds;

    @ApiModelProperty(value = "Ship radio call sign")
    private String radioCallSign;

    @ApiModelProperty(value = "Ship radio call sign type")
    private String radioCallSignType;

    @ApiModelProperty(value = "Timestamp of last vessel metadata update")
    private Timestamp updateTimestamp;

    @ApiModelProperty(value = "Data source")
    private String dataSource;

    @ApiModelProperty(value = "Vessel construction information")
    @OneToOne(targetEntity = VesselConstruction.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "vessel_id", nullable = false)
    private VesselConstruction vesselConstruction;

    @ApiModelProperty(value = "Vessel dimension information")
    @OneToOne(targetEntity = VesselDimensions.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "vessel_id", nullable = false)
    private VesselDimensions vesselDimensions;

    @ApiModelProperty(value = "Vessel registration information")
    @OneToOne(targetEntity = VesselRegistration.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "vessel_id", nullable = false)
    private VesselRegistration vesselRegistration;

    @ApiModelProperty(value = "Vessel system information")
    @OneToOne(targetEntity = VesselSystem.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "vessel_id", nullable = false)
    private VesselSystem vesselSystem;

    public void setAll(final fi.livi.digitraffic.meri.portnet.vesseldetails.xsd.VesselDetails vd) {
        final fi.livi.digitraffic.meri.portnet.vesseldetails.xsd.VesselDetails.IdentificationData idData = vd.getIdentificationData();
        this.vesselId = idData.getVesselId().longValue();
        this.mmsi = TypeUtil.getInteger(idData.getMmsi());
        this.name = idData.getName();
        this.namePrefix = idData.getNamePrefix();
        this.imoLloyds = TypeUtil.getInteger(idData.getImoLloyds());
        this.radioCallSign = idData.getRadioCallSign();
        this.radioCallSignType = TypeUtil.getEnum(idData.getRadioCallSignType());
        this.updateTimestamp = TypeUtil.getTimestamp(idData.getUpdateTimeStamp());
        this.dataSource = idData.getDataSource();
        if (this.vesselConstruction == null) {
            this.vesselConstruction = new VesselConstruction();
        }
        this.vesselConstruction.setAll(idData.getVesselId(), vd.getConstructionData());
        if (this.vesselDimensions == null) {
            this.vesselDimensions = new VesselDimensions();
        }
        this.vesselDimensions.setAll(idData.getVesselId(), vd.getDimensions());
        if (this.vesselRegistration == null) {
            this.vesselRegistration = new VesselRegistration();
        }
        this.vesselRegistration.setAll(idData.getVesselId(), vd.getRegistrationData());
        if (this.vesselSystem == null) {
            this.vesselSystem = new VesselSystem();
        }
        this.vesselSystem.setAll(idData.getVesselId(), vd.getSystem());
    }

    public Long getVesselId() {
        return vesselId;
    }

    public Integer getMmsi() {
        return mmsi;
    }

    public String getName() {
        return name;
    }

    public String getNamePrefix() {
        return namePrefix;
    }

    public Integer getImoLloyds() {
        return imoLloyds;
    }

    public String getRadioCallSign() {
        return radioCallSign;
    }

    public String getRadioCallSignType() {
        return radioCallSignType;
    }

    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

    public String getDataSource() {
        return dataSource;
    }

    public VesselConstruction getVesselConstruction() {
        return vesselConstruction;
    }

    public VesselDimensions getVesselDimensions() {
        return vesselDimensions;
    }

    public VesselRegistration getVesselRegistration() {
        return vesselRegistration;
    }

    public VesselSystem getVesselSystem() {
        return vesselSystem;
    }
}
