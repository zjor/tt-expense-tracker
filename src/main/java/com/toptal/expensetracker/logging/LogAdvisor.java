package com.toptal.expensetracker.logging;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.lang.reflect.Method;

/**
 * Aspect configuration which enables {@link LogInterceptor} for {@link Log} annotated methods
 */
@Component
public class LogAdvisor extends AbstractPointcutAdvisor {

    private final StaticMethodMatcherPointcut pointcut = new StaticMethodMatcherPointcut() {
        @Override
        public boolean matches(Method method, Class<?> aClass) {
            return method.isAnnotationPresent(Log.class);
        }
    };

    @Inject
    private LogInterceptor interceptor;

    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }

    @Override
    public Advice getAdvice() {
        return interceptor;
    }
}
