package fi.livi.digitraffic.meri.service.winternavigation.dto;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

public class PortsDto {

    public final ZonedDateTime dataValidTime;

    public final ZonedDateTime dataQueryTime;

    public final List<PortDto> ports = new ArrayList<>();

    public PortsDto(@JsonProperty("dataValidTime") final ZonedDateTime dataValidTime,
                    @JsonProperty("dataQueryTime") final ZonedDateTime dataQueryTime) {
        this.dataValidTime = dataValidTime;
        this.dataQueryTime = dataQueryTime;
    }

    @JsonSetter(value = "port")
    public void setPort(PortDto port) {
        this.ports.add(port);
    }
}
