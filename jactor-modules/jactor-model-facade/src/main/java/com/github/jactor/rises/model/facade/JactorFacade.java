package com.github.jactor.rises.model.facade;

import com.github.jactor.rises.client.facade.UserFacade;
import com.github.jactor.rises.io.ctx.JactorIo;
import com.github.jactor.rises.io.rest.GuestBookRestService;
import com.github.jactor.rises.io.rest.UserRestService;
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

    @Bean public UserFacade userFacade(UserRestService userRestService) {
        return new UserFacadeImpl(userDomainService(userRestService));
    }

    @Bean public GuestBookDomainService guestBookDomainService(GuestBookRestService guestBookRestService) {
        return new GuestBookDomainService(guestBookRestService);
    }

    @Bean public UserDomainService userDomainService(UserRestService userRestService) {
        return new UserDomainService(userRestService);
    }

    @Bean public MenuFacade menuFacade() {
        return new DefaultMenuFacade(mainMenu(), personMenu());
    }

    private Menu mainMenu() {
        return aMenu()
                .withName("main")
                .add(aMenuItem()
                        .withName("menu.main.home").withDescription("menu.main.home.desc").withTarget("home")
                        .add(aMenuItem().withName("jactor").withTarget("user?choose=jactor"))
                        .add(aMenuItem().withName("tip").withTarget("user?choose=tip"))
                ).build();
    }

    private Menu personMenu() {
        return aMenu()
                .withName("person")
                .add(aMenuItem().withName("menu.person.about").withDescription("menu.person.about.desc").withTarget("about"))
                .build();
    }
}
