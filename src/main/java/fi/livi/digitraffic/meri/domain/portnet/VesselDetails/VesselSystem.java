package fi.livi.digitraffic.meri.domain.portnet.VesselDetails;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicUpdate;

import fi.livi.digitraffic.meri.portnet.vesseldetails.xsd.VesselDetails;

@Entity
@DynamicUpdate
public class VesselSystem {

    @Id
    private Long vesselId;

    private String shipOwner;

    private String shipTelephone1;

    private String shipTelephone2;

    private String shipFax;

    private String shipEmail;

    private String shipVerifier;

    public void setAll(BigInteger vesselId, VesselDetails.System system) {
        this.vesselId = vesselId.longValue();
        this.shipOwner = system.getShipOwner();
        this.shipTelephone1 = system.getShipTelephone1();
        this.shipTelephone2 = system.getShipTelephone2();
        this.shipFax = system.getShipFax();
        this.shipEmail = system.getShipEmail();
        this.shipVerifier = system.getShipVerifier();
    }

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

    public String getShipTelephone2() {
        return shipTelephone2;
    }

    public void setShipTelephone2(String shipTelephone2) {
        this.shipTelephone2 = shipTelephone2;
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
