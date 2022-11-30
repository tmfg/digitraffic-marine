package fi.livi.digitraffic.meri.model.ais;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description="Vessel metadata model")
@JsonSerialize(as = VesselMetadataJson.class)
public interface VesselMetadataJson {
    @Schema(description = "Maritime Mobile Service Identity", required = true)
    int getMmsi();

    @Schema(description = "Name of the vessel, maximum 20 characters using 6-bit ASCII", required = true)
    String getName();

    @Schema(description = "Vessel's AIS ship type", minimum = "0", maximum = "255", required = true)
    int getShipType();

    @Schema(description = "Reference point for reported position dimension A", required = true)
    long getReferencePointA();

    @Schema(description = "Reference point for reported position dimension B", required = true)
    long getReferencePointB();

    @Schema(description = "Reference point for reported position dimension C", required = true)
    long getReferencePointC();

    @Schema(description = "Reference point for reported position dimension D", required = true)
    long getReferencePointD();

    @Schema(description = "Type of electronic position fixing device: 0 = undefined (default)\n"
            + "1 = GPS\n"
            + "2 = GLONASS\n"
            + "3 = combined GPS/GLONASS\n"
            + "4 = Loran-C\n"
            + "5 = Chayka\n"
            + "6 = integrated navigation system\n"
            + "7 = surveyed\n"
            + "8 = Galileo,\n"
            + "9-14 = not used\n"
            + "15 = internal GNSS", minimum = "0", maximum = "15", required = true)
    int getPosType();

    @Schema(description = "Maximum present static draught in 1/10m, 255 = draught 25.5 m or greater, 0 = not available (default)", minimum = "0", maximum = "255", required = true)
    int getDraught();

    @Schema(description = "Vessel International Maritime Organization (IMO) number", required = true)
    int getImo();

    @Schema(description = "Call sign, maximum 7 6-bit ASCII characters", required = true)
    String getCallSign();

    @Schema(description = "Estimated time of arrival; MMDDHHMM UTC\n"
            + "Bits 19-16: month; 1-12; 0 = not available = default\n"
            + "Bits 15-11: day; 1-31; 0 = not available = default\n"
            + "Bits 10-6: hour; 0-23; 24 = not available = default\n"
            + "Bits 5-0: minute; 0-59; 60 = not available = default\n"
            + "For SAR aircraft, the use of this field may be decided by the responsible administration", required = true)
    long getEta();

    @Schema(description = "Record timestamp in milliseconds from Unix epoch", required = true)
    long getTimestamp();

    @Schema(description = "Destination, maximum 20 characters using 6-bit ASCII", required = true)
    String getDestination();
}
