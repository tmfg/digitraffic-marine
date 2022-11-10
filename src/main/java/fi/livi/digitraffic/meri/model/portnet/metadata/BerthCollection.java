package fi.livi.digitraffic.meri.model.portnet.metadata;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fi.livi.digitraffic.meri.dto.data.v1.DataUpdatedSupportV1;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder({
        "dateUpdatedTime",
        "berths",
})
@Schema(description = "Berths")
public class BerthCollection implements DataUpdatedSupportV1 {

    final private Instant dataUpdatedTime;

    @Schema(description = "Berths")
    final public List<BerthProperties> berths;

    public BerthCollection(final List<BerthProperties> berths, final Instant dataUpdatedTime) {
        this.berths = berths;
        this.dataUpdatedTime = dataUpdatedTime;
    }

    @Override
    public Instant getDataUpdatedTime() {
        return dataUpdatedTime;
    }
}
