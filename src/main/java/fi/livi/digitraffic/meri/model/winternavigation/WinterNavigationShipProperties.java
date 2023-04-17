package fi.livi.digitraffic.meri.model.winternavigation;

import java.time.Instant;
import java.util.List;

import fi.livi.digitraffic.meri.model.geojson.Properties;
import io.swagger.v3.oas.annotations.media.Schema;

public class WinterNavigationShipProperties extends Properties {

    @Schema(description = "Vessel identification code. Equals IMO-{IMO-code} when vessel IMO is present. " +
        "Otherwise MMSI-{MMSI-code} (Maritime Mobile Service Identity).", required = true)
    public final String vesselId;

    @Schema(description = "Data source identifier")
    public final String vesselSource;

    @Schema(description = "Ship Maritime Mobile Service Identity")
    public final String mmsi;

    @Schema(description = "Ship name")
    public final String name;

    @Schema(description = "Radio call sign")
    public final String callSign;

    @Schema(description = "IMO unique ship identifier")
    public final String imo;

    @Schema(description = "Deadweight")
    public final Double dwt;

    @Schema(description = "Ship length from official ship register")
    public final Double length;

    @Schema(description = "Ship width from official ship register")
    public final Double width;

    @Schema(description = "Ship length as entered into AIS device")
    public final Double aisLength;

    @Schema(description = "Ship width as entered into AIS device")
    public final Double aisWidth;

    @Schema(description = "Pre-formatted ship dimensions, e.g. \"13 x 80m\"")
    public final String dimensions;

    @Schema(description = "Nominal draught from ship register")
    public final Double nominalDraught;

    @Schema(description = "IAS, IA, IB, IC, II, empty")
    public final String iceClass;

    @Schema(description = "International 2-letter code")
    public final String natCode;

    @Schema(description = "Nationality name")
    public final String nationality;

    @Schema(description = "Ship type")
    public final String shipType;

    @Schema(description = "AIS ship type")
    public final Integer aisShipType;

    @Schema(description = "Dynamically observed navigational status of the ship, originating either from " +
                              "the AIS network or from manual input by users. " +
                              "In IBNet terms, this element might also be called SHIPPASSAGE, POSITION or OBSERVATION.")
    public final ShipStateProperty shipState;

    @Schema(description = "Currently known and validated user-given information on the vessel's port-of-destination, " +
                              "port-of-residence, port-of-origin and/or previous and next ports. " +
                              "Note that this information is available in IBNet only if the ship has current or a " +
                              "future port visit to some Baltic Area port. " +
                              "In the general case, each event type (ETA, ATA, ETD, ATD) with their times, " +
                              "sources and timestamps, should be represented distinctly.")
    public final ShipVoyageProperty shipVoyage;

    @Schema(description = "Represents the ship state (or activity) given by icebreaker, VTS operator, " +
                              "winter navigation administrator, port authority or representative.")
    public final List<ShipActivityProperty> shipActivities;

    @Schema(description = "Planned future assistance by an icebreaker or an icebreaker-specific activity " +
                              "like bunkering or crew change.")
    public final List<ShipPlannedActivityProperty> shipPlannedActivities;

    public WinterNavigationShipProperties(
        final String vesselId,
        final String vesselSource,
        final String mmsi,
        final String name,
        final String callSign,
        final String imo,
        final Double dwt,
        final Double length,
        final Double width,
        final Double aisLength,
        final Double aisWidth,
        final String dimensions,
        final Double nominalDraught,
        final String iceClass,
        final String natCode,
        final String nationality,
        final String shipType,
        final Integer aisShipType,
        final ShipStateProperty shipState,
        final ShipVoyageProperty shipVoyage,
        final List<ShipActivityProperty> shipActivities,
        final List<ShipPlannedActivityProperty> shipPlannedActivities,
        final Instant dataUpdatedTime) {
        super(dataUpdatedTime);
        this.vesselId = vesselId;
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
