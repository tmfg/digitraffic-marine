package fi.livi.digitraffic.meri.domain.winternavigation;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;

@Entity
@DynamicUpdate
public class PortRestriction {

    @EmbeddedId
    @JsonIgnore
    private PortRestrictionPK portRestrictionPK;

    @ApiModelProperty(value = "Indicates whether this restriction is currently in effect (true); false implies past/future restriction")
    private Boolean isCurrent;

    @ApiModelProperty(value = "Indicates whether traffic to this port is restricted (true) or not (false)")
    private Boolean portRestricted;

    @ApiModelProperty(value = "Indicates if the port is closed due to extreme ice conditions; icebreaker assistance is not provided")
    private Boolean portClosed;

    @ApiModelProperty(value = "Date and time of issue/announcement")
    private Timestamp issueTime;

    @ApiModelProperty(value = "Date and time of last modification")
    private Timestamp lastModified;

    @ApiModelProperty(value = "Date when this restriction starts/started to be in effect; this is optional if isCurrent is true and\n" +
                              "portRestricted is false, because during early winter – when no restrictions yet exist – the\n" +
                              "past restriction data (end time of past season's last restriction) might not always be available")
    private Date validFrom;

    @ApiModelProperty(value = "Date when this restriction ceased to be in effect")
    private Date validUntil;

    @ApiModelProperty(value = "Raw text of the port traffic restriction.\n" +
                              "Finnish-Swedish ice classes are IA Super, IA, IB, IC, II and I.\n" +
                              "The number following the ice class code stands for minimum required ship deadweight tonnage.\n" +
                              "'2000 t' in 'IA 4000 | 2000 t' code stands for minimum cargo the ship has to load or unload.")
    private String rawText;

    @ApiModelProperty(value = "Traffic restriction text pre-formatted (HTML)")
    private String formattedText;

    public PortRestrictionPK getPortRestrictionPK() {
        return portRestrictionPK;
    }

    public void setPortRestrictionPK(PortRestrictionPK portRestrictionPK) {
        this.portRestrictionPK = portRestrictionPK;
    }

    public Boolean getCurrent() {
        return isCurrent;
    }

    public void setCurrent(Boolean current) {
        isCurrent = current;
    }

    public Boolean getPortRestricted() {
        return portRestricted;
    }

    public void setPortRestricted(Boolean portRestricted) {
        this.portRestricted = portRestricted;
    }

    public Boolean getPortClosed() {
        return portClosed;
    }

    public void setPortClosed(Boolean portClosed) {
        this.portClosed = portClosed;
    }

    public Timestamp getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(Timestamp issueTime) {
        this.issueTime = issueTime;
    }

    public Timestamp getLastModified() {
        return lastModified;
    }

    public void setLastModified(Timestamp lastModified) {
        this.lastModified = lastModified;
    }

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
    }

    public String getRawText() {
        return rawText;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText;
    }

    public String getFormattedText() {
        return formattedText;
    }

    public void setFormattedText(String formattedText) {
        this.formattedText = formattedText;
    }
}
