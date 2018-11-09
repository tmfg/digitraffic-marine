package fi.livi.digitraffic.meri.config;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import javax.sql.DataSource;

import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.quartz.spi.JobFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import fi.livi.digitraffic.meri.quartz.AutowiringSpringBeanJobFactory;
import fi.livi.digitraffic.meri.quartz.BerthUpdateJob;
import fi.livi.digitraffic.meri.quartz.PortCallUpdateJob;
import fi.livi.digitraffic.meri.quartz.SsnLocationUpdateJob;
import fi.livi.digitraffic.meri.quartz.VesselDetailsUpdateJob;
import fi.livi.digitraffic.meri.quartz.WinterNavigationDirwayUpdateJob;
import fi.livi.digitraffic.meri.quartz.WinterNavigationPortUpdateJob;
import fi.livi.digitraffic.meri.quartz.WinterNavigationShipUpdateJob;

@Configuration
@ConditionalOnProperty(name = "quartz.enabled")
@ConditionalOnNotWebApplication
public class SchedulerConfig {
    private static final Logger log = LoggerFactory.getLogger(SchedulerConfig.class);

    @Bean
    public JobFactory jobFactory(final ApplicationContext applicationContext)     {
        final AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(final DataSource dataSource,
                                                     final JobFactory jobFactory,
                                                     final Optional<List<Trigger>> triggerBeans) throws IOException {

        final SchedulerFactoryBean factory = new SchedulerFactoryBean();
        // this allows to update triggers in DB when updating settings in config file:
        factory.setOverwriteExistingJobs(true);
        factory.setDataSource(dataSource);
        factory.setJobFactory(jobFactory);

        factory.setQuartzProperties(quartzProperties());

        if (triggerBeans.isPresent()) {
            for (Trigger triggerBean : triggerBeans.get()) {
                if (triggerBean instanceof  SimpleTriggerImpl) {
                    log.info("Schedule trigger={} repeatInterval={}", triggerBean.getJobKey(), ((SimpleTriggerImpl)triggerBean).getRepeatInterval());
                } else {
                    log.info("Schedule trigger={}", triggerBean.getJobKey());
                }
            }
            factory.setTriggers(triggerBeans.get().toArray(new Trigger[0]));
        }
        return factory;
    }

    @Bean
    public Properties quartzProperties() throws IOException {
        final PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

    @Bean
    public JobDetailFactoryBean portCallUpdateJobDetail() {
        return createJobDetail(PortCallUpdateJob.class);
    }

    @Bean
    public JobDetailFactoryBean berthUpdateJobDetail() {
        return createJobDetail(BerthUpdateJob.class);
    }

    @Bean
    public JobDetailFactoryBean ssnLocationUpdateJobDetail() {
        return createJobDetail(SsnLocationUpdateJob.class);
    }

    @Bean
    public JobDetailFactoryBean vesselDetailsUpdateJobDetail() {
        return createJobDetail(VesselDetailsUpdateJob.class);
    }

    @Bean
    public JobDetailFactoryBean winterNavigationShipUpdateJobDetail() {
        return createJobDetail(WinterNavigationShipUpdateJob.class);
    }

    @Bean
    public JobDetailFactoryBean winterNavigationPortUpdateJobDetail() {
        return createJobDetail(WinterNavigationPortUpdateJob.class);
    }

    @Bean
    public JobDetailFactoryBean winterNavigationDirwayUpdateJobDetail() {
        return createJobDetail(WinterNavigationDirwayUpdateJob.class);
    }

    @Bean
    public SimpleTriggerFactoryBean portCallUpdateJobTrigger(final JobDetail portCallUpdateJobDetail,
                                                             @Value("${portCallUpdateJob.frequency}") final long frequency) {
        return createTrigger(portCallUpdateJobDetail, frequency);
    }

    @Bean
    public SimpleTriggerFactoryBean berthUpdateJobTrigger(final JobDetail berthUpdateJobDetail,
                                                          @Value("${berthUpdateJob.frequency}") final long frequency) {
        return createTrigger(berthUpdateJobDetail, frequency);
    }

    @Bean
    public SimpleTriggerFactoryBean ssnLocationUpdateJobTrigger(final JobDetail ssnLocationUpdateJobDetail,
                                                                @Value("${ssnLocationUpdateJob.frequency}") final long frequency) {
        return createTrigger(ssnLocationUpdateJobDetail, frequency);
    }

    @Bean
    public SimpleTriggerFactoryBean vesselDetailUpdateJobTrigger(final JobDetail vesselDetailsUpdateJobDetail,
                                                                 @Value("${vesselDetailsUpdateJob.frequency}") final long frequency) {
        return createTrigger(vesselDetailsUpdateJobDetail, frequency);
    }

    @Bean
    public SimpleTriggerFactoryBean winterNavigationShipUpdateJobTrigger(final JobDetail winterNavigationShipUpdateJobDetail,
                                                                         @Value("${winterNavigationShipUpdateJob.frequency}") final long frequency) {
        return createTrigger(winterNavigationShipUpdateJobDetail, frequency);
    }

    @Bean
    public SimpleTriggerFactoryBean winterNavigationPortUpdateJobTrigger(final JobDetail winterNavigationPortUpdateJobDetail,
                                                                         @Value("${winterNavigationPortUpdateJob.frequency}") final long frequency) {
        return createTrigger(winterNavigationPortUpdateJobDetail, frequency);
    }

    @Bean
    public SimpleTriggerFactoryBean winterNavigationDirwayUpdateJobTrigger(final JobDetail winterNavigationDirwayUpdateJobDetail,
                                                                           @Value("${winterNavigationDirwayUpdateJob.frequency}") final long frequency) {
        return createTrigger(winterNavigationDirwayUpdateJobDetail, frequency);
    }

    private static JobDetailFactoryBean createJobDetail(final Class jobClass) {
        final JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(jobClass);
        // job has to be durable to be stored in DB:
        factoryBean.setDurability(true);
        return factoryBean;
    }

    private static SimpleTriggerFactoryBean createTrigger(final JobDetail jobDetail, final long pollFrequencyMs) {
        final SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(jobDetail);
        // Delay first execution 5 seconds
        factoryBean.setStartDelay(5000L);
        factoryBean.setRepeatInterval(pollFrequencyMs);
        factoryBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        // In case of misfire: The first misfired execution is run immediately, remaining are discarded.
        // Next execution happens after desired interval. Effectively the first execution time is moved to current time.
        factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_REMAINING_REPEAT_COUNT);
        return factoryBean;
    }
}
