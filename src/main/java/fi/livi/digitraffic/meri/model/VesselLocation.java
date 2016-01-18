package fi.livi.digitraffic.meri.model;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer;

public class VesselLocation {
    @JsonSerialize(using = OffsetDateTimeSerializer.class)
    private final OffsetDateTime time;
    private String id;

    public VesselLocation(final OffsetDateTime time, final String id) {
        this.time = time;
        this.id = id;
    }

    public OffsetDateTime getTime() {
        return time;
    }

    public String getId() {
        return id;
    }
}
