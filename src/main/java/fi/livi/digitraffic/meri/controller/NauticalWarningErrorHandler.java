package fi.livi.digitraffic.meri.controller;

import java.io.IOException;

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
    }
}
