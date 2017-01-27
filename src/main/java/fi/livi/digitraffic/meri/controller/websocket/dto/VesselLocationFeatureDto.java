package fi.livi.digitraffic.meri.controller.websocket.dto;

import fi.livi.digitraffic.meri.model.ais.VesselLocationFeature;

public class VesselLocationFeatureDto extends WebSocketDto<VesselLocationFeature> {

    public VesselLocationFeatureDto(VesselLocationFeature data) {
        super(WebSocketDtoType.VESSEL_LOCATION, data);
    }
}
