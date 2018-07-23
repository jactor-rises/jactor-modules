package com.gitlab.jactor.rises.model.facade;

import com.gitlab.jactor.rises.commons.datatype.Name;
import com.gitlab.jactor.rises.io.ctx.JactorIo;
import com.gitlab.jactor.rises.io.rest.GuestBookRestService;
import com.gitlab.jactor.rises.io.rest.UserRestService;
import com.gitlab.jactor.rises.model.facade.impl.UserFacadeImpl;
import com.gitlab.jactor.rises.model.service.GuestBookDomainService;
import com.gitlab.jactor.rises.model.service.UserDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(JactorIo.class)
public class JactorFacade {

    public static final Name MENU_USERS = new Name("users");

    public @Bean UserFacade userFacade(UserRestService userRestService) {
        return new UserFacadeImpl(userDomainService(userRestService));
    }

    public @Bean GuestBookDomainService guestBookDomainService(GuestBookRestService guestBookRestService) {
        return new GuestBookDomainService(guestBookRestService);
    }

    public @Bean UserDomainService userDomainService(UserRestService userRestService) {
        return new UserDomainService(userRestService);
    }
}
