package fi.livi.digitraffic.meri.model.winternavigation;

import fi.livi.digitraffic.meri.model.ReadOnlyCreatedAndModifiedFields;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

@Entity
public class WinterNavigationDirwayPoint extends ReadOnlyCreatedAndModifiedFields {

    @EmbeddedId
    private WinterNavigationDirwayPointPK winterNavigationDirwayPointPK;

    private Double longitude;

    private Double latitude;

    private String seaArea;

    public WinterNavigationDirwayPoint() {
        // Empty
    }

    public WinterNavigationDirwayPoint(final WinterNavigationDirwayPointPK pk) {
        this.winterNavigationDirwayPointPK = pk;
    }
    public WinterNavigationDirwayPointPK getWinterNavigationDirwayPointPK() {
        return winterNavigationDirwayPointPK;
    }

    public Long getOrderNumber() {
        return winterNavigationDirwayPointPK.getOrderNumber();
    }

    public void setWinterNavigationDirwayPointPK(final WinterNavigationDirwayPointPK winterNavigationDirwayPointPK) {
        this.winterNavigationDirwayPointPK = winterNavigationDirwayPointPK;
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
}
