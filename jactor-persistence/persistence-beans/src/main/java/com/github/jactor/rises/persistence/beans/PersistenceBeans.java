package com.github.jactor.rises.persistence.beans;

import com.github.jactor.rises.persistence.beans.service.BlogRestService;
import com.github.jactor.rises.persistence.beans.service.GuestBookRestService;
import com.github.jactor.rises.persistence.beans.service.UserRestService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class PersistenceBeans {

    @Bean public BlogRestService blogDaoService() {
        return new BlogRestService(new RestTemplate(), null);
    }

    @Bean public GuestBookRestService guestBookDaoService() {
        return new GuestBookRestService(new RestTemplate(), null);
    }

    @Bean public UserRestService userDaoService() {
        return new UserRestService(new RestTemplate(), null);
    }
}
