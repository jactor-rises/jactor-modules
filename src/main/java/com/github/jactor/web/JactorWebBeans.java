package com.github.jactor.web;

import com.github.jactor.web.consumer.UserConsumer;
import com.github.jactor.web.menu.Menu;
import com.github.jactor.web.menu.MenuFacade;
import com.github.jactor.web.menu.MenuItem;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RootUriTemplateHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;

@Configuration
@PropertySource("classpath:application.properties")
public class JactorWebBeans {

  public static final String USERS_MENU_NAME = "user";

  @Bean
  public MenuFacade menuFacade() {
    return new MenuFacade(List.of(usersMenu()));
  }

  private Menu usersMenu() {
    return new Menu(USERS_MENU_NAME)
        .addItem(new MenuItem("menu.users.default"))
        .addItem(new MenuItem("jactor", "user?choose=jactor", "menu.users.jactor.desc"))
        .addItem(new MenuItem("tip", "user?choose=tip", "menu.users.tip.desc"));
  }

  @Bean
  public UserConsumer userConsumer(RestTemplate restTemplate) {
    return new UserConsumer(restTemplate);
  }

  @Bean
  @Scope("prototype")
  public RestTemplate restTemplate(@Value("${jactor-persistence.url.root}") String rootUrlPersistence) {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.setUriTemplateHandler(new RootUriTemplateHandler(rootUrlPersistence));

    return restTemplate;
  }
}
