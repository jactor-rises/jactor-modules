package com.github.jactorrises.model.persistence.entity.user;

import com.github.jactorrises.client.datatype.UserName;

import java.util.Objects;

import static java.util.Objects.hash;

public class UserNameEmbeddable {

    private final String userName;

    @SuppressWarnings("unused") // used by hibernate
    UserNameEmbeddable() {
        userName = null;
    }

    public UserNameEmbeddable(UserName userName) {
        this.userName = userName.asString();
    }

    UserName fetchUserName() {
        return new UserName(userName);
    }

    boolean isName(EmailAddressEmbeddable emailAddress) {
        return userName != null && userName.equals(emailAddress.asString());
    }

    @Override public int hashCode() {
        return hash(userName);
    }

    @Override public boolean equals(Object o) {
        return o == this || o != null && o.getClass().equals(getClass()) && Objects.equals(userName, ((UserNameEmbeddable) o).userName);
    }

    @Override public String toString() {
        return userName != null ? new UserName(userName).toString() : "";
    }
}
