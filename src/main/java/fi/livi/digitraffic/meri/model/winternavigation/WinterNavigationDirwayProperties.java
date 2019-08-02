package fi.livi.digitraffic.meri.model.winternavigation;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.model.geojson.Properties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonPropertyOrder({ "issueTime", "issuerCode", "issuerName", "validUntil" })
@ApiModel(parent = Properties.class)
public class WinterNavigationDirwayProperties extends Properties {

    @ApiModelProperty(value = "Time when the dirway was issued")
    public final ZonedDateTime issueTime;

    @ApiModelProperty(value = "Issuer code, e.g. URH")
    public final String issuerCode;

    @ApiModelProperty(value = "Issuer name, e.g. URHO")
    public final String issuerName;

    @ApiModelProperty(value = "Date and time until this dirway was in effect")
    public final ZonedDateTime validUntil;

    public WinterNavigationDirwayProperties(final ZonedDateTime issueTime,
                                            final String issuerCode,
                                            final String issuerName,
                                            final ZonedDateTime validUntil) {
        this.issueTime = issueTime;
        this.issuerCode = issuerCode;
        this.issuerName = issuerName;
        this.validUntil = validUntil;
    }
}
