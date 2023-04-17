package fi.livi.digitraffic.meri.domain.winternavigation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.annotations.DynamicUpdate;

import fi.livi.digitraffic.meri.domain.ReadOnlyCreatedAndModifiedFields;

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
    private List<PortRestriction> portRestrictions = new ArrayList<>();

    public String getLocode() {
        return locode;
    }

    public void setLocode(String locode) {
        this.locode = locode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getSeaArea() {
        return seaArea;
    }

    public void setSeaArea(String seaArea) {
        this.seaArea = seaArea;
    }

    public List<PortRestriction> getPortRestrictions() {
        return portRestrictions;
    }

    public Date getObsoleteDate() {
        return obsoleteDate;
    }

    public void setObsoleteDate(Date obsoleteDate) {
        this.obsoleteDate = obsoleteDate;
    }
}
