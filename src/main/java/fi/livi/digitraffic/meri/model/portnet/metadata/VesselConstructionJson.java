package fi.livi.digitraffic.meri.model.portnet.metadata;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModel;

@ApiModel(description="Vessel construction")
@JsonPropertyOrder({ "vesselId", "vesselTypeCode", "vesselTypeName", "iceClassCode", "iceClassIssueDate", "iceClassIssuePlace",
                     "iceClassEndDate", "classificationSociety", "doubleBottom", "inertGasSystem", "ballastTank" })
public interface VesselConstructionJson {

    Long getVesselId();

    Integer getVesselTypeCode();

    String getVesselTypeName();

    String getIceClassCode();

    Timestamp getIceClassIssueDate();

    String getIceClassIssuePlace();

    Timestamp getIceClassEndDate();

    String getClassificationSociety();

    Boolean getDoubleBottom();

    Boolean getInertGasSystem();

    Boolean getBallastTank();
}
