package com.github.jactorrises.model.internal.persistence.entity.user;

public class UserEntityBuilder {
    private String userName;
    private String password;

    UserEntityBuilder() { }

    public UserEntityBuilder withUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public UserEntityBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserEntity build() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(userName);
        userEntity.setPassword(password);

        return userEntity;
    }
}
