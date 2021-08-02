package fi.livi.digitraffic.meri.aop;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import fi.livi.digitraffic.meri.annotation.PerformanceMonitor;

@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PerformanceMonitorAspect {

    private static final Logger log = LoggerFactory.getLogger("PerformanceMonitor");
    private static final Logger logScheduledJob = LoggerFactory.getLogger("ScheduledJobMonitor");
    public static final int DEFAULT_WARNING_LIMIT = 5000;
    public static final int DEFAULT_INFO_LIMIT = 1000;
    private static final DecimalFormat decimalFormat = new DecimalFormat("#0.0");

    /**
     * By default every method with @Scheduled annotation is monitored for
     * logging execution start and end.
     */
    @Around("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    public Object monitorScheduledJob(final ProceedingJoinPoint pjp) throws Throwable {

        final MethodSignature methodSignature = (MethodSignature) pjp.getSignature();

        final StopWatch stopWatch = StopWatch.createStarted();
        final String jobName = methodSignature.getDeclaringType().getSimpleName();
        logScheduledJob.info("jobType=Scheduled jobName={} start", jobName);

        try {
            return pjp.proceed();
        } catch (final Exception e) {
            logScheduledJob.info("jobType=Scheduled jobName={} end jobEndStatus={} jobTimeMs={} lastError: {} {}",
                jobName, "FAIL", stopWatch.getTime(), e.getClass(), e.getMessage());
            throw e;
        } finally {
            stopWatch.stop();
            logScheduledJob.info("jobType=Scheduled jobName={} end jobEndStatus={} jobTimeMs={}",
                jobName, "SUCCESS", stopWatch.getTime());
        }
    }

    /**
     * By default every method in class with @Service annotation is monitored.
     * In addition any method can be monitored with @PerformanceMonitor annotation.
     * @PerformanceMonitor annotation also has properties to adjust monitoring settings.
     *
     * @Around("@annotation(org.springframework.transaction.annotation.Transactional)") -> @Transactional annotated methods.
     * @Around("execution(* fi.livi.digitraffic.meri..*Service.*(..))") -> Every class which name ends to Service.
     * @Around("within(@org.springframework.stereotype.Service *)") -> Every class which has @Service annotation.
     *
     */
    @Around("within(@org.springframework.stereotype.Service *) || @annotation(fi.livi.digitraffic.meri.annotation.PerformanceMonitor)")
    public Object monitor(ProceedingJoinPoint pjp) throws Throwable {

        final MethodSignature methodSignature = (MethodSignature) pjp.getSignature();

        final StopWatch stopWatch = StopWatch.createStarted();

        if (log.isDebugEnabled()) {
            log.debug("monitor {}#{}", methodSignature.getDeclaringType().getName(), methodSignature.getName());
        }

        try {
            return pjp.proceed();
        } finally {
            stopWatch.stop();
            monitor(pjp, methodSignature, stopWatch.getTime());
        }
    }

    private void monitor(final ProceedingJoinPoint pjp, final MethodSignature methodSignature, final long executionTime) {
        final Method method = methodSignature.getMethod();
        final PerformanceMonitor monitorAnnotation = method.getAnnotation(PerformanceMonitor.class);
        final boolean monitor = monitorAnnotation != null ? monitorAnnotation.monitor() : true;

        if (monitor) {
            final int warningLimit = monitorAnnotation != null ? monitorAnnotation.maxWarnExcecutionTime() : DEFAULT_WARNING_LIMIT;
            final int infoLimit = monitorAnnotation != null ? monitorAnnotation.maxInfoExcecutionTime() : DEFAULT_INFO_LIMIT;
            final Object[] args = pjp.getArgs();
            final String methodWithClass = getMethodWithClass(methodSignature);

            if (executionTime > warningLimit &&
                log.isWarnEnabled()) {
                log.warn(buildMessage(methodWithClass, args, executionTime));
            } else if (executionTime > infoLimit &&
                       log.isInfoEnabled()) {
                log.info(buildMessage(methodWithClass, args, executionTime));
            }
        }
    }

    private String getMethodWithClass(final MethodSignature methodSignature) {
        return methodSignature.getDeclaringType().getName() + "#" + methodSignature.getName();
    }

    private String buildMessage(final String invocationName,
                                final Object[] args,
                                final double executionTime) {
        final double executionTimeSeconds = executionTime/1000.0;
        final StringBuilder builder = new StringBuilder(100)
            .append("invocation=").append(invocationName)
            .append(" invocationTimeSec=").append(decimalFormat.format(executionTimeSeconds));

        if (args != null && args.length > 0) {
            builder.append(" arguments=");
            buildValueToString(builder, args);
        }

        return StringUtils.truncate(builder.toString(), 1000);
    }

    private void buildValueToString(final StringBuilder builder, final Object value) {
        if (value == null) {
            builder.append("null");
            return;
        }

        if (value.getClass().isArray()) {
            try {
                final Object[] objects = (Object[]) value;
                buildArrayToString(builder, objects);
            } catch (ClassCastException e) {
                builder.append("?");
            }
        } else if (value instanceof Collection<?>) {
            final Collection<?> values = (Collection<?>) value;
            final Object[] objects = values.toArray(new Object[values.size()]);
            buildArrayToString(builder, objects);
        } else {
            builder.append(value.toString());
        }
    }

    private void buildArrayToString(final StringBuilder builder, final Object[] objects) {
        builder.append("[");
        for (int index = 0;
            index < objects.length && index < 5;
            index++) {
            if(index > 0) {
                builder.append(", ");
            }
            buildValueToString(builder, objects[index]);
        }
        builder.append("]");
    }

}
