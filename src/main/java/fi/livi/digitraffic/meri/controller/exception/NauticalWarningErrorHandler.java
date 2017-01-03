package fi.livi.digitraffic.meri.controller.exception;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import fi.livi.digitraffic.util.RestUtil;

public class NauticalWarningErrorHandler implements ResponseErrorHandler {
    private static final Logger log = LoggerFactory.getLogger(NauticalWarningErrorHandler.class);

    @Override
    public boolean hasError(final ClientHttpResponse response) throws IOException {
        return RestUtil.isError(response.getStatusCode());
    }

    @Override
    public void handleError(final ClientHttpResponse response) throws IOException {
        log.info("Pooki response error: {} {}", response.getStatusCode(), response.getStatusText());

        final Map<String, Object> errorAttributes = new LinkedHashMap<>();
        errorAttributes.put("timestamp", new Date());
        errorAttributes.put("status", response.getStatusCode().value());
        errorAttributes.put("error", response.getStatusCode().getReasonPhrase());
        errorAttributes.put("exception", PookiException.class.getSimpleName());
        errorAttributes.put("message", IOUtils.toString(response.getBody(), "UTF-8"));
        errorAttributes.put("path", response.getHeaders().getLocation());

        throw new PookiException(String.format("Pooki response error: %s %s", response.getStatusCode(), response.getStatusText()), errorAttributes);
    }
}
