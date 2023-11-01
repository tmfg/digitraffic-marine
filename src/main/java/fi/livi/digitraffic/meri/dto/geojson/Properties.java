package fi.livi.digitraffic.meri.dto.geojson;

import java.io.Serializable;
import java.time.Instant;

import fi.livi.digitraffic.meri.dto.LastModifiedSupport;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "GeoJSON Properties object")
public abstract class Properties implements Serializable, LastModifiedSupport {

    private final Instant dataUpdatedTime;

    protected Properties(final Instant dataUpdatedTime) {
        this.dataUpdatedTime = dataUpdatedTime;
    }

    @Override
    public Instant getLastModified() {
        return dataUpdatedTime;
    }
}
