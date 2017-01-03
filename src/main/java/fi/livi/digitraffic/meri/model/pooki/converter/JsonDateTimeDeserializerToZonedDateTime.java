package fi.livi.digitraffic.meri.model.pooki.converter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import fi.livi.digitraffic.meri.config.SchedulerConfig;

@Component
public class JsonDateTimeDeserializerToZonedDateTime extends JsonDeserializer<ZonedDateTime> {

    private static final Logger log = LoggerFactory.getLogger(SchedulerConfig.class);

    protected static SimpleDateFormat[] DATE_FORMATS =
            new SimpleDateFormat[] { new SimpleDateFormat("d.M.yyyy h:m:s"),
                                     new SimpleDateFormat("d.M.yyyy h:m"),
                                     new SimpleDateFormat("d.M.yyyy") };

    @Override
    public ZonedDateTime deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {

        ObjectCodec oc = jp.getCodec();
        JsonNode node = oc.readTree(jp);
        String dateString = node.asText();

        if (StringUtils.isNotBlank(dateString)) {
            log.debug("From " + dateString + " to " + parseDateQuietly(dateString));
        }

        return parseDateQuietly(dateString);
    }

    public static ZonedDateTime parseDateQuietly(final String dateTime) {
        if (StringUtils.isBlank(dateTime)) {
            return null;
        }
        for (SimpleDateFormat dateFormat : DATE_FORMATS)  {
            try {
                Date date = dateFormat.parse(dateTime);
                return ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
            } catch (ParseException e) {
                log.debug("Parse of " + dateTime + " failed", e);
            }
        }
        log.warn("Could not parse dateTime " + dateTime);
        return null;
    }
}
