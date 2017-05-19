package fi.livi.digitraffic.meri.service.winternavigation;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WinterNavigationShipDataDto {

    public final String name;

    public final String callSign;

    public final String imo;

    public final String mmsi;

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

    public WinterNavigationShipDataDto(@JsonProperty("name") final String name,
                                       @JsonProperty("callSign") final String callSign,
                                       @JsonProperty("imo") final String imo,
                                       @JsonProperty("mmsi") final String mmsi,
                                       @JsonProperty("dwt") final Double dwt,
                                       @JsonProperty("length") final Double length,
                                       @JsonProperty("width") final Double width,
                                       @JsonProperty("ais_length") final Double aisLength,
                                       @JsonProperty("ais_width") final Double aisWidth,
                                       @JsonProperty("dimensions") final String dimensions,
                                       @JsonProperty("nominal_draught") final Double nominalDraught,
                                       @JsonProperty("iceclass") final String iceClass,
                                       @JsonProperty("natcode") final String natCode,
                                       @JsonProperty("nationality") final String nationality,
                                       @JsonProperty("ship_type") final String shipType,
                                       @JsonProperty("ais_ship_type") final Integer aisShipType) {
        this.name = name;
        this.callSign = callSign;
        this.imo = imo;
        this.mmsi = mmsi;
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
    }
}
