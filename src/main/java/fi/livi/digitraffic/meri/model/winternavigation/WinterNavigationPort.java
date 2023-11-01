package fi.livi.digitraffic.meri.model.winternavigation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.DynamicUpdate;

import fi.livi.digitraffic.meri.model.ReadOnlyCreatedAndModifiedFields;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;

@Entity
@DynamicUpdate
public class WinterNavigationPort extends ReadOnlyCreatedAndModifiedFields {

    @Id
    private String locode;

    private String name;

    private String nationality;

    private Double longitude;

    private Double latitude;

    private String seaArea;

    private Date obsoleteDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "locode", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy(value = "orderNumber")
    private final List<PortRestriction> portRestrictions = new ArrayList<>();

    public String getLocode() {
        return locode;
    }

    public void setLocode(final String locode) {
        this.locode = locode;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(final String nationality) {
        this.nationality = nationality;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(final Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(final Double latitude) {
        this.latitude = latitude;
    }

    public String getSeaArea() {
        return seaArea;
    }

    public void setSeaArea(final String seaArea) {
        this.seaArea = seaArea;
    }

    public List<PortRestriction> getPortRestrictions() {
        return portRestrictions;
    }

    public Date getObsoleteDate() {
        return obsoleteDate;
    }

    public void setObsoleteDate(final Date obsoleteDate) {
        this.obsoleteDate = obsoleteDate;
    }
}
