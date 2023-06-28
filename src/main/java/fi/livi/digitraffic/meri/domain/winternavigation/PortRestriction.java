package fi.livi.digitraffic.meri.domain.winternavigation;

import java.time.ZonedDateTime;
import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import jakarta.persistence.SequenceGenerator;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@DynamicUpdate
public class PortRestriction {

    @Id
    @SequenceGenerator(name = "SEQ_PORT_RESTRICTION", sequenceName = "SEQ_PORT_RESTRICTION", allocationSize = 1)
    @GeneratedValue(generator = "SEQ_PORT_RESTRICTION")
    private Long id;

    private String locode;

    private Integer orderNumber;

    private Boolean isCurrent;

    private Boolean portRestricted;

    private Boolean portClosed;

    private ZonedDateTime issueTime;

    private ZonedDateTime lastModified;

    private Date validFrom;

    private Date validUntil;

    private String rawText;

    private String formattedText;

    public Long getId() {
        return id;
    }

    public String getLocode() {
        return locode;
    }

    public void setLocode(String locode) {
        this.locode = locode;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
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

    public ZonedDateTime getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(ZonedDateTime issueTime) {
        this.issueTime = issueTime;
    }

    public ZonedDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(ZonedDateTime lastModified) {
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
