package fi.livi.digitraffic.meri.service.sse.tlsc;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Component;

import fi.livi.digitraffic.meri.config.converter.AutoRegisteredConverter;
import fi.livi.digitraffic.meri.model.sse.tlsc.SseExtraFields;

@ConditionalOnWebApplication
@Component
public class TlscSseExtraFieldsToSseExtraFieldsConverter
    extends AutoRegisteredConverter<fi.livi.digitraffic.meri.external.tlsc.sse.ExtraFields, SseExtraFields> {

    @Override
    public SseExtraFields convert(fi.livi.digitraffic.meri.external.tlsc.sse.ExtraFields src) {
        return new SseExtraFields(src.getTemperature(), src.getBattVoltage(), src.getAdditionalProperties());
    }
}
