package fi.livi.digitraffic.meri.service.sse;

import static java.time.ZoneOffset.UTC;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import fi.livi.digitraffic.meri.domain.sse.SseReport;
import fi.livi.digitraffic.meri.model.sse.SseProperties;

public class SseReportBuilder {

    private final ZonedDateTime lastUpdate;

    public SseReportBuilder(final ZonedDateTime lastUpdate) {
        this.lastUpdate = lastUpdate.toInstant().atZone(UTC);
    }

    public List<SseReport> build() {

        final List<SseReport> reports = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            reports.add(new SseReport(
                lastUpdate, true, i, "siteName" + i, SseProperties.SiteType.FLOATING, lastUpdate, SseProperties.SeaState.BREEZE,
                SseProperties.Trend.ASCENDING, 90, SseProperties.Confidence.GOOD, BigDecimal.valueOf(45.0), SseProperties.LightStatus.ON,
                10,  BigDecimal.valueOf(21.22558), BigDecimal.valueOf(59.44507)));
        }
        return reports;
    }
}
