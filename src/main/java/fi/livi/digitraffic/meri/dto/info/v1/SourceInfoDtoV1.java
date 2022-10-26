package fi.livi.digitraffic.meri.dto.info.v1;

import java.time.Duration;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonPropertyOrder({ "name", "source" })
@Schema(description = "Maintenance tracking domain")
public interface SourceInfoDtoV1 {

    @NotNull
    String getId();

    String getSource();

    Duration getUpdateInterval();
}
