package fi.livi.digitraffic.meri.service.winternavigation;

import org.slf4j.Logger;
import org.springframework.ws.soap.client.SoapFaultClientException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class SoapFaultLogger {
    public static final void logException(final Logger logger, final Exception e) {
        if(e instanceof SoapFaultClientException) {
            logger.error("soapfault={}", getSoapFaultAsString((SoapFaultClientException) e));
        } else {
            logger.error("exception from soap", e);
        }
    }

    private static String getSoapFaultAsString(final SoapFaultClientException e) {
        try {
            return new ObjectMapper().writeValueAsString(e.getSoapFault());
        } catch (final JsonProcessingException e1) {
            return e1.getMessage();
        }
    }
}
