package fi.livi.digitraffic.meri.controller.websocket.dto;

import fi.livi.digitraffic.meri.domain.ais.VesselMetadata;

public class VesselMetadataDto extends WebSocketDto<VesselMetadata> {

    public VesselMetadataDto(VesselMetadata data) {
        super(WebSocketDtoType.VESSEL_METADATA, data);
    }
}
