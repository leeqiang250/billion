package com.billion.model.factory;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author liqiang
 */
@Slf4j
public class NamedThreadFactory implements ThreadFactory {

    final String prefix;

    final AtomicInteger counter = new AtomicInteger(0);

    final boolean daemon;

    public NamedThreadFactory(String prefix) {
        this(prefix, false);
    }

    public NamedThreadFactory(String prefix, boolean daemon) {
        super();
        this.prefix = prefix;
        this.daemon = daemon;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r);
        t.setDaemon(this.daemon);
        t.setName(this.prefix + "-" + counter.getAndIncrement());
        return t;
    }

}