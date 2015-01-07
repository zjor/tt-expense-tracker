package com.toptal.expensetracker.logging;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Arrays;

/**
 * Intercepts {@link Log} annotation and logs method invocation
 */
@Component
public class LogInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        try {
            Object result = invocation.proceed();
            log(method, invocation.getArguments(), result);
            return result;
        } catch (Throwable t) {
            logError(t, method, invocation.getArguments());
            throw t;
        }
    }

    private void log(Method method, Object[] args, Object result) {
        Log log = method.getAnnotation(Log.class);
        String logMessage = MessageFormat.format("{0}.{1} ({2}): {3}",
                method.getDeclaringClass().getSimpleName(),
                method.getName(), Arrays.toString(args), result);
        Logger logger = getLogger(method);
        switch (log.level()) {
            case TRACE:
                logger.trace(logMessage);
                break;
            case DEBUG:
                logger.debug(logMessage);
                break;
            case INFO:
                logger.info(logMessage);
                break;
            case WARN:
                logger.warn(logMessage);
                break;
            default:
                logger.error(logMessage);
                break;
        }
    }

    private void logError(Throwable t, Method method, Object[] args) {
        String logMessage = MessageFormat.format("{0}.{1} ({2}) threw {3}: {4}",
                method.getDeclaringClass().getSimpleName(),
                method.getName(), Arrays.toString(args), t.getClass().getSimpleName(), t.getMessage());
        getLogger(method).error(logMessage, t);
    }

    private Logger getLogger(Method method) {
        return LoggerFactory.getLogger(method.getDeclaringClass());
    }
}
