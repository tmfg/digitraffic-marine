package fi.livi.digitraffic.meri.model.portnet.metadata;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModel;

@ApiModel(description="Vessel dimensions")
@JsonPropertyOrder({ "vesselId", "tonnageCertificateIssuer", "dateOfIssue", "grossTonnage", "netTonnage", "deathWeight", "length",
                     "overallLength", "height", "breadth", "draught", "maxSpeed", "enginePower", "totalPower", "maxPersons",
                     "maxPassengers", "keelDate" })
public interface VesselDimensionsJson {

    Long getVesselId();

    String getTonnageCertificateIssuer();

    Timestamp getDateOfIssue();

    Integer getGrossTonnage();

    Integer getNetTonnage();

    Integer getDeathWeight();

    Double getLength();

    Double getOverallLength();

    Double getHeight();

    Double getBreadth();

    Double getDraught();

    Double getMaxSpeed();

    String getEnginePower();

    Integer getTotalPower();

    Integer getMaxPersons();

    Integer getMaxPassengers();

    Timestamp getKeelDate();
}
