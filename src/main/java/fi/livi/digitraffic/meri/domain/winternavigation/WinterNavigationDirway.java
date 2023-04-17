package fi.livi.digitraffic.meri.domain.winternavigation;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import fi.livi.digitraffic.meri.domain.ReadOnlyCreatedAndModifiedFields;

@Entity
public class WinterNavigationDirway extends ReadOnlyCreatedAndModifiedFields {

    @Id
    private String name;

    private ZonedDateTime issueTime;

    private String issuerCode;

    private String issuerName;

    private ZonedDateTime validUntil;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "winterNavigationDirwayPointPK.dirwayName", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy(value = "winterNavigationDirwayPointPK.orderNumber")
    private List<WinterNavigationDirwayPoint> dirwayPoints = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(ZonedDateTime issueTime) {
        this.issueTime = issueTime;
    }

    public String getIssuerCode() {
        return issuerCode;
    }

    public void setIssuerCode(String issuerCode) {
        this.issuerCode = issuerCode;
    }

    public String getIssuerName() {
        return issuerName;
    }

    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }

    public ZonedDateTime getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(ZonedDateTime validUntil) {
        this.validUntil = validUntil;
    }

    public List<WinterNavigationDirwayPoint> getDirwayPoints() {
        return dirwayPoints;
    }

}
