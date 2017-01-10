package fi.livi.digitraffic.meri.model.portnet.metadata;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BerthProperties {
    @ApiModelProperty(value = "Berth name", required = true)
    public final String berthName;

    public BerthProperties(final String berthName) {
        this.berthName = berthName;
    }
}
