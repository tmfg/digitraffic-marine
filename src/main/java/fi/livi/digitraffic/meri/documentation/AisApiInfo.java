package fi.livi.digitraffic.meri.documentation;

import java.util.Collections;

import fi.livi.digitraffic.meri.service.BuildVersionService;
import fi.livi.digitraffic.meri.service.MessageService;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;

public class AisApiInfo {
    private BuildVersionService buildVersionService;
    protected MessageService messageService;

    public AisApiInfo(final MessageService messageService, final BuildVersionService buildVersionService) {
        this.messageService = messageService;
        this.buildVersionService = buildVersionService;
    }

    public String getTitle() {
        return messageService.getMessage("apiInfo.title");
    }

    public String getDescription() {
        return messageService.getMessage("apiInfo.description");
    }

    public String getVersion() {
        return buildVersionService.getAppFullVersion();
    }

    public Contact getContact() {
        return new Contact()
            .name(messageService.getMessage("apiInfo.contact.name"))
            .url(messageService.getMessage("apiInfo.contact.url"));
    }

    public String getTermsOfServiceUrl() {
        return messageService.getMessage("apiInfo.terms.of.service");
    }

    public License getLicense() {
        return new License()
            .name(messageService.getMessage("apiInfo.licence"))
            .url(messageService.getMessage("apiInfo.licence.url"));
    }

    public String getLicenseUrl() {
        return messageService.getMessage("apiInfo.licence.url");
    }

}