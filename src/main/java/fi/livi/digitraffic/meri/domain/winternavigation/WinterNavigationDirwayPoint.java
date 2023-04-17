package fi.livi.digitraffic.meri.domain.winternavigation;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import fi.livi.digitraffic.meri.domain.ReadOnlyCreatedAndModifiedFields;

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

    public void setWinterNavigationDirwayPointPK(WinterNavigationDirwayPointPK winterNavigationDirwayPointPK) {
        this.winterNavigationDirwayPointPK = winterNavigationDirwayPointPK;
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
}
