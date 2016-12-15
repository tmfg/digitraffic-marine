package fi.livi.digitraffic.meri.domain.portnet.VesselDetails;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate
public class VesselRegistration {

    @Id
    private Long vesselId;

    private String nationality;

    private String portOfRegistry;

    private String domicile;

    public Long getVesselId() {
        return vesselId;
    }

    public void setVesselId(Long vesselId) {
        this.vesselId = vesselId;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getPortOfRegistry() {
        return portOfRegistry;
    }

    public void setPortOfRegistry(String portOfRegistry) {
        this.portOfRegistry = portOfRegistry;
    }

    public String getDomicile() {
        return domicile;
    }

    public void setDomicile(String domicile) {
        this.domicile = domicile;
    }
}
