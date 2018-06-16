package com.github.jactor.rises.model.facade;

import com.github.jactor.rises.client.facade.UserFacade;
import com.github.jactor.rises.io.ctx.JactorIo;
import com.github.jactor.rises.io.rest.GuestBookRestService;
import com.github.jactor.rises.io.rest.UserRestService;
import com.github.jactor.rises.model.service.GuestBookDomainService;
import com.github.jactor.rises.model.service.UserDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(JactorIo.class)
public class JactorFacade {

    @Bean public UserFacade userFacade(UserRestService userRestService) {
        return new UserFacadeImpl(userDomainService(userRestService));
    }

    @Bean public GuestBookDomainService guestBookDomainService(GuestBookRestService guestBookRestService) {
        return new GuestBookDomainService(guestBookRestService);
    }

    @Bean public UserDomainService userDomainService(UserRestService userRestService) {
        return new UserDomainService(userRestService);
    }
}
