package fi.livi.digitraffic.meri.service;

import java.util.Locale;

import fi.livi.digitraffic.common.annotation.NotTransactionalServiceMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    private final MessageSource messageSource;

    @Autowired
    public MessageService(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @NotTransactionalServiceMethod
    public String getMessage(final String code) {
        return getMessage(code, (Object[])null);
    }

    @NotTransactionalServiceMethod
    public String getMessage(final String code, final Object[] args) {
        return getMessage(code, args, null, LocaleContextHolder.getLocale());
    }

    @NotTransactionalServiceMethod
    public String getMessage(final String code, final String defaultMessage) {
        return getMessage(code, null, defaultMessage);
    }

    @NotTransactionalServiceMethod
    public String getMessage(final String code, final Object[] args, final String defaultMessage) {
        return getMessage(code, args, defaultMessage, LocaleContextHolder.getLocale());
    }

    @NotTransactionalServiceMethod
    public String getMessage(final MessageSourceResolvable resolvable) {
        return messageSource.getMessage(resolvable, LocaleContextHolder.getLocale());
    }

    @NotTransactionalServiceMethod
    public Locale getLocale() {
        return LocaleContextHolder.getLocale();
    }

    protected String getMessage(final String code, final Object[] args, final String defaultMessage, final Locale locale) {
        return messageSource.getMessage(code, args, defaultMessage, locale);
    }

}
