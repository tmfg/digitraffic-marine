package fi.livi.digitraffic.meri.model;

import java.time.LocalDateTime;

public interface FlywayVersion {
    String getVersion();
    LocalDateTime getInstalledOn();
    Boolean getSuccess();
}
