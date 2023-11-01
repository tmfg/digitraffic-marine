package fi.livi.digitraffic.meri.model.portnet;

import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

@Entity
@DynamicUpdate
public class PortArea {
    @EmbeddedId
    private PortAreaKey portAreaKey;

    private String portAreaName;

    @Column(name = "wgs84_lat")
    private Double wgs84Lat;

    @Column(name = "wgs84_long")
    private Double wgs84Long;

    public String getPortAreaName() {
        return portAreaName;
    }

    public void setPortAreaName(final String portAreaName) {
        this.portAreaName = portAreaName;
    }

    public PortAreaKey getPortAreaKey() {
        return portAreaKey;
    }

    public void setPortAreaKey(final PortAreaKey portAreaKey) {
        this.portAreaKey = portAreaKey;
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
}
