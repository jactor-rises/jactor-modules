package com.github.jactorrises.persistence.orm.time;

import java.time.LocalDateTime;
import java.util.Date;

public class Now {
    private static final Object SYNC = new Object();

    private static volatile Now instance;

    LocalDateTime nowAsDateTime() {
        return LocalDateTime.now();
    }

    Date nowAsDate() {
        return new Date();
    }

    public static LocalDateTime asDateTime() {
        return instance.nowAsDateTime();
    }

    public static Date asDate() {
        return instance.nowAsDate();
    }

    static void setInstance(Now instance) {
        synchronized (SYNC) {
            Now.instance = instance;
        }
    }

    static {
        setInstance(new Now());
    }
}
