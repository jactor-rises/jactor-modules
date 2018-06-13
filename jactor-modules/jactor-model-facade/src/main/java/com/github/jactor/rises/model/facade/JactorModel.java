package com.github.jactor.rises.model.facade;

import com.github.jactor.rises.client.facade.UserFacade;
import com.github.jactor.rises.model.facade.UserFacadeImpl;
import com.github.jactor.rises.model.service.GuestBookDomainService;
import com.github.jactor.rises.model.service.UserDomainService;
import com.github.jactor.rises.model.service.rest.GuestBookRestService;
import com.github.jactor.rises.model.service.rest.UserRestService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
// todo: #193: spring context for services exposed to usage of jactor-rises
public class JactorModel {

    @Bean
    public UserFacade userFacade(UserRestService userRestService) {
        return new UserFacadeImpl(userRestService);
    }

    @Bean
    public GuestBookDomainService guestBookDomainService(GuestBookRestService guestBookRestService) {
        return new GuestBookDomainService(guestBookRestService);
    }

    @Bean
    public UserDomainService userDaoService(UserRestService userRestService) {
        return new UserDomainService(userRestService);
    }
}
