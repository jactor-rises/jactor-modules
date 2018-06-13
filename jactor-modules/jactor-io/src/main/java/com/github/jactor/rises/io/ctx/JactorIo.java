package com.github.jactor.rises.io.ctx;

import com.github.jactor.rises.io.rest.BlogRestService;
import com.github.jactor.rises.io.rest.GuestBookRestService;
import com.github.jactor.rises.io.rest.UserRestService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class JactorIo {

    @Bean public BlogRestService blogRestService() {
        return new BlogRestService(new RestTemplate(), "http://localhost:8080");
    }

    @Bean public GuestBookRestService guestBookRestService() {
        return new GuestBookRestService(new RestTemplate(), "http://localhost:8080");
    }

    @Bean public UserRestService userRestService() {
        return new UserRestService(new RestTemplate(), "http://localhost:8080");
    }
}
