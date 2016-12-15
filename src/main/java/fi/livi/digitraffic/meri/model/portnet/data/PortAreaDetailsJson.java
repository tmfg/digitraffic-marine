package fi.livi.digitraffic.meri.model.portnet.data;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="Port area details")
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface PortAreaDetailsJson {

    @ApiModelProperty(value = "Port area code")
    String getPortAreaCode();
    @ApiModelProperty(value = "Port area name")
    String getPortAreaName();
    @ApiModelProperty(value = "Berth code")
    String getBerthCode();
    @ApiModelProperty(value = "Berth name")
    String getBerthName();

    @ApiModelProperty(value = "Estimated time of arrival. Set by ship agent, port or pilot.")
    Timestamp getEta();
    @ApiModelProperty(value = "Update time of ETA. Set by ship agent, port or pilot.")
    Timestamp getEtaTimestamp();
    @ApiModelProperty(value = "Source of ETA")
    String getEtaSource();

    @ApiModelProperty(value = "Estimated time of departure. Set by ship agent, port or pilot.")
    Timestamp getEtd();
    @ApiModelProperty(value = "Update time of ETD. Set by ship agent, port or pilot.")
    Timestamp getEtdTimestamp();
    @ApiModelProperty(value = "Source of ETD")
    String getEtdSource();

    @ApiModelProperty(value = "Actual time of arrival. Set by ship agent, port or pilot.")
    Timestamp getAta();
    @ApiModelProperty(value = "Update time of ATA. Set by ship agent, port or pilot.")
    Timestamp getAtaTimestamp();
    @ApiModelProperty(value = "Source of ATA")
    String getAtaSource();

    @ApiModelProperty(value = "Actual time of departure. Set by ship agent, port or pilot.")
    Timestamp getAtd();
    @ApiModelProperty(value = "Update time of ATD. Set by ship agent, port or pilot.")
    Timestamp getAtdTimestamp();
    @ApiModelProperty(value = "Source of ATD")
    String getAtdSource();

    @ApiModelProperty(value = "Arrival draught")
    BigDecimal getArrivalDraught();
    @ApiModelProperty(value = "Departure draught")
    BigDecimal getDepartureDraught();

    // no cargo-info here
}
