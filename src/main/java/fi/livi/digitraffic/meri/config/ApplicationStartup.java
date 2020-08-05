package fi.livi.digitraffic.meri.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import fi.livi.digitraffic.meri.service.BuildVersionService;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger log = LoggerFactory.getLogger(ApplicationStartup.class);

    @Autowired
    private BuildVersionService buildVersionService;

    @Value("${app.type}")
    private String appType;

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent applicationReadyEvent) {
        log.info("startedApp=MarineApplication appType={} version: {}", appType, buildVersionService.getAppFullVersion());
    }

    @EventListener
    public void onShutdown(final ContextStoppedEvent event) {
        log.info("stoppedApp=MarineApplication appType={} version: {}", appType, buildVersionService.getAppFullVersion());
    }
}
