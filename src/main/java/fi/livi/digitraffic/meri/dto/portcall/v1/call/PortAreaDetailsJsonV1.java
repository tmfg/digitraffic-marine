package fi.livi.digitraffic.meri.dto.portcall.v1.call;

import java.math.BigDecimal;
import java.sql.Timestamp;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description="Port area details")
public interface PortAreaDetailsJsonV1 {

    @Schema(description = "Port area code")
    String getPortAreaCode();
    @Schema(description = "Port area name")
    String getPortAreaName();
    @Schema(description = "Berth code")
    String getBerthCode();
    @Schema(description = "Berth name")
    String getBerthName();

    @Schema(description = "Estimated time of arrival. Set by ship agent, port or pilot.")
    Timestamp getEta();
    @Schema(description = "Update time of ETA. Set by ship agent, port or pilot.")
    Timestamp getEtaTimestamp();
    @Schema(description = "Source of ETA")
    String getEtaSource();

    @Schema(description = "Estimated time of departure. Set by ship agent, port or pilot.")
    Timestamp getEtd();
    @Schema(description = "Update time of ETD. Set by ship agent, port or pilot.")
    Timestamp getEtdTimestamp();
    @Schema(description = "Source of ETD")
    String getEtdSource();

    @Schema(description = "Actual time of arrival. Set by ship agent, port or pilot.")
    Timestamp getAta();
    @Schema(description = "Update time of ATA. Set by ship agent, port or pilot.")
    Timestamp getAtaTimestamp();
    @Schema(description = "Source of ATA")
    String getAtaSource();

    @Schema(description = "Actual time of departure. Set by ship agent, port or pilot.")
    Timestamp getAtd();
    @Schema(description = "Update time of ATD. Set by ship agent, port or pilot.")
    Timestamp getAtdTimestamp();
    @Schema(description = "Source of ATD")
    String getAtdSource();

    @Schema(description = "Arrival draught")
    BigDecimal getArrivalDraught();
    @Schema(description = "Departure draught")
    BigDecimal getDepartureDraught();

    // no cargo-info (CargoInfoJson) here as it is not public info
}
