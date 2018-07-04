package com.github.jactor.rises.model.facade;

import com.github.jactor.rises.client.datatype.Name;
import com.github.jactor.rises.client.facade.UserFacade;
import com.github.jactor.rises.io.ctx.JactorIo;
import com.github.jactor.rises.io.rest.GuestBookRestService;
import com.github.jactor.rises.io.rest.UserRestService;
import com.github.jactor.rises.model.facade.impl.UserFacadeImpl;
import com.github.jactor.rises.model.facade.menu.DefaultMenuFacade;
import com.github.jactor.rises.model.facade.menu.Menu;
import com.github.jactor.rises.model.service.GuestBookDomainService;
import com.github.jactor.rises.model.service.UserDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static com.github.jactor.rises.model.facade.menu.Menu.aMenu;
import static com.github.jactor.rises.model.facade.menu.MenuItem.aMenuItem;

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
                .add(
                        aMenuItem()
                                .withName("menu.users.default").withDescription("menu.users.default.desc").withTarget("user")
                                .add(aMenuItem().withName("jactor").withTarget("user?choose=jactor"))
                                .add(aMenuItem().withName("tip").withTarget("user?choose=tip"))
                ).add(
                        aMenuItem()
                                .withName("menu.users.choose").withDescription("menu.users.choose.desc").withTarget("user")
                                .add(aMenuItem().withName("#201").withTarget("user?choose=#201"))
                ).build();
    }
}
