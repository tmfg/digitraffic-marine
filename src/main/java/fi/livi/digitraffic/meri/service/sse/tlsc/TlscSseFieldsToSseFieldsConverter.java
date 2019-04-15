package fi.livi.digitraffic.meri.service.sse.tlsc;

import static fi.livi.digitraffic.meri.model.sse.SseProperties.Confidence;
import static fi.livi.digitraffic.meri.model.sse.SseProperties.SeaState;
import static fi.livi.digitraffic.meri.model.sse.SseProperties.Trend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Component;

import fi.livi.digitraffic.meri.config.converter.AutoRegisteredConverter;
import fi.livi.digitraffic.meri.model.sse.tlsc.SseFields;

@ConditionalOnWebApplication
@Component
public class TlscSseFieldsToSseFieldsConverter
    extends AutoRegisteredConverter<fi.livi.digitraffic.meri.external.tlsc.sse.SSEFields, SseFields> {

    private static final Logger log = LoggerFactory.getLogger(TlscSseFieldsToSseFieldsConverter.class);

    @Override
    public SseFields convert(fi.livi.digitraffic.meri.external.tlsc.sse.SSEFields src) {
        if (!src.getAdditionalProperties().isEmpty()) {
            log.warn("No mappings at {} for additionalProperties={}", src.getClass(), src.getAdditionalProperties());
        }
        return new SseFields(src.getLastUpdate(),
            SeaState.fromValue(src.getSeaState()),
            Trend.fromValue(src.getTrend()),
            src.getWindWaveDir(),
            Confidence.fromValue(src.getConfidence()),
            src.getAdditionalProperties());
    }
}
