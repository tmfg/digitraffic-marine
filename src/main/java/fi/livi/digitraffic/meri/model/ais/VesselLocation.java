package fi.livi.digitraffic.meri.model.ais;

import org.hibernate.annotations.DynamicUpdate;

import fi.livi.digitraffic.meri.dto.ais.external.AISMessage;
import fi.livi.digitraffic.meri.model.ReadOnlyCreatedAndModifiedFields;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
@DynamicUpdate
public class VesselLocation extends ReadOnlyCreatedAndModifiedFields {
    @Id
    private int mmsi;

    private double x;

    private double y;

    private double sog;

    private double cog;

    private int navStat;

    private int rot;

    private boolean posAcc;

    private boolean raim;

    private Integer heading;

    private long timestamp;

    @Column(name = "timestamp_ext")
    private long timestampExternal;

    public  VesselLocation() {
        // for hibernate
    }

    public VesselLocation(final AISMessage ais) {
        this.mmsi = ais.attributes.mmsi;
        this.timestampExternal = ais.attributes.timestampExternal;
        this.x = ais.geometry.x;
        this.y = ais.geometry.y;
        this.sog = ais.attributes.sog;
        this.cog = ais.attributes.cog;
        this.navStat = ais.attributes.navStat;
        this.rot = ais.attributes.rot;
        this.raim = ais.attributes.raim == 1;
        this.timestamp = ais.attributes.timestamp;
        this.posAcc = ais.attributes.posAcc == 1;
        this.heading = ais.attributes.heading;
    }

    public int getMmsi() {
        return mmsi;
    }

    public void setMmsi(final int mmsi) {
        this.mmsi = mmsi;
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

    public double getSog() {
        return sog;
    }

    public void setSog(final double sog) {
        this.sog = sog;
    }

    public double getCog() {
        return cog;
    }

    public void setCog(final double cog) {
        this.cog = cog;
    }

    public int getNavStat() {
        return navStat;
    }

    public void setNavStat(final int navStat) {
        this.navStat = navStat;
    }

    public int getRot() {
        return rot;
    }

    public void setRot(final int rot) {
        this.rot = rot;
    }

    public boolean isPosAcc() {
        return posAcc;
    }

    public void setPosAcc(final boolean posAcc) {
        this.posAcc = posAcc;
    }

    public boolean isRaim() {
        return raim;
    }

    public void setRaim(final boolean raim) {
        this.raim = raim;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestampExternal() {
        return timestampExternal;
    }

    public void setTimestampExternal(final long timestampExternal) {
        this.timestampExternal = timestampExternal;
    }

    public Integer getHeading() {
        return heading;
    }

    public void setHeading(final Integer heading) {
        this.heading = heading;
    }
}
