package com.github.jactorrises.persistence.boot.entity.user;

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

    public UserEntityImpl build() {
        UserEntityImpl userEntity = new UserEntityImpl();
        userEntity.setUserName(userName);
        userEntity.setPassword(password);

        return userEntity;
    }
}
