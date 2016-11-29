package fi.livi.digitraffic.meri.domain.portnet;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate
public class PortCall {
    @Id
    private Long portCallId;

    private Timestamp portCallTimestamp;

    private String customsReference;
    private String portToVisit;
    private String prevPort;
    private String nextPort;

    private Boolean domesticTrafficArrival;
    private Boolean domesticTrafficDeparture;
    private Boolean arrivalWithCargo;
    private Boolean notLoading;
    private Integer discharge;

    private String shipMasterArrival;
    private String shipMasterDeparture;
    private String managementNameArrival;
    private String managementNameDeparture;
    private String forwarderNameArrival;
    private String forwarderNameDeparture;
    private String freeTextArrival;
    private String freeTextDeparture;

    private String vesselName;
    private String vesselNamePrefix;
    private String radioCallSign;
    private String radioCallSignType;
    private Integer imoLloyds;
    private Integer mmsi;

    private String certificateIssuer;
    private Timestamp certificateStartDate;
    private Timestamp certificateEndDate;
    private Integer currentSecurityLevel;

    @OneToMany(targetEntity = PortAreaDetails.class, cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "port_call_id", nullable = false)
    private Set<PortAreaDetails> portAreaDetails = new HashSet<>();

    @OneToMany(targetEntity = ImoInformation.class, cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "port_call_id", nullable = false)
    private Set<ImoInformation> imoInformation = new HashSet<>();

    @OneToMany(targetEntity = AgentInfo.class, cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "port_call_id", nullable = false)
    private Set<AgentInfo> agentInfo = new HashSet<>();

    public Long getPortCallId() {
        return portCallId;
    }

    public void setPortCallId(final Long portCallId) {
        this.portCallId = portCallId;
    }

    public String getPortToVisit() {
        return portToVisit;
    }

    public void setPortToVisit(final String portToVisit) {
        this.portToVisit = portToVisit;
    }

    public String getPrevPort() {
        return prevPort;
    }

    public void setPrevPort(final String prevPort) {
        this.prevPort = prevPort;
    }

    public String getNextPort() {
        return nextPort;
    }

    public void setNextPort(final String nextPort) {
        this.nextPort = nextPort;
    }

    public Boolean getDomesticTrafficArrival() {
        return domesticTrafficArrival;
    }

    public void setDomesticTrafficArrival(final Boolean domesticTrafficArrival) {
        this.domesticTrafficArrival = domesticTrafficArrival;
    }

    public Boolean getDomesticTrafficDeparture() {
        return domesticTrafficDeparture;
    }

    public void setDomesticTrafficDeparture(final Boolean domesticTrafficDeparture) {
        this.domesticTrafficDeparture = domesticTrafficDeparture;
    }

    public Boolean getArrivalWithCargo() {
        return arrivalWithCargo;
    }

    public void setArrivalWithCargo(final Boolean arrivalWithCargo) {
        this.arrivalWithCargo = arrivalWithCargo;
    }

    public Integer getDischarge() {
        return discharge;
    }

    public void setDischarge(final Integer discharge) {
        this.discharge = discharge;
    }

    public Timestamp getPortCallTimestamp() {
        return portCallTimestamp;
    }

    public void setPortCallTimestamp(final Timestamp portCallTimestamp) {
        this.portCallTimestamp = portCallTimestamp;
    }

    public Boolean getNotLoading() {
        return notLoading;
    }

    public void setNotLoading(final Boolean notLoading) {
        this.notLoading = notLoading;
    }

    public String getShipMasterArrival() {
        return shipMasterArrival;
    }

    public void setShipMasterArrival(final String shipMasterArrival) {
        this.shipMasterArrival = shipMasterArrival;
    }

    public String getShipMasterDeparture() {
        return shipMasterDeparture;
    }

    public void setShipMasterDeparture(final String shipMasterDeparture) {
        this.shipMasterDeparture = shipMasterDeparture;
    }

    public String getManagementNameArrival() {
        return managementNameArrival;
    }

    public void setManagementNameArrival(final String managementNameArrival) {
        this.managementNameArrival = managementNameArrival;
    }

    public String getManagementNameDeparture() {
        return managementNameDeparture;
    }

    public void setManagementNameDeparture(final String managementNameDeparture) {
        this.managementNameDeparture = managementNameDeparture;
    }

    public String getForwarderNameArrival() {
        return forwarderNameArrival;
    }

    public void setForwarderNameArrival(final String forwarderNameArrival) {
        this.forwarderNameArrival = forwarderNameArrival;
    }

    public String getFreeTextArrival() {
        return freeTextArrival;
    }

    public void setFreeTextArrival(final String freeTextArrival) {
        this.freeTextArrival = freeTextArrival;
    }

    public String getForwarderNameDeparture() {
        return forwarderNameDeparture;
    }

    public void setForwarderNameDeparture(final String forwarderNameDeparture) {
        this.forwarderNameDeparture = forwarderNameDeparture;
    }

    public String getFreeTextDeparture() {
        return freeTextDeparture;
    }

    public void setFreeTextDeparture(final String freeTextDeparture) {
        this.freeTextDeparture = freeTextDeparture;
    }

    public Set<PortAreaDetails> getPortAreaDetails() {
        return portAreaDetails;
    }

    public void setPortAreaDetails(final Set<PortAreaDetails> portAreaDetails) {
        this.portAreaDetails = portAreaDetails;
    }

    public String getVesselName() {
        return vesselName;
    }

    public void setVesselName(final String vesselName) {
        this.vesselName = vesselName;
    }

    public String getVesselNamePrefix() {
        return vesselNamePrefix;
    }

    public void setVesselNamePrefix(final String vesselNamePrefix) {
        this.vesselNamePrefix = vesselNamePrefix;
    }

    public String getRadioCallSign() {
        return radioCallSign;
    }

    public void setRadioCallSign(final String radioCallSign) {
        this.radioCallSign = radioCallSign;
    }

    public String getRadioCallSignType() {
        return radioCallSignType;
    }

    public void setRadioCallSignType(final String radioCallSignType) {
        this.radioCallSignType = radioCallSignType;
    }

    public Integer getImoLloyds() {
        return imoLloyds;
    }

    public void setImoLloyds(final Integer imoLloyds) {
        this.imoLloyds = imoLloyds;
    }

    public Integer getMmsi() {
        return mmsi;
    }

    public void setMmsi(final Integer mmsi) {
        this.mmsi = mmsi;
    }

    public Set<ImoInformation> getImoInformation() {
        return imoInformation;
    }

    public void setImoInformation(final Set<ImoInformation> imoInformation) {
        this.imoInformation = imoInformation;
    }

    public Set<AgentInfo> getAgentInfo() {
        return agentInfo;
    }

    public void setAgentInfo(final Set<AgentInfo> agentInfo) {
        this.agentInfo = agentInfo;
    }

    public String getCustomsReference() {
        return customsReference;
    }

    public void setCustomsReference(final String customsReference) {
        this.customsReference = customsReference;
    }

    public String getCertificateIssuer() {
        return certificateIssuer;
    }

    public void setCertificateIssuer(final String certificateIssuer) {
        this.certificateIssuer = certificateIssuer;
    }

    public Timestamp getCertificateStartDate() {
        return certificateStartDate;
    }

    public void setCertificateStartDate(final Timestamp certificateStartDate) {
        this.certificateStartDate = certificateStartDate;
    }

    public Timestamp getCertificateEndDate() {
        return certificateEndDate;
    }

    public void setCertificateEndDate(final Timestamp certificateEndDate) {
        this.certificateEndDate = certificateEndDate;
    }

    public Integer getCurrentSecurityLevel() {
        return currentSecurityLevel;
    }

    public void setCurrentSecurityLevel(final Integer currentSecurityLevel) {
        this.currentSecurityLevel = currentSecurityLevel;
    }
}
