package fi.livi.digitraffic.meri.domain.portnet;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@DynamicInsert
public class PortAreaDetails {
    @Id
    @GenericGenerator(name = "SEQ_PORT_AREA_DETAILS", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = @Parameter(name = "sequence_name", value = "SEQ_PORT_AREA_DETAILS"))
    @GeneratedValue(generator = "SEQ_PORT_AREA_DETAILS")
    private long portAreaDetailsId;

    private String portAreaCode;
    private String portAreaName;
    private String berthCode;
    private String berthName;

    private Timestamp eta;
    private Timestamp etaTimestamp;
    private String etaSource;

    private Timestamp etd;
    private Timestamp etdTimestamp;
    private String etdSource;

    private Timestamp ata;
    private Timestamp ataTimestamp;
    private String ataSource;

    private Timestamp atd;
    private Timestamp atdTimestamp;
    private String atdSource;

    private BigDecimal arrivalDraught;
    private BigDecimal departureDraught;

    @OneToMany(targetEntity = CargoInfo.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "port_area_details_id", nullable = false)
    private Set<CargoInfo> cargoInfo = new HashSet<>();

    public String getPortAreaCode() {
        return portAreaCode;
    }

    public void setPortAreaCode(final String portAreaCode) {
        this.portAreaCode = portAreaCode;
    }

    public String getPortAreaName() {
        return portAreaName;
    }

    public void setPortAreaName(final String portAreaName) {
        this.portAreaName = portAreaName;
    }

    public String getBerthCode() {
        return berthCode;
    }

    public void setBerthCode(final String berthCode) {
        this.berthCode = berthCode;
    }

    public String getBerthName() {
        return berthName;
    }

    public void setBerthName(final String berthName) {
        this.berthName = berthName;
    }

    public long getPortAreaDetailsId() {
        return portAreaDetailsId;
    }

    public void setPortAreaDetailsId(final long portAreaDetailsId) {
        this.portAreaDetailsId = portAreaDetailsId;
    }

    public Timestamp getEta() {
        return eta;
    }

    public void setEta(final Timestamp eta) {
        this.eta = eta;
    }

    public Timestamp getEtaTimestamp() {
        return etaTimestamp;
    }

    public void setEtaTimestamp(final Timestamp etaTimestamp) {
        this.etaTimestamp = etaTimestamp;
    }

    public String getEtaSource() {
        return etaSource;
    }

    public void setEtaSource(final String etaSource) {
        this.etaSource = etaSource;
    }

    public Timestamp getEtd() {
        return etd;
    }

    public void setEtd(final Timestamp etd) {
        this.etd = etd;
    }

    public Timestamp getEtdTimestamp() {
        return etdTimestamp;
    }

    public void setEtdTimestamp(final Timestamp etdTimestamp) {
        this.etdTimestamp = etdTimestamp;
    }

    public String getEtdSource() {
        return etdSource;
    }

    public void setEtdSource(final String etdSource) {
        this.etdSource = etdSource;
    }

    public Timestamp getAta() {
        return ata;
    }

    public void setAta(final Timestamp ata) {
        this.ata = ata;
    }

    public Timestamp getAtaTimestamp() {
        return ataTimestamp;
    }

    public void setAtaTimestamp(final Timestamp ataTimestamp) {
        this.ataTimestamp = ataTimestamp;
    }

    public String getAtaSource() {
        return ataSource;
    }

    public void setAtaSource(final String ataSource) {
        this.ataSource = ataSource;
    }

    public Timestamp getAtd() {
        return atd;
    }

    public void setAtd(final Timestamp atd) {
        this.atd = atd;
    }

    public Timestamp getAtdTimestamp() {
        return atdTimestamp;
    }

    public void setAtdTimestamp(final Timestamp atdTimestamp) {
        this.atdTimestamp = atdTimestamp;
    }

    public String getAtdSource() {
        return atdSource;
    }

    public void setAtdSource(final String atdSource) {
        this.atdSource = atdSource;
    }

    public BigDecimal getArrivalDraught() {
        return arrivalDraught;
    }

    public void setArrivalDraught(final BigDecimal arrivalDraught) {
        this.arrivalDraught = arrivalDraught;
    }

    public BigDecimal getDepartureDraught() {
        return departureDraught;
    }

    public void setDepartureDraught(final BigDecimal departureDraught) {
        this.departureDraught = departureDraught;
    }

    public Set<CargoInfo> getCargoInfo() {
        return cargoInfo;
    }

    public void setCargoInfo(final Set<CargoInfo> cargoInfo) {
        this.cargoInfo = cargoInfo;
    }
}
