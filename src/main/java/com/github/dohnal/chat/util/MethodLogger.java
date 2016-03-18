package com.github.dohnal.chat.util;

import javax.annotation.Nonnull;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dohnal
 */
@Aspect
public class MethodLogger
{
    private Logger LOG = LoggerFactory.getLogger(MethodLogger.class);

    @Around("execution(* *(..)) && @annotation(Loggable)")
    public Object around(ProceedingJoinPoint point) throws Throwable
    {
        long start = System.currentTimeMillis();

        Object result = point.proceed();

        LOG.info("{}({}) in {} ms",
                getSignature(MethodSignature.class.cast(point.getSignature())),
                point.getArgs(),
                System.currentTimeMillis() - start
        );

        return result;
    }

    @Nonnull
    protected String getSignature(final @Nonnull MethodSignature signature)
    {
        return signature.getDeclaringType().getSimpleName() + "." + signature.getMethod().getName();
    }
}
