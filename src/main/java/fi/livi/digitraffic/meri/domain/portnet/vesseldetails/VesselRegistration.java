package fi.livi.digitraffic.meri.domain.portnet.vesseldetails;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.portnet.vesseldetails.xsd.VesselDetails;
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

    @ApiModelProperty(value = "Ship nationality")
    private String nationality;

    @ApiModelProperty(value = "Ship home city")
    private String portOfRegistry;

    @ApiModelProperty(value = "Always null")
    @JsonIgnore
    private String domicile;

    public void setAll(BigInteger vesselId, VesselDetails.RegistrationData registrationData) {
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
}
