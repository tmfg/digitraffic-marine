package fi.livi.digitraffic.meri.service.winternavigation;

import org.slf4j.Logger;
import org.springframework.ws.soap.client.SoapFaultClientException;

import fi.livi.digitraffic.common.util.StringUtil;

public final class SoapFaultLogger {
    public static void logException(final Logger logger, final Exception e, final String methodName) {
        if(e instanceof SoapFaultClientException) {
            logger.error("method={} failed, soapfault={}", methodName, getSoapFaultAsString((SoapFaultClientException) e));
        } else {
            logger.error(StringUtil.format("method={} failed, exception from soap {}", methodName, e.getMessage()), e);
        }
    }

    private static String getSoapFaultAsString(final SoapFaultClientException e) {
        return StringUtil.toJsonStringLogSafe(e.getSoapFault());
    }
}
