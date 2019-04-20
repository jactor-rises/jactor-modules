package com.github.jactor.web.controller;

import static java.util.stream.Collectors.toList;

import com.github.jactor.web.JactorWebBeans;
import com.github.jactor.web.consumer.UserConsumer;
import com.github.jactor.web.dto.UserModel;
import com.github.jactor.web.menu.MenuFacade;
import com.github.jactor.web.menu.MenuItem;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

  private final MenuFacade menuFacade;
  private final String contextPath;
  private final UserConsumer userConsumer;

  @Autowired
  public UserController(UserConsumer userConsumer, MenuFacade menuFacade, @Value("${server.servlet.context-path}") String contextPath) {
    this.userConsumer = userConsumer;
    this.menuFacade = menuFacade;
    this.contextPath = contextPath;
  }

  @GetMapping(value = "/user")
  public ModelAndView get(@RequestParam(name = "choose", required = false) String username) {
    ModelAndView modelAndView = new ModelAndView("user");

    if (!StringUtils.isEmpty(username)) {
      populateUser(username, modelAndView);
    }

    populateUserMenu(modelAndView);
    populateDefaultUsers(modelAndView);

    return modelAndView;
  }

  private void populateUser(String username, ModelAndView modelAndView) {
    var user = userConsumer.find(username);
    Map<String, Object> modelMap = modelAndView.getModel();

    if (user.isPresent()) {
      modelMap.put("user", new UserModel(user.get()));
    } else {
      modelMap.put("unknownUser", username);
    }
  }

  private void populateUserMenu(ModelAndView modelAndView) {
    var usernames = userConsumer.findAllUsernames();
    modelAndView.addObject("usersMenu", List.of(
        new MenuItem(
            "menu.users.choose",
            usernames.stream().map(this::chooseUserItem).collect(toList()))
        )
    );
  }

  private MenuItem chooseUserItem(String username) {
    return new MenuItem(username, String.format("%s/user?choose=%s", contextPath, username), "user.choose.desc");
  }

  private void populateDefaultUsers(ModelAndView modelAndView) {
    var menuItems = menuFacade.fetchMenuItemsByName(JactorWebBeans.USERS_MENU_NAME);
    modelAndView.addObject("defaultUsers", menuItems);
  }
}
