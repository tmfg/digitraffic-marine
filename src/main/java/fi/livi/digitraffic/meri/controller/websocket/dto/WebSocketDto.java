package fi.livi.digitraffic.meri.controller.websocket.dto;

public abstract class WebSocketDto<T> {

    public final WebSocketDtoType type;

    public final T data;

    public WebSocketDto(WebSocketDtoType type, T data) {
        this.type = type;
        this.data = data;
    }
}
