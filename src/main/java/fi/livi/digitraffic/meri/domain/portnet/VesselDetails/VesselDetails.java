package fi.livi.digitraffic.meri.domain.portnet.VesselDetails;

import java.time.ZonedDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.DynamicUpdate;

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

    private ZonedDateTime updateTimeStamp;

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

    public ZonedDateTime getUpdateTimeStamp() {
        return updateTimeStamp;
    }

    public void setUpdateTimeStamp(ZonedDateTime updateTimeStamp) {
        this.updateTimeStamp = updateTimeStamp;
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
