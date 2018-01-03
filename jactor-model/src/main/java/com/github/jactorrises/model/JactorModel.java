package com.github.jactorrises.model;

import com.github.jactorrises.client.facade.UserFacade;
import com.github.jactorrises.model.facade.UserFacadeImpl;
import com.github.jactorrises.model.service.GuestBookDomainService;
import com.github.jactorrises.model.service.UserDomainService;
import com.github.jactorrises.persistence.beans.PersistenceBeans;
import com.github.jactorrises.persistence.service.GuestBookDaoService;
import com.github.jactorrises.persistence.service.UserDaoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({PersistenceBeans.class})
public class JactorModel {

    @Bean
    public UserFacade userFacade(UserDaoService userDaoService) {
        return new UserFacadeImpl(userDaoService);
    }

    @Bean
    public GuestBookDomainService guestBookDomainService(GuestBookDaoService guestBookDaoService) {
        return new GuestBookDomainService(guestBookDaoService);
    }

    @Bean
    public UserDomainService userDaoService(UserDaoService userDaoService) {
        return new UserDomainService(userDaoService);
    }
}
