package com.github.jactorrises.persistence.entity;

import java.time.LocalDateTime;
import java.util.Date;

public class Now {
    private static final Object SYNC = new Object();

    private static volatile Now instance;

    protected LocalDateTime nowAsDateTime() {
        return LocalDateTime.now();
    }

    protected Date nowAsDate() {
        return new Date();
    }

    public static LocalDateTime asDateTime() {
        return instance.nowAsDateTime();
    }

    public static Date asDate() {
        return instance.nowAsDate();
    }

    static void reset(Now instance) {
        synchronized (SYNC) {
            Now.instance = instance;
        }
    }

    static {
        reset(new Now());
    }
}
