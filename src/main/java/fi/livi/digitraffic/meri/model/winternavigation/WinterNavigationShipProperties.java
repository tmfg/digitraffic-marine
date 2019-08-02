package fi.livi.digitraffic.meri.model.winternavigation;

import java.util.List;

import fi.livi.digitraffic.meri.model.geojson.Properties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(parent = Properties.class)
public class WinterNavigationShipProperties extends Properties {

    @ApiModelProperty(value = "Data source identifier")
    public final String vesselSource;

    @ApiModelProperty(value = "Ship Maritime Mobile Service Identity")
    public final String mmsi;

    @ApiModelProperty(value = "Ship name")
    public final String name;

    @ApiModelProperty(value = "Radio call sign")
    public final String callSign;

    @ApiModelProperty(value = "IMO unique ship identifier")
    public final String imo;

    @ApiModelProperty(value = "Deadweight")
    public final Double dwt;

    @ApiModelProperty(value = "Ship length from official ship register")
    public final Double length;

    @ApiModelProperty(value = "Ship width from official ship register")
    public final Double width;

    @ApiModelProperty(value = "Ship length as entered into AIS device")
    public final Double aisLength;

    @ApiModelProperty(value = "Ship width as entered into AIS device")
    public final Double aisWidth;

    @ApiModelProperty(value = "Pre-formatted ship dimensions, e.g. \"13 x 80m\"")
    public final String dimensions;

    @ApiModelProperty(value = "Nominal draught from ship register")
    public final Double nominalDraught;

    @ApiModelProperty(value = "IAS, IA, IB, IC, II, empty")
    public final String iceClass;

    @ApiModelProperty(value = "International 2-letter code")
    public final String natCode;

    @ApiModelProperty(value = "Nationality name")
    public final String nationality;

    @ApiModelProperty(value = "Ship type")
    public final String shipType;

    @ApiModelProperty(value = "AIS ship type")
    public final Integer aisShipType;

    @ApiModelProperty(value = "Dynamically observed navigational status of the ship, originating either from " +
                              "the AIS network or from manual input by users. " +
                              "In IBNet terms, this element might also be called SHIPPASSAGE, POSITION or OBSERVATION.")
    public final ShipStateProperty shipState;

    @ApiModelProperty(value = "Currently known and validated user-given information on the vessel's port-of-destination, " +
                              "port-of-residence, port-of-origin and/or previous and next ports. " +
                              "Note that this information is available in IBNet only if the ship has current or a " +
                              "future port visit to some Baltic Area port. " +
                              "In the general case, each event type (ETA, ATA, ETD, ATD) with their times, " +
                              "sources and timestamps, should be represented distinctly.")
    public final ShipVoyageProperty shipVoyage;

    @ApiModelProperty(value = "Represents the ship state (or activity) given by icebreaker, VTS operator, " +
                              "winter navigation administrator, port authority or representative.")
    public final List<ShipActivityProperty> shipActivities;

    @ApiModelProperty(value = "Planned future assistance by an icebreaker or an icebreaker-specific activity " +
                              "like bunkering or crew change.")
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
