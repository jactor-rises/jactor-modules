package com.github.jactor.persistence.builder;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class MissingFields {
    private final List<String> fieldsMissing = new ArrayList<>();

    private String beanName;

    void failWhenWhenFieldsAreMissing() {
        if (!fieldsMissing.isEmpty()) {
            throw new IllegalStateException("Missing fields on " + beanName + ":\n- " + String.join("\n- ", fieldsMissing));
        }
    }

    public Optional<MissingFields> presentWhenFieldsAreMissing() {
        return fieldsMissing.isEmpty() ? Optional.empty() : Optional.of(this);
    }

    public void addInvalidFieldWhenNoValue(String beanName, String field, Object value) {
        if (value == null || (value instanceof CharSequence && StringUtils.isBlank((CharSequence) value))) {
            addBeanName(beanName);
            fieldsMissing.add(field);
        }
    }

    private void addBeanName(String beanName) {
        if (this.beanName == null) {
            this.beanName = beanName;
        }
    }

    public void forEach(Consumer<? super String> fieldConsumer) {
        fieldsMissing.forEach(fieldConsumer);
    }
}
