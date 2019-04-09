package fi.livi.digitraffic.meri.service.sse.tlsc;

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
                             src.getSeaState(),
                             src.getTrend(),
                             src.getWindWaveDir(),
                             src.getConfidence(),
                             src.getAdditionalProperties());
    }
}
