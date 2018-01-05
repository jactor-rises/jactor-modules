package com.github.jactorrises.persistence.beans;

import com.github.jactorrises.persistence.beans.service.BlogRestService;
import com.github.jactorrises.persistence.beans.service.GuestBookRestService;
import com.github.jactorrises.persistence.beans.service.UserRestService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class PersistenceBeans {

    @Bean public BlogRestService blogDaoService() {
        return new BlogRestService(new RestTemplate(), null);
    }

    @Bean public GuestBookRestService guestBookDaoService() {
        return new GuestBookRestService(new RestTemplate());
    }

    @Bean public UserRestService userDaoService() {
        return new UserRestService(new RestTemplate());
    }
}
