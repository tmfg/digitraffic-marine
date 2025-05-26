package fi.livi.digitraffic.meri.model.winternavigation;

import java.time.Instant;
import java.time.LocalDate;

import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity
@DynamicUpdate
public class PortRestriction {

    @Id
    @SequenceGenerator(name = "SEQ_PORT_RESTRICTION",
                       sequenceName = "SEQ_PORT_RESTRICTION",
                       allocationSize = 1)
    @GeneratedValue(generator = "SEQ_PORT_RESTRICTION")
    private Long id;

    private String locode;

    private Integer orderNumber;

    private Boolean isCurrent;

    private Boolean portRestricted;

    private Boolean portClosed;

    private Instant issueTime;

    private Instant lastModified;

    private LocalDate validFrom;

    private LocalDate validUntil;

    private String rawText;

    private String formattedText;

    public Long getId() {
        return id;
    }

    public String getLocode() {
        return locode;
    }

    public void setLocode(final String locode) {
        this.locode = locode;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(final Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Boolean getCurrent() {
        return isCurrent;
    }

    public void setCurrent(final Boolean current) {
        isCurrent = current;
    }

    public Boolean getPortRestricted() {
        return portRestricted;
    }

    public void setPortRestricted(final Boolean portRestricted) {
        this.portRestricted = portRestricted;
    }

    public Boolean getPortClosed() {
        return portClosed;
    }

    public void setPortClosed(final Boolean portClosed) {
        this.portClosed = portClosed;
    }

    public Instant getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(final Instant issueTime) {
        this.issueTime = issueTime;
    }

    public Instant getLastModified() {
        return lastModified;
    }

    public void setLastModified(final Instant lastModified) {
        this.lastModified = lastModified;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(final LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDate getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(final LocalDate validUntil) {
        this.validUntil = validUntil;
    }

    public String getRawText() {
        return rawText;
    }

    public void setRawText(final String rawText) {
        this.rawText = rawText;
    }

    public String getFormattedText() {
        return formattedText;
    }

    public void setFormattedText(final String formattedText) {
        this.formattedText = formattedText;
    }
}
