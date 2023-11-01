package fi.livi.digitraffic.meri.config;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.quartz.spi.JobFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

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
public class QuartzSchedulerConfiguration {

    private static final Logger log = LoggerFactory.getLogger(QuartzSchedulerConfiguration.class);

    private final static String JOB_SCHEDULE_STRING_FORMAT = "dt.job.%s.schedule";

    private final Environment environment;

    public QuartzSchedulerConfiguration(final Environment environment) {
        this.environment = environment;
    }

    @Bean
    public JobFactory jobFactory(final ApplicationContext applicationContext)     {
        final AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean
    public DataSource quartzDataSource(final @Value("${marine.datasource.url}") String url,
        final @Value("${marine.datasource.username}") String username,
        final @Value("${marine.datasource.password}") String password) {

        final HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);

        config.setMaximumPoolSize(8);

        config.setMaxLifetime(570000);
        config.setIdleTimeout(500000);
        config.setConnectionTimeout(60000);
        config.setPoolName("quartz_pool");

        // Auto commit must be true for Quartz
        config.setAutoCommit(true);
        return new HikariDataSource(config);
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(@Qualifier("quartzDataSource")
                                                     final DataSource quartzDataSource,
                                                     final JobFactory jobFactory,
                                                     final Optional<List<Trigger>> triggerBeans) throws IOException {

        final SchedulerFactoryBean factory = new SchedulerFactoryBean() {
            @Override
            protected Scheduler createScheduler(final SchedulerFactory schedulerFactory, final String schedulerName) throws SchedulerException {
                final Scheduler scheduler = super.createScheduler(schedulerFactory, schedulerName);

                final List<Trigger> triggers = triggerBeans.orElse(Collections.emptyList());
                final Set<JobKey> jobKeys = triggers.stream().map(Trigger::getJobKey).collect(Collectors.toSet());

                for (final String groupName : scheduler.getJobGroupNames()) {
                    for (final JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                        if (!jobKeys.contains(jobKey)) {
                            try {
                                log.warn("method=createScheduler Deleting job={}", jobKey);
                                scheduler.deleteJob(jobKey);
                            } catch (final SchedulerException e) {
                                log.error("method=createScheduler Deleting job=" + jobKey + " failed", e);
                            }
                        }
                    }
                }
                return scheduler;
            }
        };
        // this allows to update triggers in DB when updating settings in config file:
        factory.setOverwriteExistingJobs(true);
        factory.setDataSource(quartzDataSource);
        factory.setJobFactory(jobFactory);

        factory.setQuartzProperties(quartzProperties());

        if (triggerBeans.isPresent()) {
            for (final Trigger triggerBean : triggerBeans.get()) {
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
    public FactoryBean<? extends Trigger> portCallUpdateJobTrigger(final JobDetail portCallUpdateJobDetail) {
        return createTrigger(portCallUpdateJobDetail);
    }

    @Bean
    public FactoryBean<? extends Trigger> berthUpdateJobTrigger(final JobDetail berthUpdateJobDetail) {
        return createTrigger(berthUpdateJobDetail);
    }

    @Bean
    public FactoryBean<? extends Trigger> ssnLocationUpdateJobTrigger(final JobDetail ssnLocationUpdateJobDetail) {
        return createTrigger(ssnLocationUpdateJobDetail);
    }

    @Bean
    public FactoryBean<? extends Trigger> vesselDetailUpdateJobTrigger(final JobDetail vesselDetailsUpdateJobDetail) {
        return createTrigger(vesselDetailsUpdateJobDetail);
    }

    @Bean
    public FactoryBean<? extends Trigger> winterNavigationShipUpdateJobTrigger(final JobDetail winterNavigationShipUpdateJobDetail) {
        return createTrigger(winterNavigationShipUpdateJobDetail);
    }

    @Bean
    public FactoryBean<? extends Trigger> winterNavigationPortUpdateJobTrigger(final JobDetail winterNavigationPortUpdateJobDetail) {
        return createTrigger(winterNavigationPortUpdateJobDetail);
    }

    @Bean
    public FactoryBean<? extends Trigger> winterNavigationDirwayUpdateJobTrigger(final JobDetail winterNavigationDirwayUpdateJobDetail) {
        return createTrigger(winterNavigationDirwayUpdateJobDetail);
    }

    private static JobDetailFactoryBean createJobDetail(final Class<? extends Job> jobClass) {
        final JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(jobClass);
        // job has to be durable to be stored in DB:
        factoryBean.setDurability(true);
        return factoryBean;
    }

    private FactoryBean<? extends Trigger> createTrigger(final JobDetail jobDetail) {

        final String jobScheduleProperty = String.format(JOB_SCHEDULE_STRING_FORMAT, jobDetail.getJobClass().getSimpleName());
        log.info("jobScheduleProperty={}", jobScheduleProperty);
        final String jobScheduleExpression = environment.getProperty(jobScheduleProperty);
        if (jobScheduleExpression == null) {
            log.warn("Not creating trigger for job {} as jobScheduleProperty={} is missing", jobDetail.getJobClass().getSimpleName(), jobScheduleProperty);
            return null;
        }
        try {
            // Try first to create interval trigger and fallback to cron
            final long intervalMs = Long.parseLong(jobScheduleExpression);
            return  createRepeatingTrigger(jobDetail, intervalMs);
        } catch (final NumberFormatException nfe) { // cron expression
            return createCronTrigger(jobDetail, jobScheduleExpression);
        }
    }

    /**
     * @param jobDetail
     * @param repeatIntervalMs how often is job repeated in ms. If time <= 0 it's triggered only once.
     * @return
     */
    private static SimpleTriggerFactoryBean createRepeatingTrigger(final JobDetail jobDetail, final long repeatIntervalMs) {

        final String jobName = getJobName(jobDetail);

        final SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(jobDetail);
        factoryBean.setRepeatInterval(repeatIntervalMs);
        factoryBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        // In case of misfire: The first misfired execution is run immediately, remaining are discarded.
        // Next execution happens after desired interval. Effectively the first execution time is moved to current time.
        factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_REMAINING_REPEAT_COUNT);

        log.info("Created SimpleTrigger for jobName={} with repeatIntervalMs={}", jobName, repeatIntervalMs);
        return factoryBean;
    }

    /**
     * @param jobDetail
     * @param cronExpression Cron expression for trigger schedule.
     * @return
     */
    private static CronTriggerFactoryBean createCronTrigger(final JobDetail jobDetail, final String cronExpression) {

        final String jobName = getJobName(jobDetail);

        final CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setJobDetail(jobDetail);
        factoryBean.setCronExpression(cronExpression);
        factoryBean.setTimeZone(TimeZone.getTimeZone("UTC"));
        factoryBean.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_FIRE_ONCE_NOW);
        log.info("Created CronTrigger for jobName={} with cron expression={}", jobName, cronExpression);
        return factoryBean;
    }

    private static String getJobName(final JobDetail jobDetail) {
        return jobDetail.getJobClass().getSimpleName();
    }
}
