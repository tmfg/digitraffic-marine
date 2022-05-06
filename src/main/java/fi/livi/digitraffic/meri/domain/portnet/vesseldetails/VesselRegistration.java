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

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description="Vessel registration", name = "VesselRegistration")
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

    @Schema(description = "Ship nationality")
    private String nationality;

    @Schema(description = "Ship home city")
    private String portOfRegistry;

    @Schema(description = "Always null")
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

    public void setVesselDetails(final fi.livi.digitraffic.meri.domain.portnet.vesseldetails.VesselDetails vesselDetails) {
        this.vesselDetails = vesselDetails;
    }
}
