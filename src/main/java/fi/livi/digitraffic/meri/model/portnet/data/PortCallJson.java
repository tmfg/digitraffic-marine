package fi.livi.digitraffic.meri.model.portnet.data;

import java.sql.Timestamp;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="Port call")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"portCallId", "portCallTimestamp", "customsReference", "portToVisit", "prevPort", "nextPort", "domesticTrafficArrival", "domesticTrafficDeparture",
"arrivalWithCargo", "notLoading", "discharge", "shipMasterArrival", "shipMasterDeparture", "managementNameArrival", "managementNameDeparture", "forwaredNameArrival",
"forwarderNameDeparture", "freeTextArrival", "freeTextDeparture", "vesselName", "vesselNamePrefix", "radioCallSign", "radioCallSignType", "ImoLloyds", "mmsi",
"certificateIssuer", "certificateStartDate", "certificateEndDate", "currentSecurityLevel", "agentInfo", "imoInformation", "portAreaDetails"})
public interface PortCallJson {
    @ApiModelProperty(value = "Unique identifier for port call", required = true)
    Long getPortCallId();

    @ApiModelProperty(value = "Port call information modification time", required = true)
    Timestamp getPortCallTimestamp();

    @ApiModelProperty(value = "Custom's reference")
    String getCustomsReference();
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
    @ApiModelProperty(value = "Type of discharge", allowableValues = "range[1, 4]")
    Integer getDischarge();

    @ApiModelProperty(value = "Ship master at the moment of arrival")
    String getShipMasterArrival();
    @ApiModelProperty(value = "Ship master at the moment of departure")
    String getShipMasterDeparture();
    @ApiModelProperty(value = "Ship management name at moment of arrival")
    String getManagementNameArrival();
    @ApiModelProperty(value = "Ship management name at moment of departure")
    String getManagementNameDeparture();
    @ApiModelProperty(value = "Forwarder at moment of arrival")
    String getForwarderNameArrival();
    @ApiModelProperty(value = "Forwarder at moment of departure")
    String getForwarderNameDeparture();

    @ApiModelProperty(value = "Notes concerning arrival")
    String getFreeTextArrival();
    @ApiModelProperty(value = "Notes concerning departure")
    String getFreeTextDeparture();

    @ApiModelProperty(value = "Vessel name")
    String getVesselName();
    @ApiModelProperty(value = "Vessel name prefix")
    String getVesselNamePrefix();
    @ApiModelProperty(value = "Ship radio call sign")
    String getRadioCallSign();
    @ApiModelProperty(value = "Ship radio call sign type")
    String getRadioCallSignType();

    @ApiModelProperty(value = "Ship IMO / Lloyds")
    Integer getImoLloyds();

    @ApiModelProperty(value = "Ship MMSI (Maritime Mobile Service Identity)")
    Integer getMmsi();
    @ApiModelProperty(value = "Name of certificate issuer")
    String getCertificateIssuer();
    @ApiModelProperty(value = "Certificate start date")
    Timestamp getCertificateStartDate();
    @ApiModelProperty(value = "Certificate end date")
    Timestamp getCertificateEndDate();
    @ApiModelProperty(value = "Current security level. 1 = normal security level, 2 = raised security level, 3 = exceptional security level")
    Integer getCurrentSecurityLevel();

    @ApiModelProperty(value = "Ship agents")
    Set<AgentInfoJson> getAgentInfo();
    @ApiModelProperty(value = "IMO information")
    Set<ImoInformationJson> getImoInformation();
    @ApiModelProperty(value = "Port area details")
    Set<PortAreaDetailsJson> getPortAreaDetails();
}
