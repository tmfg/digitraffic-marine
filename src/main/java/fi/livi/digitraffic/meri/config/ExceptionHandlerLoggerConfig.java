package fi.livi.digitraffic.meri.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fi.livi.digitraffic.meri.controller.exception.DefaultExceptionHandler;

@Configuration
public class ExceptionHandlerLoggerConfig {
    @Bean
    public Logger exceptionHandlerLogger() {
        return LoggerFactory.getLogger(DefaultExceptionHandler.class);
    }
}
