package fi.livi.digitraffic.meri.model.portnet.vesseldetails;

import java.sql.Timestamp;
import java.time.Instant;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.common.dto.LastModifiedSupport;
import fi.livi.digitraffic.meri.model.ReadOnlyCreatedAndModifiedFields;
import fi.livi.digitraffic.meri.portnet.xsd.IdentificationData;
import fi.livi.digitraffic.meri.util.TypeUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Schema(description="Vessel details", name = "VesselDetails")
@JsonPropertyOrder({ "vesselId", "mmsi", "name", "namePrefix", "imoLloyds", "radioCallSign", "radioCallSignType", "updateTimestamp",
                     "dataSource", "vesselConstruction", "vesselDimensions", "vesselRegistration", "vesselSystem" })
@Entity
@DynamicUpdate
public class VesselDetails extends ReadOnlyCreatedAndModifiedFields implements LastModifiedSupport {

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
    private VesselConstruction vesselConstruction;

    @Schema(description = "Vessel dimension information")
    @OneToOne(targetEntity = VesselDimensions.class, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "vesselDetails")
    private VesselDimensions vesselDimensions;

    @Schema(description = "Vessel registration information")
    @OneToOne(targetEntity = VesselRegistration.class, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "vesselDetails")
    @JoinColumn(name = "vessel_id", nullable = false)
    private VesselRegistration vesselRegistration;

    @Schema(description = "Vessel system information")
    @OneToOne(targetEntity = VesselSystem.class, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "vesselDetails")
    @JoinColumn(name = "vessel_id", nullable = false)
    private VesselSystem vesselSystem;

    @JsonIgnore
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
        this.vesselConstruction.setAll(vd.getConstructionData());
        if (this.vesselDimensions == null) {
            this.vesselDimensions = new VesselDimensions();
            vesselDimensions.setVesselDetails(this);
        }
        this.vesselDimensions.setAll(vd.getDimensions());
        if (this.vesselRegistration == null) {
            this.vesselRegistration = new VesselRegistration();
            vesselRegistration.setVesselDetails(this);
        }
        this.vesselRegistration.setAll(vd.getRegistrationData());
        if (this.vesselSystem == null) {
            this.vesselSystem = new VesselSystem();
            vesselSystem.setVesselDetails(this);
        }
        this.vesselSystem.setAll(vd.getSystem());
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

    @Override
    public Instant getLastModified() {
        return getModified();
    }
}
