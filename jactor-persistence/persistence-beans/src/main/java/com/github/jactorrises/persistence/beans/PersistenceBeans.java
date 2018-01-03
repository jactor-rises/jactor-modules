package com.github.jactorrises.persistence.beans;

import com.github.jactorrises.persistence.service.GuestBookDaoService;
import com.github.jactorrises.persistence.service.UserDaoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersistenceBeans {

    @Bean
    public UserDaoService userDaoService() {
        return new UserDaoService();
    }

    @Bean
    public GuestBookDaoService guestBookDaoService() {
        return new GuestBookDaoService();
    }
}
