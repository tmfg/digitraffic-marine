package fi.livi.digitraffic.meri.model.portnet.metadata;

import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="Berth")
@JsonPropertyOrder({"locode", "portAreaCode", "berthCode", "berthName"})
public interface BerthJson {
    @ApiModelProperty(value = "Locode", required = true)
    @Value("#{target.berthKey.locode}")
    String getLocode();

    @ApiModelProperty(value = "Port area code", required = true)
    @Value("#{target.berthKey.portAreaCode}")
    String getPortAreaCode();

    @ApiModelProperty(value = "Berth code", required = true)
    @Value("#{target.berthKey.berthCode}")
    String getBerthCode();

    @ApiModelProperty(value = "Berth name", required = true)
    String getBerthName();
}
