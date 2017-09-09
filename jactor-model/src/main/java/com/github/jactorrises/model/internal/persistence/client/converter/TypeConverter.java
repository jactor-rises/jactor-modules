package com.github.jactorrises.model.internal.persistence.client.converter;

public interface TypeConverter<T, F> {
    T convertTo(F from);

    F convertFrom(T to);
}
