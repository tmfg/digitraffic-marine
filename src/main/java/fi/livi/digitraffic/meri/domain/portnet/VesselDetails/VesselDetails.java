package fi.livi.digitraffic.meri.domain.portnet.VesselDetails;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.DynamicUpdate;

import fi.livi.digitraffic.util.TypeUtil;

@Entity
@DynamicUpdate
public class VesselDetails {

    @Id
    private Long vesselId;

    private Integer mmsi;

    private String name;

    private String namePrefix;

    private Integer imoLloyds;

    private String radioCallSign;

    private String radioCallSignType;

    private Timestamp updateTimestamp;

    private String dataSource;

    @OneToOne(targetEntity = VesselConstruction.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "vessel_id", nullable = false)
    private VesselConstruction vesselConstruction;

    @OneToOne(targetEntity = VesselDimensions.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "vessel_id", nullable = false)
    private VesselDimensions vesselDimensions;

    @OneToOne(targetEntity = VesselRegistration.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "vessel_id", nullable = false)
    private VesselRegistration vesselRegistration;

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

    public void setVesselId(Long vesselId) {
        this.vesselId = vesselId;
    }

    public Integer getMmsi() {
        return mmsi;
    }

    public void setMmsi(Integer mmsi) {
        this.mmsi = mmsi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamePrefix() {
        return namePrefix;
    }

    public void setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    public Integer getImoLloyds() {
        return imoLloyds;
    }

    public void setImoLloyds(Integer imoLloyds) {
        this.imoLloyds = imoLloyds;
    }

    public String getRadioCallSign() {
        return radioCallSign;
    }

    public void setRadioCallSign(String radioCallSign) {
        this.radioCallSign = radioCallSign;
    }

    public String getRadioCallSignType() {
        return radioCallSignType;
    }

    public void setRadioCallSignType(String radioCallSignType) {
        this.radioCallSignType = radioCallSignType;
    }

    public Timestamp getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(Timestamp updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public VesselConstruction getVesselConstruction() {
        return vesselConstruction;
    }

    public void setVesselConstruction(VesselConstruction vesselConstruction) {
        this.vesselConstruction = vesselConstruction;
    }

    public VesselDimensions getVesselDimensions() {
        return vesselDimensions;
    }

    public void setVesselDimensions(VesselDimensions vesselDimensions) {
        this.vesselDimensions = vesselDimensions;
    }

    public VesselRegistration getVesselRegistration() {
        return vesselRegistration;
    }

    public void setVesselRegistration(VesselRegistration vesselRegistration) {
        this.vesselRegistration = vesselRegistration;
    }

    public VesselSystem getVesselSystem() {
        return vesselSystem;
    }

    public void setVesselSystem(VesselSystem vesselSystem) {
        this.vesselSystem = vesselSystem;
    }
}
