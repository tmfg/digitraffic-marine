package fi.livi.digitraffic.meri.model.winternavigation;

import java.util.List;

public class WinterNavigationShipProperties {

    public final String vesselSource;

    public final String mmsi;

    public final String name;

    public final String callSign;

    public final String imo;

    public final Double dwt;

    public final Double length;

    public final Double width;

    public final Double aisLength;

    public final Double aisWidth;

    public final String dimensions;

    public final Double nominalDraught;

    public final String iceClass;

    public final String natCode;

    public final String nationality;

    public final String shipType;

    public final Integer aisShipType;

    public final ShipStateProperty shipState;

    public final ShipVoyageProperty shipVoyage;

    public final List<ShipActivityProperty> shipActivities;

    public final List<ShipPlannedActivityProperty> shipPlannedActivities;

    public WinterNavigationShipProperties(String vesselSource, String mmsi, String name, String callSign, String imo, Double dwt, Double length,
                                          Double width, Double aisLength, Double aisWidth, String dimensions, Double nominalDraught,
                                          String iceClass, String natCode, String nationality, String shipType, Integer aisShipType,
                                          ShipStateProperty shipState, ShipVoyageProperty shipVoyage, List<ShipActivityProperty> shipActivities,
                                          List<ShipPlannedActivityProperty> shipPlannedActivities) {
        this.vesselSource = vesselSource;
        this.mmsi = mmsi;
        this.name = name;
        this.callSign = callSign;
        this.imo = imo;
        this.dwt = dwt;
        this.length = length;
        this.width = width;
        this.aisLength = aisLength;
        this.aisWidth = aisWidth;
        this.dimensions = dimensions;
        this.nominalDraught = nominalDraught;
        this.iceClass = iceClass;
        this.natCode = natCode;
        this.nationality = nationality;
        this.shipType = shipType;
        this.aisShipType = aisShipType;
        this.shipState = shipState;
        this.shipVoyage = shipVoyage;
        this.shipActivities = shipActivities;
        this.shipPlannedActivities = shipPlannedActivities;
    }
}
