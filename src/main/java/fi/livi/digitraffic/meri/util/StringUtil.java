package fi.livi.digitraffic.meri.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StringUtil {

    private final static Logger log = LoggerFactory.getLogger(StringUtil.class);
    private static final ObjectMapper  objectMapper = new ObjectMapper();

    public static String toJsonString(final Object o) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert object to JSON-string", e);
        }
        return o.toString();
    }
}
