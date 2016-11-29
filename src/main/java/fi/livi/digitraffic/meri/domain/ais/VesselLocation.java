package fi.livi.digitraffic.meri.domain.ais;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicUpdate;

import fi.livi.digitraffic.meri.model.ais.AISMessage;
import fi.livi.digitraffic.meri.model.ais.VesselLocationJson;

@Entity
@DynamicUpdate
public class VesselLocation implements VesselLocationJson {
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

    @Override public int getMmsi() {
        return mmsi;
    }

    public void setMmsi(final int mmsi) {
        this.mmsi = mmsi;
    }

    @Override public double getX() {
        return x;
    }

    public void setX(final double x) {
        this.x = x;
    }

    @Override public double getY() {
        return y;
    }

    public void setY(final double y) {
        this.y = y;
    }

    @Override public double getSog() {
        return sog;
    }

    public void setSog(final double sog) {
        this.sog = sog;
    }

    @Override public double getCog() {
        return cog;
    }

    public void setCog(final double cog) {
        this.cog = cog;
    }

    @Override public int getNavStat() {
        return navStat;
    }

    public void setNavStat(final int navStat) {
        this.navStat = navStat;
    }

    @Override public int getRot() {
        return rot;
    }

    public void setRot(final int rot) {
        this.rot = rot;
    }

    @Override public boolean isPosAcc() {
        return posAcc;
    }

    public void setPosAcc(final boolean posAcc) {
        this.posAcc = posAcc;
    }

    @Override public boolean isRaim() {
        return raim;
    }

    public void setRaim(final boolean raim) {
        this.raim = raim;
    }

    @Override public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final long timestamp) {
        this.timestamp = timestamp;
    }

    @Override public long getTimestampExternal() {
        return timestampExternal;
    }

    public void setTimestampExternal(final long timestampExternal) {
        this.timestampExternal = timestampExternal;
    }

    @Override public Integer getHeading() {
        return heading;
    }

    public void setHeading(final Integer heading) {
        this.heading = heading;
    }
}
