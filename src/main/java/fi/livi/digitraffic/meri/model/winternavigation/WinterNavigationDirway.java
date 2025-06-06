package fi.livi.digitraffic.meri.model.winternavigation;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import fi.livi.digitraffic.meri.model.ReadOnlyCreatedAndModifiedFields;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;

@Entity
public class WinterNavigationDirway extends ReadOnlyCreatedAndModifiedFields {

    @Id
    private String name;

    private Instant issueTime;

    private String issuerCode;

    private String issuerName;

    private Instant validUntil;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "winterNavigationDirwayPointPK.dirwayName", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy(value = "winterNavigationDirwayPointPK.orderNumber")
    private final List<WinterNavigationDirwayPoint> dirwayPoints = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Instant getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(final Instant issueTime) {
        this.issueTime = issueTime;
    }

    public String getIssuerCode() {
        return issuerCode;
    }

    public void setIssuerCode(final String issuerCode) {
        this.issuerCode = issuerCode;
    }

    public String getIssuerName() {
        return issuerName;
    }

    public void setIssuerName(final String issuerName) {
        this.issuerName = issuerName;
    }

    public Instant getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(final Instant validUntil) {
        this.validUntil = validUntil;
    }

    public List<WinterNavigationDirwayPoint> getDirwayPoints() {
        return dirwayPoints;
    }

}
