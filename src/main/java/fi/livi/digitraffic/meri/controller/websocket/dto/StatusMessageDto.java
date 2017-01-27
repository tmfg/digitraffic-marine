package fi.livi.digitraffic.meri.controller.websocket.dto;

import fi.livi.digitraffic.meri.model.ais.StatusMessage;

public class StatusMessageDto extends WebSocketDto<StatusMessage> {

    public StatusMessageDto(StatusMessage data) {
        super(WebSocketDtoType.STATUS, data);
    }
}
