package fi.livi.digitraffic.meri.model.pooki.converter;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

@Component
/**
 * This deserializes times without timezone to zoneddatetimes.  This assumes, that the times
 * without timezone are in fact in Europe/Helsinki timezone.
 */
public class JsonDateTimeDeserializerToZonedDateTime extends JsonDeserializer<ZonedDateTime> {
    private static final Logger log = LoggerFactory.getLogger(JsonDateTimeDeserializerToZonedDateTime.class);

    private static DateTimeFormatter createFormatter(final String pattern) {
        return new DateTimeFormatterBuilder()
            .appendPattern(pattern)
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .toFormatter()
            .withZone(ZoneId.of("Europe/Helsinki"));
    }

    private static final List<DateTimeFormatter> DATE_FORMATTERS = List.of(
        createFormatter("d.M.yyyy H:m:s"),
        createFormatter("d.M.yyyy H:m"),
        createFormatter("d.M.yyyy"),
        createFormatter("yyyy-MM-dd HH:mm:ss")
    );

    @Override
    public ZonedDateTime deserialize(final JsonParser jp, final DeserializationContext ctxt)
            throws IOException {

        final ObjectCodec oc = jp.getCodec();
        final JsonNode node = oc.readTree(jp);
        final String dateString = node.asText();

        if (StringUtils.isNotBlank(dateString)) {
            log.debug("From {} to {}", dateString, parseDateQuietly(dateString));
        }

        return parseDateQuietly(dateString);
    }

    public ZonedDateTime parseDateQuietly(final String dateTime) {
        if (StringUtils.isBlank(dateTime)) {
            return null;
        }

        for(final DateTimeFormatter dateTimeFormatter : DATE_FORMATTERS) {
            try {
                return ZonedDateTime.parse(dateTime, dateTimeFormatter);
            } catch(final DateTimeException e) {
                log.debug("Parse of " + dateTime + " failed", e);
            }
        };

        log.warn("Could not parse dateTime={}", dateTime);

        return null;
    }
}
