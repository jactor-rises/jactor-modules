package com.github.jactorrises.model.business.persistence.entity;

import com.github.jactorrises.model.business.persistence.client.time.Now;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public final class NowAsPureDate extends Now {

    private NowAsPureDate() {
    }

    @Override protected LocalDateTime nowAsDateTime() {
        return LocalDateTime.now()
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    @Override protected Date nowAsDate() {
        return Date.from(nowAsDateTime().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static void set() {
        reset(new NowAsPureDate());
    }

    public static void remove() {
        reset(new Now());
    }
}
