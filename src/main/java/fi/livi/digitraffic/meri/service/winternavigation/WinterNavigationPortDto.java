package fi.livi.digitraffic.meri.service.winternavigation;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WinterNavigationPortDto {

    public final WinterNavigationPortInfoDto portInfo;

    public final List<WinterNavigationPortRestrictionDto> restrictions;

    public WinterNavigationPortDto(@JsonProperty("portInfo") final WinterNavigationPortInfoDto portInfo,
                                   @JsonProperty("restrictions") final List<WinterNavigationPortRestrictionDto> restrictions) {
        this.portInfo = portInfo;
        this.restrictions = restrictions;
    }
}
