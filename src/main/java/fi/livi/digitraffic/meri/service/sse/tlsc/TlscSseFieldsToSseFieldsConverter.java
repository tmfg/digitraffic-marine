package fi.livi.digitraffic.meri.service.sse.tlsc;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Component;

import fi.livi.digitraffic.meri.config.converter.AutoRegisteredConverter;
import fi.livi.digitraffic.meri.domain.sse.tlsc.SseFields;

@ConditionalOnWebApplication
@Component
public class TlscSseFieldsToSseFieldsConverter
    extends AutoRegisteredConverter<fi.livi.digitraffic.meri.external.sse.SSEFields, SseFields> {

    @Override
    public SseFields convert(fi.livi.digitraffic.meri.external.sse.SSEFields src) {
        return new SseFields(src.getLastUpdate(),
                             src.getSeaState(),
                             src.getTrend(),
                             src.getWindWaveDir(),
                             src.getConfidence(),
                             src.getAdditionalProperties());
    }
}
