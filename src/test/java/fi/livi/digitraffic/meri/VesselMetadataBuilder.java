package fi.livi.digitraffic.meri;

import java.time.Instant;

import fi.livi.digitraffic.meri.model.ais.VesselMetadataJson;

public class VesselMetadataBuilder {
    private final int mmsi;

    private int shipType = 0;

    public VesselMetadataBuilder(final int mmsi) {
        this.mmsi = mmsi;
    }

    public VesselMetadataBuilder shipType(final int shipType) {
        this.shipType = shipType;

        return this;
    }

    public VesselMetadataJson build() {
        return new VesselMetadataJson() {
            @Override
            public Instant getLastModified() {
                return Instant.now();
            }

            @Override public int getMmsi() {
                return mmsi;
            }

            @Override public String getName() {
                return null;
            }

            @Override public int getShipType() {
                return shipType;
            }

            @Override public long getReferencePointA() {
                return 0;
            }

            @Override public long getReferencePointB() {
                return 0;
            }

            @Override public long getReferencePointC() {
                return 0;
            }

            @Override public long getReferencePointD() {
                return 0;
            }

            @Override public int getPosType() {
                return 0;
            }

            @Override public int getDraught() {
                return 0;
            }

            @Override public int getImo() {
                return 0;
            }

            @Override public String getCallSign() {
                return null;
            }

            @Override public long getEta() {
                return 0;
            }

            @Override public long getTimestamp() {
                return 0;
            }

            @Override public String getDestination() {
                return null;
            }
        };
    }
}
