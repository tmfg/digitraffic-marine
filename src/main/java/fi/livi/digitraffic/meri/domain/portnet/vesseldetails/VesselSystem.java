package fi.livi.digitraffic.meri.domain.portnet.vesseldetails;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.portnet.vesseldetails.xsd.VesselDetails;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="Vessel system", value = "VesselSystem")
@JsonPropertyOrder({ "vesselId", "shipOwner", "shipTelephone1", "shipTelephone2", "shipFax", "shipEmail", "shipVerifier" })
@Entity
@DynamicUpdate
public class VesselSystem {

    @JsonIgnore
    @Id
    private Long vesselId;

    @JsonIgnore
    @OneToOne(targetEntity = fi.livi.digitraffic.meri.domain.portnet.vesseldetails.VesselDetails.class)
    @JoinColumn(name = "vesselId", nullable = false)
    @MapsId
    private fi.livi.digitraffic.meri.domain.portnet.vesseldetails.VesselDetails vesselDetails;

    @ApiModelProperty(value = "Ship owner name")
    private String shipOwner;

    @ApiModelProperty(value = "Ship telephone")
    private String shipTelephone1;

    @ApiModelProperty(value = "Always null")
    @JsonIgnore
    private String shipTelephone2;

    @ApiModelProperty(value = "Always null")
    @JsonIgnore
    private String shipFax;

    @ApiModelProperty(value = "Ship email address")
    private String shipEmail;

    @ApiModelProperty(value = "Ship verifier")
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

    public void setVesselDetails(final fi.livi.digitraffic.meri.domain.portnet.vesseldetails.VesselDetails vesselDetails) {
        this.vesselDetails = vesselDetails;
    }
}
