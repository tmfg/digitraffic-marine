package fi.livi.digitraffic.meri.model.portnet.vesseldetails;

import java.math.BigInteger;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.portnet.xsd.System;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;

@Schema(description="Vessel system", name = "VesselSystem")
@JsonPropertyOrder({ "vesselId", "shipOwner", "shipTelephone1", "shipTelephone2", "shipFax", "shipEmail", "shipVerifier" })
@Entity
@DynamicUpdate
public class VesselSystem {

    @JsonIgnore
    @Id
    private Long vesselId;

    @JsonIgnore
    @OneToOne(targetEntity = fi.livi.digitraffic.meri.model.portnet.vesseldetails.VesselDetails.class)
    @JoinColumn(name = "vesselId", nullable = false)
    @MapsId
    private fi.livi.digitraffic.meri.model.portnet.vesseldetails.VesselDetails vesselDetails;

    @Schema(description = "Ship owner name")
    private String shipOwner;

    @Schema(description = "Ship telephone")
    private String shipTelephone1;

    @Schema(description = "Always null")
    @JsonIgnore
    private String shipTelephone2;

    @Schema(description = "Always null")
    @JsonIgnore
    private String shipFax;

    @Schema(description = "Ship email address")
    private String shipEmail;

    @Schema(description = "Ship verifier")
    private String shipVerifier;

    public void setAll(final BigInteger vesselId, final System system) {
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

    public String getShipOwner() {
        return shipOwner;
    }

    public String getShipTelephone1() {
        return shipTelephone1;
    }

    public String getShipTelephone2() {
        return shipTelephone2;
    }

    public String getShipFax() {
        return shipFax;
    }

    public String getShipEmail() {
        return shipEmail;
    }

    public String getShipVerifier() {
        return shipVerifier;
    }

    public void setVesselDetails(final fi.livi.digitraffic.meri.model.portnet.vesseldetails.VesselDetails vesselDetails) {
        this.vesselDetails = vesselDetails;
    }
}
