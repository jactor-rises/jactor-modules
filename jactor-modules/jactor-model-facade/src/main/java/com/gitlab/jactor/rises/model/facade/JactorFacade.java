package com.gitlab.jactor.rises.model.facade;

import com.gitlab.jactor.rises.model.datatype.Name;
import com.gitlab.jactor.rises.io.facade.UserFacade;
import com.gitlab.jactor.rises.io.ctx.JactorIo;
import com.gitlab.jactor.rises.io.rest.GuestBookRestService;
import com.gitlab.jactor.rises.io.rest.UserRestService;
import com.gitlab.jactor.rises.model.facade.impl.UserFacadeImpl;
import com.gitlab.jactor.rises.model.facade.menu.DefaultMenuFacade;
import com.gitlab.jactor.rises.model.facade.menu.Menu;
import com.gitlab.jactor.rises.model.service.GuestBookDomainService;
import com.gitlab.jactor.rises.model.service.UserDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static com.gitlab.jactor.rises.model.facade.menu.Menu.aMenu;
import static com.gitlab.jactor.rises.model.facade.menu.MenuItem.aMenuItem;

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

    public @Bean MenuFacade menuFacade() {
        return new DefaultMenuFacade(usersMenu());
    }

    private Menu usersMenu() {
        return aMenu()
                .withName(MENU_USERS)
                .add(aMenuItem().withName("menu.users.default")
                       .add(aMenuItem().withName("jactor").withTarget("user?choose=jactor").withDescription("menu.users.jactor.desc"))
                        .add(aMenuItem().withName("tip").withTarget("user?choose=tip").withDescription("menu.users.tip.desc"))
                ).build();
    }
}
