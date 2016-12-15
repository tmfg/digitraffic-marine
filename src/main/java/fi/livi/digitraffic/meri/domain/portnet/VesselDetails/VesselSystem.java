package fi.livi.digitraffic.meri.domain.portnet.VesselDetails;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate
public class VesselSystem {

    @Id
    private Long vesselId;

    private String shipOwner;

    private String shipTelephone1;

    private String getShipTelephone2;

    private String shipFax;

    private String shipEmail;

    private String shipVerifier;

    public Long getVesselId() {
        return vesselId;
    }

    public void setVesselId(Long vesselId) {
        this.vesselId = vesselId;
    }

    public String getShipOwner() {
        return shipOwner;
    }

    public void setShipOwner(String shipOwner) {
        this.shipOwner = shipOwner;
    }

    public String getShipTelephone1() {
        return shipTelephone1;
    }

    public void setShipTelephone1(String shipTelephone1) {
        this.shipTelephone1 = shipTelephone1;
    }

    public String getGetShipTelephone2() {
        return getShipTelephone2;
    }

    public void setGetShipTelephone2(String getShipTelephone2) {
        this.getShipTelephone2 = getShipTelephone2;
    }

    public String getShipFax() {
        return shipFax;
    }

    public void setShipFax(String shipFax) {
        this.shipFax = shipFax;
    }

    public String getShipEmail() {
        return shipEmail;
    }

    public void setShipEmail(String shipEmail) {
        this.shipEmail = shipEmail;
    }

    public String getShipVerifier() {
        return shipVerifier;
    }

    public void setShipVerifier(String shipVerifier) {
        this.shipVerifier = shipVerifier;
    }
}
