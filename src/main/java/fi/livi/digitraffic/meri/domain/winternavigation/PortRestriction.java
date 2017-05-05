package fi.livi.digitraffic.meri.domain.winternavigation;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate
public class PortRestriction {

    @EmbeddedId
    private PortRestrictionPK portRestrictionPK;

    private Boolean isCurrent;

    private Boolean portRestricted;

    private Boolean portClosed;

    private Timestamp issueTime;

    private Timestamp lastModified;

    private Date validFrom;

    private Date validUntil;

    private String rawText;

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
