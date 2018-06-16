package com.github.jactor.rises.io.ctx;

import com.github.jactor.rises.io.rest.BlogRestService;
import com.github.jactor.rises.io.rest.GuestBookRestService;
import com.github.jactor.rises.io.rest.UserRestService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class JactorIo {

    private static final String HTTP_LOCALHOST = "http://localhost:8080/jactor-persistence-orm";

    @Bean public BlogRestService blogRestService() {
        return new BlogRestService(new RestTemplate(), HTTP_LOCALHOST);
    }

    @Bean public GuestBookRestService guestBookRestService() {
        return new GuestBookRestService(new RestTemplate(), HTTP_LOCALHOST);
    }

    @Bean public UserRestService userRestService() {
        return new UserRestService(new RestTemplate(), HTTP_LOCALHOST);
    }
}
