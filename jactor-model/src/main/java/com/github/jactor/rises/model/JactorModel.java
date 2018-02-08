package com.github.jactor.rises.model;

import com.github.jactorrises.client.facade.UserFacade;
import com.github.jactor.rises.model.facade.UserFacadeImpl;
import com.github.jactor.rises.model.service.GuestBookDomainService;
import com.github.jactor.rises.model.service.UserDomainService;
import com.github.jactor.rises.persistence.beans.PersistenceBeans;
import com.github.jactor.rises.persistence.beans.service.GuestBookRestService;
import com.github.jactor.rises.persistence.beans.service.UserRestService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({PersistenceBeans.class})
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
