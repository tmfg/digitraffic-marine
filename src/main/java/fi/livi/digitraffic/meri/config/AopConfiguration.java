package fi.livi.digitraffic.meri.config;

import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import fi.livi.digitraffic.meri.aop.PerformanceMonitorAspect;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Aspect
public class AopConfiguration {

    private static final Logger log = LoggerFactory.getLogger(AopConfiguration.class);

    @Bean
    public PerformanceMonitorAspect performanceMonitorAspect() {
        return new PerformanceMonitorAspect();
    }
}
