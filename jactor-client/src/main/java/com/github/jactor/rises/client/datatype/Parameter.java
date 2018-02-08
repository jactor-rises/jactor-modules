package com.github.jactor.rises.client.datatype;

import org.apache.commons.lang3.Validate;

import java.util.Objects;

public class Parameter {
    private static final String EQUAL_SIGN = "=";

    private final String name;
    private final String value;

    public Parameter(String name, String value) {
        Validate.notBlank(name, "The parameter name cannot be blank");
        Validate.notBlank(value, "The parameter value cannot be blank");

        this.name = name;
        this.value = value;
    }

    public Parameter(String parameterAndValue) {
        this(parameterAndValue.split(EQUAL_SIGN)[0], parameterAndValue.split(EQUAL_SIGN)[1]);
    }

    @Override public int hashCode() {
        return name.hashCode() + value.hashCode();
    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Parameter parameter = (Parameter) o;

        return Objects.equals(name, parameter.name) && Objects.equals(value, parameter.value);
    }

    @Override public String toString() {
        return name + EQUAL_SIGN + value;
    }

    public String getKey() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
