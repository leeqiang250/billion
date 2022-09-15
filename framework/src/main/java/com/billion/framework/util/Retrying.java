package com.billion.framework.util;

import com.github.rholder.retry.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @author liqiang
 */
@Slf4j
public class Retrying {

    public static <T> T retry(Callable<T> callable, int attempt, long delay, Class<? extends Throwable> exceptionClass) {
        try {
            return Retrying.<T>buildRetriever(attempt, delay, exceptionClass).call(callable);
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }
    }

    public static void retry(Runnable runnable, int attempt, long delay, Class<? extends Throwable> exceptionClass) {
        try {
            Retrying.buildRetriever(attempt, delay, exceptionClass).call(() -> {
                runnable.run();
                return null;
            });
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }
    }

    public static <T> Retryer<T> buildRetriever(int attempt, long delay, Class<? extends Throwable> exceptionClass) {
        return RetryerBuilder.<T>newBuilder()
                .retryIfExceptionOfType(exceptionClass)
                .withStopStrategy(StopStrategies.stopAfterAttempt(attempt))
                .withWaitStrategy(WaitStrategies.fixedWait(delay, TimeUnit.MILLISECONDS))
                .withRetryListener(new RetryListener() {
                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        if (attempt.hasException()) {
                            log.info("retry time:{}, msg:{}", attempt.getAttemptNumber(), attempt.getExceptionCause().getMessage());
                        }
                    }
                })
                .build();
    }

}