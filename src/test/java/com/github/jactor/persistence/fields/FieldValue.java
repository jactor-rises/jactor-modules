package com.github.jactor.persistence.fields;

public class FieldValue {

    private final DynamicFieldValue dynamicFieldValue;
    private final String name;
    private String setter;

    public FieldValue(String name, Object value) {
        this(name, () -> value, null);
    }

    public FieldValue(String name, DynamicFieldValue dynamicFieldValue) {
        this(name, dynamicFieldValue, null);
    }

     private FieldValue(String name, DynamicFieldValue dynamicFieldValue, String setter) {
        this.dynamicFieldValue = dynamicFieldValue;
        this.name = name;
        this.setter = setter;
    }

    String fetchNameOfSetter() {
        if (setter != null) {
            return setter;
        }

        return "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    Object fetchValue() {
        return dynamicFieldValue.provideValue();
    }

    @Override public String toString() {
        return String.format("%s (%s): %s", name, fetchNameOfSetter(), fetchValue());
    }

    String getName() {
        return name;
    }

    @FunctionalInterface
    public interface DynamicFieldValue {
        Object provideValue();
    }
}
