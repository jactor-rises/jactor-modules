package com.github.jactorrises.persistence.entity.user;

import com.github.jactorrises.client.datatype.EmailAddress;

import java.util.Objects;

import static java.util.Objects.hash;

class EmailAddressEmbeddable {
    private String emailAddress;

    @SuppressWarnings("unused") // used by hibernate
    EmailAddressEmbeddable() {
        this.emailAddress = null;
    }

    EmailAddressEmbeddable(EmailAddress emailAddress) {
        this.emailAddress = emailAddress != null ? emailAddress.asString() : null;
    }

    EmailAddress fetchEmailAddress() {
        return emailAddress != null ? new EmailAddress(emailAddress) : null;
    }

    String asString() {
        return emailAddress;
    }

    @Override public boolean equals(Object o) {
        return o == this || o != null && o.getClass().equals(getClass()) && Objects.equals(emailAddress, ((EmailAddressEmbeddable) o).emailAddress);
    }

    @Override public int hashCode() {
        return hash(emailAddress);
    }

    @Override public String toString() {
        return emailAddress != null ? new EmailAddress(emailAddress).toString() : "No email address";
    }
}
