package fi.livi.digitraffic.meri.model.portnet.metadata;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModel;

@ApiModel(description="Vessel registration")
@JsonPropertyOrder({ "vesselId", "nationality", "portOfRegistry", "domicile" })
public interface VesselRegistrationJson {

    Long getVesselId();

    String getNationality();

    String getPortOfRegistry();

    String getDomicile();
}
