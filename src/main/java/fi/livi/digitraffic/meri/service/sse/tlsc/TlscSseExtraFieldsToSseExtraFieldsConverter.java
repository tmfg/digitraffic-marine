package fi.livi.digitraffic.meri.service.sse.tlsc;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fi.livi.digitraffic.meri.config.converter.AutoRegisteredConverter;
import fi.livi.digitraffic.meri.domain.sse.tlsc.ExtraFields;
import fi.livi.digitraffic.meri.domain.sse.tlsc.Site;
import fi.livi.digitraffic.meri.domain.sse.tlsc.SseFields;

@ConditionalOnWebApplication
@Component
public class TlscSseExtraFieldsToSseExtraFieldsConverter
    extends AutoRegisteredConverter<fi.livi.digitraffic.meri.external.sse.ExtraFields, ExtraFields> {

    @Override
    public ExtraFields convert(fi.livi.digitraffic.meri.external.sse.ExtraFields src) {
        return new ExtraFields(src.getTemperature(), src.getBattVoltage(), src.getAdditionalProperties());
    }
}
