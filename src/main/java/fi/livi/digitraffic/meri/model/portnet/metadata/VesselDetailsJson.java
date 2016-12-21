package fi.livi.digitraffic.meri.model.portnet.metadata;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="Vessel details", value = "VesselDetails")
@JsonPropertyOrder({ "vesselId", "mmsi", "name", "namePrefix", "imoLloyds", "radioCallSign", "radioCallSignType", "updateTimestamp",
                     "dataSource", "vesselConstruction", "vesselDimensions", "vesselRegistration", "vesselSystem" })
public interface VesselDetailsJson {

    Long getVesselId();

    @ApiModelProperty(value = "Ship MMSI (Maritime Mobile Service Identity)")
    Integer getMmsi();

    @ApiModelProperty(value = "Vessel name")
    String getName();

    @ApiModelProperty(value = "Vessel name prefix")
    String getNamePrefix();

    @ApiModelProperty(value = "Ship IMO / Lloyds")
    Integer getImoLloyds();

    @ApiModelProperty(value = "Ship radio call sign")
    String getRadioCallSign();

    @ApiModelProperty(value = "Ship radio call sign type")
    String getRadioCallSignType();

    @ApiModelProperty(value = "Timestamp of last vessel metadata update")
    Timestamp getUpdateTimestamp();

    @ApiModelProperty(value = "Data source")
    String getDataSource();

    @ApiModelProperty(value = "Vessel construction information")
    @Value("#{target.vesselConstruction}")
    VesselConstructionJson getVesselConstruction();

    @ApiModelProperty(value = "Vessel dimension information")
    @Value("#{target.vesselDimensions}")
    VesselDimensionsJson getVesselDimensions();

    @ApiModelProperty(value = "Vessel registration information")
    @Value("#{target.vesselRegistration}")
    VesselRegistrationJson getVesselRegistration();

    @ApiModelProperty(value = "Vessel system information")
    @Value("#{target.vesselSystem}")
    VesselSystemJson getVesselSystem();
}
