package fi.livi.digitraffic.meri.model.portnet;

import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
@DynamicUpdate
public class SsnLocation {
    @Id
    private String locode;

    private String locationName;

    private String country;

    @Column(name = "wgs84_lat")
    private Double wgs84Lat;

    @Column(name = "wgs84_long")
    private Double wgs84Long;

    public String getLocode() {
        return locode;
    }

    public void setLocode(final String locode) {
        this.locode = locode;
    }

    public Double getWgs84Lat() {
        return wgs84Lat;
    }

    public void setWgs84Lat(final Double wgs84Lat) {
        this.wgs84Lat = wgs84Lat;
    }

    public Double getWgs84Long() {
        return wgs84Long;
    }

    public void setWgs84Long(final Double wgs84Long) {
        this.wgs84Long = wgs84Long;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(final String locationName) {
        this.locationName = locationName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }
}
