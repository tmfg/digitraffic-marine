package fi.livi.digitraffic.meri.model.portnet.metadata;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModel;

@ApiModel(description="Vessel system")
@JsonPropertyOrder({ "vesselId", "shipOwner", "shipTelephone1", "shipTelephone2", "shipFax", "shipEmail", "shipVerifier" })
public interface VesselSystemJson {

    Long getVesselId();

    String getShipOwner();

    String getShipTelephone1();

    String getShipTelephone2();

    String getShipFax();

    String getShipEmail();

    String getShipVerifier();
}
