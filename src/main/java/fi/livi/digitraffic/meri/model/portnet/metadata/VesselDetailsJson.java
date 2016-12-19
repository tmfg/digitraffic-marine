package fi.livi.digitraffic.meri.model.portnet.metadata;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModel;

@ApiModel(description="Vessel details")
@JsonPropertyOrder({ "vesselId", "mmsi", "name", "namePrefix", "imoLloyds", "radioCallSign", "radioCallSignType", "updateTimestamp",
                     "dataSource", "vesselConstruction", "vesselDimensions", "vesselRegistration", "vesselSystem" })
public interface VesselDetailsJson {

    Long getVesselId();

    Integer getMmsi();

    String getName();

    String getNamePrefix();

    Integer getImoLloyds();

    String getRadioCallSign();

    String getRadioCallSignType();

    Timestamp getUpdateTimestamp();

    String getDataSource();

    @Value("#{target.vesselConstruction}")
    VesselConstructionJson getVesselConstruction();

    @Value("#{target.vesselDimensions}")
    VesselDimensionsJson getVesselDimensions();

    @Value("#{target.vesselRegistration}")
    VesselRegistrationJson getVesselRegistration();

    @Value("#{target.vesselSystem}")
    VesselSystemJson getVesselSystem();
}
