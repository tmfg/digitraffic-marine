package fi.livi.digitraffic.meri.service.winternavigation.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PortDto {

    public final PortInfoDto portInfo;

    public final List<PortRestrictionDto> restrictions;

    public PortDto(@JsonProperty("portInfo") final PortInfoDto portInfo,
                   @JsonProperty("restrictions") final List<PortRestrictionDto> restrictions) {
        this.portInfo = portInfo;
        this.restrictions = restrictions;
    }
}
