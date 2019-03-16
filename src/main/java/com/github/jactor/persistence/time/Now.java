package com.github.jactor.persistence.time;

import java.time.LocalDateTime;

public class Now {
    private static final Object SYNC = new Object();

    private static volatile Now instance;

    protected LocalDateTime nowAsDateTime() {
        return LocalDateTime.now();
    }

    public static LocalDateTime asDateTime() {
        return instance.nowAsDateTime();
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
