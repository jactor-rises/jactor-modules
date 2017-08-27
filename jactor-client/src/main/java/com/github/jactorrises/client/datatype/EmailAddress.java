package com.github.jactorrises.client.datatype;

import org.apache.commons.lang3.Validate;

import java.util.Objects;

import static java.util.Objects.hash;

public class EmailAddress {
    private static final String AT = "@";

    private final String prefix;
    private final String suffix;

    public EmailAddress(String fullEmailAddress) {
        String[] prefixAndSuffix = validateAtSign(fullEmailAddress).split(AT);
        prefix = prefixAndSuffix[0];
        suffix = prefixAndSuffix[1];
    }

    private String validateAtSign(String fullEmailArress) {
        Validate.notBlank(fullEmailArress, "full email address is required");

        if (!fullEmailArress.contains(AT)) {
            throw new IllegalArgumentException("An email address requires an @-sign");
        }

        return fullEmailArress;
    }

    public EmailAddress(String prefix, String suffix) {
        Validate.notBlank(prefix, "prefix cannot be empty");
        Validate.notBlank(suffix, "suffix cannot be empty");

        this.prefix = prefix;
        this.suffix = suffix;
    }

    @Override public int hashCode() {
        return hash(prefix, suffix);
    }

    @Override public boolean equals(Object obj) {
        return obj == this || isSameClasses(obj) && haveEqualValues((EmailAddress) obj);
    }

    private boolean isSameClasses(Object obj) {
        return obj != null && obj.getClass().equals(this.getClass());
    }

    private boolean haveEqualValues(EmailAddress obj) {
        return Objects.equals(prefix, obj.prefix) && Objects.equals(suffix, obj.suffix);
    }


    public String asString() {
        return prefix + AT + suffix;
    }

    @Override public String toString() {
        return EmailAddress.class.getSimpleName() + "[" + asString() + "]";
    }
}
