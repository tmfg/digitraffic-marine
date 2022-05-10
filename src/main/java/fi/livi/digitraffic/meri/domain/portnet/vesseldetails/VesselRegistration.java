package fi.livi.digitraffic.meri.domain.portnet.vesseldetails;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import fi.livi.digitraffic.meri.portnet.xsd.RegistrationData;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="Vessel registration", value = "VesselRegistration")
@JsonPropertyOrder({ "vesselId", "nationality", "portOfRegistry", "domicile" })
@Entity
@DynamicUpdate
public class VesselRegistration {

    @JsonIgnore
    @Id
    private Long vesselId;

    @JsonIgnore
    @OneToOne(targetEntity = fi.livi.digitraffic.meri.domain.portnet.vesseldetails.VesselDetails.class)
    @JoinColumn(name = "vesselId", nullable = false)
    @MapsId
    private fi.livi.digitraffic.meri.domain.portnet.vesseldetails.VesselDetails vesselDetails;

    @ApiModelProperty(value = "Ship nationality")
    private String nationality;

    @ApiModelProperty(value = "Ship home city")
    private String portOfRegistry;

    @ApiModelProperty(value = "Always null")
    @JsonIgnore
    private String domicile;

    public void setAll(final BigInteger vesselId, final RegistrationData registrationData) {
        this.vesselId = vesselId.longValue();
        this.nationality = registrationData.getNationality();
        this.portOfRegistry = registrationData.getPortOfRegistry();
        this.domicile = registrationData.getDomicile();
    }

    public Long getVesselId() {
        return vesselId;
    }

    public String getNationality() {
        return nationality;
    }

    public String getPortOfRegistry() {
        return portOfRegistry;
    }

    public String getDomicile() {
        return domicile;
    }

    public void setVesselDetails(final fi.livi.digitraffic.meri.domain.portnet.vesseldetails.VesselDetails vesselDetails) {
        this.vesselDetails = vesselDetails;
    }
}
