package fi.livi.digitraffic.meri.model.portnet.data;

import java.sql.Timestamp;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Port call")
@JsonPropertyOrder({ "portCallId", "portCallTimestamp", "customsReference", "portToVisit", "prevPort", "nextPort", "domesticTrafficArrival",
                     "domesticTrafficDeparture",
                     "arrivalWithCargo", "notLoading", "discharge", "shipMasterArrival", "shipMasterDeparture", "managementNameArrival",
                     "managementNameDeparture", "forwarderNameArrival",
                     "forwarderNameDeparture", "freeTextArrival", "freeTextDeparture", "vesselName", "vesselNamePrefix", "radioCallSign",
                     "radioCallSignType", "imoLloyds", "mmsi",
                     "nationality", "vesselTypeCode",
                     "certificateIssuer", "certificateStartDate", "certificateEndDate", "currentSecurityLevel", "agentInfo", "imoInformation",
                     "portAreaDetails" })
public interface PortCallJson {
    @Schema(description = "Unique identifier for port call", requiredMode = Schema.RequiredMode.REQUIRED)
    Long getPortCallId();

    @Schema(description = "Port call information modification time")
    Timestamp getPortCallTimestamp();

    @Schema(description = "Custom's reference")
    String getCustomsReference();
    @Schema(description = "Code of the port to visit")
    String getPortToVisit();
    @Schema(description = "Code of the previous port")
    String getPrevPort();
    @Schema(description = "Code of the next port")
    String getNextPort();

    @Schema(description = "Is this a domestic arrival")
    Boolean getDomesticTrafficArrival();
    @Schema(description = "Is this a domestic departure")
    Boolean getDomesticTrafficDeparture();
    @Schema(description = "Does the ship arrive with cargo")
    Boolean getArrivalWithCargo();
    @Schema(description = "Does the ship load")
    Boolean getNotLoading();
    @Schema(description = "Type of discharge<br>" +
                          "1: Unloads all cargo<br>" +
                          "2: Unloads part of cargo<br>" +
                          "3: Does not unload cargo<br>" +
                          "4: Ship arriving in ballast cargo", allowableValues = {"1", "2", "3", "4"})
    Integer getDischarge();

    @Schema(description = "Ship master at the moment of arrival(deprecated)")
    @Value("#{target.EMPTY}")
    String getShipMasterArrival();
    @Schema(description = "Ship master at the moment of departure(deprecated)")
    @Value("#{target.EMPTY}")
    String getShipMasterDeparture();
    @Schema(description = "Ship management name at moment of arrival")
    String getManagementNameArrival();
    @Schema(description = "Ship management name at moment of departure")
    String getManagementNameDeparture();
    @Schema(description = "Forwarder at moment of arrival")
    String getForwarderNameArrival();
    @Schema(description = "Forwarder at moment of departure")
    String getForwarderNameDeparture();

    @Schema(description = "Notes concerning arrival")
    String getFreeTextArrival();
    @Schema(description = "Notes concerning departure")
    String getFreeTextDeparture();

    @Schema(description = "Vessel name")
    String getVesselName();
    @Schema(description = "Vessel name prefix")
    String getVesselNamePrefix();
    @Schema(description = "Ship radio call sign")
    String getRadioCallSign();
    @Schema(description = "Ship radio call sign type")
    String getRadioCallSignType();

    @Schema(description = "Ship IMO / Lloyds")
    Integer getImoLloyds();

    @Schema(description = "Ship nationality")
    String getNationality();
    @Schema(description = "Ship Vessel Type Code")
    Integer getVesselTypeCode();

    @Schema(description = "Ship MMSI (Maritime Mobile Service Identity)")
    Integer getMmsi();
    @Schema(description = "Name of certificate issuer")
    String getCertificateIssuer();
    @Schema(description = "Certificate start date")
    Timestamp getCertificateStartDate();
    @Schema(description = "Certificate end date")
    Timestamp getCertificateEndDate();
    @Schema(description = "Current security level. 1 = normal security level, 2 = raised security level, 3 = exceptional security level")
    Integer getCurrentSecurityLevel();

    @Schema(description = "Ship agents")
    Set<AgentInfoJson> getAgentInfo();
    @Schema(description = "IMO information")
    Set<ImoInformationJson> getImoInformation();
    @Schema(description = "Port area details")
    Set<PortAreaDetailsJson> getPortAreaDetails();
}
