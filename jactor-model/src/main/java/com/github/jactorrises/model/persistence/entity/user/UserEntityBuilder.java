package com.github.jactorrises.model.persistence.entity.user;

import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.model.Builder;

public class UserEntityBuilder extends Builder<UserEntity> {
    private UserName userName;
    private String password;

    UserEntityBuilder() {
    }

    public UserEntityBuilder withUserName(String userName) {
        this.userName = new UserName(userName);
        return this;
    }

    public UserEntityBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    @Override protected UserEntity buildBean() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName(userName);
        userEntity.setPassword(password);

        return userEntity;
    }
}
