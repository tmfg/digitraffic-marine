package fi.livi.digitraffic.meri.dto.ais.v1;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import fi.livi.digitraffic.common.dto.LastModifiedSupport;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description="Vessel metadata model")
@JsonSerialize(as = VesselMetadataJsonV1.class)
public interface VesselMetadataJsonV1 extends LastModifiedSupport {
    @Schema(description = "Maritime Mobile Service Identity", requiredMode = Schema.RequiredMode.REQUIRED)
    int getMmsi();

    @Schema(description = "Name of the vessel, maximum 20 characters using 6-bit ASCII", requiredMode = Schema.RequiredMode.REQUIRED)
    String getName();

    @Schema(description = "Vessel's AIS ship type", minimum = "0", maximum = "255", requiredMode = Schema.RequiredMode.REQUIRED)
    int getShipType();

    @Schema(description = "Reference point for reported position dimension A", requiredMode = Schema.RequiredMode.REQUIRED)
    long getReferencePointA();

    @Schema(description = "Reference point for reported position dimension B", requiredMode = Schema.RequiredMode.REQUIRED)
    long getReferencePointB();

    @Schema(description = "Reference point for reported position dimension C", requiredMode = Schema.RequiredMode.REQUIRED)
    long getReferencePointC();

    @Schema(description = "Reference point for reported position dimension D", requiredMode = Schema.RequiredMode.REQUIRED)
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
            + "15 = internal GNSS", minimum = "0", maximum = "15", requiredMode = Schema.RequiredMode.REQUIRED)
    int getPosType();

    @Schema(description = "Maximum present static draught in 1/10m, 255 = draught 25.5 m or greater, 0 = not available (default)", minimum = "0", maximum = "255", requiredMode = Schema.RequiredMode.REQUIRED)
    int getDraught();

    @Schema(description = "Vessel International Maritime Organization (IMO) number", requiredMode = Schema.RequiredMode.REQUIRED)
    int getImo();

    @Schema(description = "Call sign, maximum 7 6-bit ASCII characters", requiredMode = Schema.RequiredMode.REQUIRED)
    String getCallSign();

    @Schema(description = "Estimated time of arrival; MMDDHHMM UTC\n"
            + "Bits 19-16: month; 1-12; 0 = not available = default\n"
            + "Bits 15-11: day; 1-31; 0 = not available = default\n"
            + "Bits 10-6: hour; 0-23; 24 = not available = default\n"
            + "Bits 5-0: minute; 0-59; 60 = not available = default\n"
            + "For SAR aircraft, the use of this field may be decided by the responsible administration", requiredMode = Schema.RequiredMode.REQUIRED)
    long getEta();

    @Schema(description = "Record timestamp in milliseconds from Unix epoch", requiredMode = Schema.RequiredMode.REQUIRED)
    long getTimestamp();

    @Schema(description = "Destination, maximum 20 characters using 6-bit ASCII", requiredMode = Schema.RequiredMode.REQUIRED)
    String getDestination();
}
