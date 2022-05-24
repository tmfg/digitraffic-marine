package fi.livi.digitraffic.meri.domain.portnet.vesseldetails;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import fi.livi.digitraffic.meri.portnet.xsd.IdentificationData;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.util.TypeUtil;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description="Vessel details", name = "VesselDetails")
@JsonPropertyOrder({ "vesselId", "mmsi", "name", "namePrefix", "imoLloyds", "radioCallSign", "radioCallSignType", "updateTimestamp",
                     "dataSource", "vesselConstruction", "vesselDimensions", "vesselRegistration", "vesselSystem" })
@Entity
@DynamicUpdate
public class VesselDetails {

    @Id
    private Long vesselId;

    @Schema(description = "Ship MMSI (Maritime Mobile Service Identity)")
    private Integer mmsi;

    @Schema(description = "Vessel name")
    private String name;

    @Schema(description = "Vessel name prefix")
    private String namePrefix;

    @Schema(description = "Ship IMO / Lloyds")
    private Integer imoLloyds;

    @Schema(description = "Ship radio call sign")
    private String radioCallSign;

    @Schema(description = "Ship radio call sign type")
    private String radioCallSignType;

    @Schema(description = "Timestamp of last vessel metadata update")
    private Timestamp updateTimestamp;

    @Schema(description = "Data source")
    private String dataSource;

    @Schema(description = "Vessel construction information")
    @OneToOne(targetEntity = VesselConstruction.class, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "vesselDetails")
    @JoinColumn(name = "vessel_id", nullable = false)
    private VesselConstruction vesselConstruction;

    @Schema(description = "Vessel dimension information")
    @OneToOne(targetEntity = VesselDimensions.class, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "vesselDetails")
    @JoinColumn(name = "vessel_id", nullable = false)
    private VesselDimensions vesselDimensions;

    @Schema(description = "Vessel registration information")
    @OneToOne(targetEntity = VesselRegistration.class, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "vesselDetails")
    @JoinColumn(name = "vessel_id", nullable = false)
    private VesselRegistration vesselRegistration;

    @Schema(description = "Vessel system information")
    @OneToOne(targetEntity = VesselSystem.class, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "vesselDetails")
    @JoinColumn(name = "vessel_id", nullable = false)
    private VesselSystem vesselSystem;

    public void setAll(final fi.livi.digitraffic.meri.portnet.xsd.VesselDetails vd) {
        final IdentificationData idData = vd.getIdentificationData();
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
            vesselConstruction.setVesselDetails(this);
        }
        this.vesselConstruction.setAll(idData.getVesselId(), vd.getConstructionData());
        if (this.vesselDimensions == null) {
            this.vesselDimensions = new VesselDimensions();
            vesselDimensions.setVesselDetails(this);
        }
        this.vesselDimensions.setAll(idData.getVesselId(), vd.getDimensions());
        if (this.vesselRegistration == null) {
            this.vesselRegistration = new VesselRegistration();
            vesselRegistration.setVesselDetails(this);
        }
        this.vesselRegistration.setAll(idData.getVesselId(), vd.getRegistrationData());
        if (this.vesselSystem == null) {
            this.vesselSystem = new VesselSystem();
            vesselSystem.setVesselDetails(this);
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
