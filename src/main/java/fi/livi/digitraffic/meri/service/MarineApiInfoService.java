package fi.livi.digitraffic.meri.service;

import fi.livi.digitraffic.meri.documentation.MarineApiInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MarineApiInfoService {
    private final MessageService messageService;

    private final BuildVersionService buildVersionService;

    @Autowired
    public MarineApiInfoService(final MessageService messageService,
                             final BuildVersionService buildVersionService) {
        this.messageService = messageService;
        this.buildVersionService = buildVersionService;
    }

    public MarineApiInfo getApiInfo() {
        return new MarineApiInfo(messageService, buildVersionService);
    }
}
