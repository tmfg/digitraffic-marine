package fi.livi.digitraffic.meri.model.portnet;

import io.swagger.annotations.ApiModel;

@ApiModel(description="Port area")
public interface PortAreaJson {
    String getLocode();
    String getPortAreaCode();
    String getPortAreaName();
}
