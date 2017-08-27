package com.github.jactorrises.client.facade;

import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.client.domain.User;

import java.util.Optional;

public interface UserFacade {

    Optional<User> findUsing(UserName userName);
}
