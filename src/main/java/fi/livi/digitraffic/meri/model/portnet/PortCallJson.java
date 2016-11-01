package fi.livi.digitraffic.meri.model.portnet;

import java.sql.Timestamp;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="Port call")
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface PortCallJson {
    @ApiModelProperty(value = "Unique identifier for port call", required = true)
    long getPortCallId();

    @ApiModelProperty(value = "Port call information modification time", required = true)
    Timestamp getPortCallTimestamp();

    @ApiModelProperty(value = "Code of the port to visit")
    String getPortToVisit();
    @ApiModelProperty(value = "Code of the previous port")
    String getPrevPort();
    @ApiModelProperty(value = "Code of the next port")
    String getNextPort();

    @ApiModelProperty(value = "Is this a domestic arrival")
    Boolean getDomesticTrafficArrival();
    @ApiModelProperty(value = "Is this a domestic departure")
    Boolean getDomesticTrafficDeparture();
    @ApiModelProperty(value = "Does the ship arrive with cargo")
    Boolean getArrivalWithCargo();
    @ApiModelProperty(value = "Does the ship load")
    Boolean getNotLoading();
    @ApiModelProperty(value = "Type of discharge", allowableValues = "range[1..4]")
    Integer getDischarge();

    String getShipMasterArrival();
    String getShipMasterDeparture();
    String getManagementNameArrival();
    String getManagementNameDeparture();
    String getForwarderNameArrival();
    String getForwarderNameDeparture();
    String getFreeTextArrival();
    String getFreeTextDeparture();

    String getVesselName();
    String getVesselNamePrefix();
    String getRadioCallSign();
    String getRadioCallSignType();
    Integer getImoLloyds();
    Integer getMmsi();

    Set<AgentInfoJson> getAgentInfo();
    Set<ImoInformationJson> getImoInformation();
    Set<PortAreaDetailsJson> getPortAreaDetails();
}
