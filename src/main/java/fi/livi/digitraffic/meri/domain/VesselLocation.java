package fi.livi.digitraffic.meri.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fi.livi.digitraffic.meri.model.AISMessage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Immutable;

@JsonIgnoreProperties("id")
@Entity
@Immutable
@ApiModel(description="Vessel location model")
public class VesselLocation {

    @Id
    @SequenceGenerator(name = "VL_SEQ", sequenceName = "SEQ_VESSEL_LOCATION")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VL_SEQ")
    @ApiModelProperty(value = "Unique identifier", required = true)
    private long id;

    @ApiModelProperty(value = "Maritime Mobile Service Identity", required = true)
    private int mmsi;

    @ApiModelProperty(value = "X-coordinate (longitude) in ETRS89 (EUREF-FIN)", required = true)
    private double x;

    @ApiModelProperty(value = "Y-coordinate (latitude) in ETRS89 (EUREF-FIN)", required = true)
    private double y;

    @ApiModelProperty(value = "Speed in knots", required = true)
    private double speed;

    @ApiModelProperty(value = "Course in TODO", required = true)
    private double course;

    @ApiModelProperty(value = "Location record timestamp in milliseconds from Unix epoch.", required = true)
    private long timestamp;

    public  VesselLocation() {
        // for hibernate
    }

    public VesselLocation(final AISMessage ais) {
        this.mmsi = ais.attributes.mmsi;
        this.x = ais.geometry.x;
        this.y = ais.geometry.y;
        this.speed = ais.attributes.speed;
        this.course = ais.attributes.course;
        this.timestamp = ais.attributes.timestamp;
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(final double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(final double y) {
        this.y = y;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(final double speed) {
        this.speed = speed;
    }

    public double getCourse() {
        return course;
    }

    public void setCourse(final double course) {
        this.course = course;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final long timestamp) {
        this.timestamp = timestamp;
    }

    public int getMmsi() {
        return mmsi;
    }

    public void setMmsi(final int mmsi) {
        this.mmsi = mmsi;
    }
}