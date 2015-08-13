package nu.hjemme.facade.config;

import nu.hjemme.business.service.MenuFacadeImpl;
import nu.hjemme.business.service.UserFacadeImpl;
import nu.hjemme.client.datatype.UserName;
import nu.hjemme.client.domain.User;
import nu.hjemme.client.domain.menu.Menu;
import nu.hjemme.client.service.MenuFacade;
import nu.hjemme.client.service.UserFacade;
import nu.hjemme.facade.aop.ChosenMenuItemAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Tor Egil Jacobsen
 */
@Configuration
@EnableAspectJAutoProxy
public class HjemmeAppContext {

    public HjemmeAppContext() {
        Locale.setDefault(Locale.ENGLISH);
    }

    @Bean(name = "hjemme.menuFacade")
    @SuppressWarnings("unused") // brukes av spring
    public MenuFacade menuFacade(List<Menu> menus) {
        return new MenuFacadeImpl(menus);
    }

    @Bean(name = "hjemme.userFacade")
    @SuppressWarnings("unused") // brukes av spring
    public UserFacade userFacade() {
        Map<UserName, User> defaultUsers = new HashMap<>();
        defaultUsers.put(DefaultUsers.getJactor().getUserName(), DefaultUsers.getJactor());
        defaultUsers.put(DefaultUsers.getTip().getUserName(), DefaultUsers.getTip());

        return new UserFacadeImpl(defaultUsers);
    }

    @Bean(name = "hjemme.aop.chosenMenuItems")
    @SuppressWarnings("unused") // brukes av spring
    public ChosenMenuItemAspect chosenMenuItemAspect() {
        return new ChosenMenuItemAspect();
    }
}