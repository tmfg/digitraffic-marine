package fi.livi.digitraffic.meri.model.portnet;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;

@ApiModel(description="Port area details")
@JsonInclude(JsonInclude.Include.NON_NULL)
public interface PortAreaDetailsJson {
    String getPortAreaCode();
    String getPortAreaName();
    String getBerthCode();
    String getBerthName();

    Timestamp getEta();
    Timestamp getEtaTimestamp();
    String getEtaSource();

    Timestamp getEtd();
    Timestamp getEtdTimestamp();
    String getEtdSource();

    Timestamp getAta();
    Timestamp getAtaTimestamp();
    String getAtaSource();

    Timestamp getAtd();
    Timestamp getAtdTimestamp();
    String getAtdSource();

    BigDecimal getArrivalDraught();
    BigDecimal getDepartureDraught();

    Set<CargoInfoJson> getCargoInfo();
}
