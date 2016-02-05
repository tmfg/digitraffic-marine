package fi.livi.digitraffic.meri.model;

import jdk.nashorn.internal.ir.annotations.Immutable;

@Immutable
public class VesselMetadata {
    private final int mmsi;
    private final String name;

    public VesselMetadata(final int mmsi, final String name) {
        this.mmsi = mmsi;
        this.name = name;
    }

    public int getMmsi() {
        return mmsi;
    }

    public String getName() {
        return name;
    }
}
