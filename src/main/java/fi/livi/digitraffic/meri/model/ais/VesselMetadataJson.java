package fi.livi.digitraffic.meri.model.ais;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="Vessel metadata model")
@JsonSerialize(as = VesselMetadataJson.class)
public interface VesselMetadataJson {
    @ApiModelProperty(value = "Maritime Mobile Service Identity", required = true)
    int getMmsi();

    @ApiModelProperty(value = "Name of the vessel, maximum 20 characters using 6-bit ASCII", required = true)
    String getName();

    @ApiModelProperty(value = "Vessel type", allowableValues = "range[0,255]", required = true)
    int getShipType();

    @ApiModelProperty(value = "Reference point for reported position dimension A", required = true)
    long getReferencePointA();

    @ApiModelProperty(value = "Reference point for reported position dimension B", required = true)
    long getReferencePointB();

    @ApiModelProperty(value = "Reference point for reported position dimension C", required = true)
    long getReferencePointC();

    @ApiModelProperty(value = "Reference point for reported position dimension D", required = true)
    long getReferencePointD();

    @ApiModelProperty(value = "Type of electronic position fixing device: 0 = undefined (default)\n"
            + "1 = GPS\n"
            + "2 = GLONASS\n"
            + "3 = combined GPS/GLONASS\n"
            + "4 = Loran-C\n"
            + "5 = Chayka\n"
            + "6 = integrated navigation system\n"
            + "7 = surveyed\n"
            + "8 = Galileo,\n"
            + "9-14 = not used\n"
            + "15 = internal GNSS", allowableValues = "range[0,15]", required = true)
    int getPosType();

    @ApiModelProperty(value = "Maximum present static draught in 1/10m, 255 = draught 25.5 m or greater, 0 = not available (default)", allowableValues = "range[0,255]", required = true)
    int getDraught();

    @ApiModelProperty(value = "Vessel International Maritime Organization (IMO) number", required = true)
    int getImo();

    @ApiModelProperty(value = "Call sign, maximum 7 6-bit ASCII characters", required = true)
    String getCallSign();

    @ApiModelProperty(value = "Estimated time of arrival; MMDDHHMM UTC\n"
            + "Bits 19-16: month; 1-12; 0 = not available = default\n"
            + "Bits 15-11: day; 1-31; 0 = not available = default\n"
            + "Bits 10-6: hour; 0-23; 24 = not available = default\n"
            + "Bits 5-0: minute; 0-59; 60 = not available = default\n"
            + "For SAR aircraft, the use of this field may be decided by the responsible administration", required = true)
    long getEta();

    @ApiModelProperty(value = "Record timestamp in milliseconds from Unix epoch", required = true)
    long getTimestamp();

    @ApiModelProperty(value = "Destination, maximum 20 characters using 6-bit ASCII", required = true)
    String getDestination();
}
