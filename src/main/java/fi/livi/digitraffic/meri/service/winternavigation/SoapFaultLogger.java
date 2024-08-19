package fi.livi.digitraffic.meri.service.winternavigation;

import org.slf4j.Logger;
import org.springframework.ws.soap.client.SoapFaultClientException;

import fi.livi.digitraffic.common.util.StringUtil;

public final class SoapFaultLogger {
    public static void logException(final Logger logger, final Exception e) {
        if(e instanceof SoapFaultClientException) {
            logger.error("soapfault={}", getSoapFaultAsString((SoapFaultClientException) e));
        } else {
            logger.error("exception from soap", e);
        }
    }

    private static String getSoapFaultAsString(final SoapFaultClientException e) {
        return StringUtil.toJsonStringLogSafe(e.getSoapFault());
    }
}
