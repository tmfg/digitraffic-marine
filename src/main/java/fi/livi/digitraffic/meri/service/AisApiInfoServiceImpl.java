package fi.livi.digitraffic.meri.service;

import fi.livi.digitraffic.meri.documentation.AisApiInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springfox.documentation.service.ApiInfo;

@Service
public class AisApiInfoServiceImpl  implements AisApiInfoService {


    private final MessageService messageService;

    private final BuildVersionService buildVersionService;

    @Autowired
    public AisApiInfoServiceImpl(final MessageService messageService,
                                 final BuildVersionService buildVersionService) {
        this.messageService = messageService;
        this.buildVersionService = buildVersionService;
    }

    @Override
    public ApiInfo getApiInfo() {
        return new AisApiInfo(messageService, buildVersionService);
    }
}
