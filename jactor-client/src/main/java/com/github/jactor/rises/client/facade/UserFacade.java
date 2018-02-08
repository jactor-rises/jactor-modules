package com.github.jactor.rises.client.facade;

import com.github.jactor.rises.client.datatype.UserName;
import com.github.jactor.rises.client.domain.User;

import java.util.Optional;

public interface UserFacade {

    Optional<User> findUsing(UserName userName);
}
