package com.github.jactor.web.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import com.github.jactor.shared.dto.UserDto;
import com.github.jactor.web.consumer.UserConsumer;
import com.github.jactor.web.menu.MenuFacade;
import com.github.jactor.web.menu.MenuItem;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@SpringBootTest
@DisplayName("The UserController")
class UserControllerTest {

  private static final String REQUEST_USER = "choose";
  private static final String USER_ENDPOINT = "/user";
  private static final String USER_JACTOR = "jactor";

  private MockMvc mockMvc;
  @MockBean
  private UserConsumer userRestServiceMock;
  @Autowired
  private MenuFacade menuFacade;
  @Value("${server.servlet.context-path}")
  private String contextPath;
  @Value("${spring.mvc.view.prefix}")
  private String prefix;
  @Value("${spring.mvc.view.suffix}")
  private String suffix;

  @BeforeEach
  void mockMvcWithViewResolver() {
    InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
    internalResourceViewResolver.setPrefix(prefix);
    internalResourceViewResolver.setSuffix(suffix);

    mockMvc = standaloneSetup(new UserController(userRestServiceMock, menuFacade, contextPath))
        .setViewResolvers(internalResourceViewResolver)
        .build();
  }

  @Test
  @DisplayName("should not fetch user by username if the username is missing from the request")
  void shouldNotFetchUserByUsernameIfTheUsernameIsMissongFromTheRequest() throws Exception {
    mockMvc.perform(get(USER_ENDPOINT)).andExpect(status().isOk());

    verify(userRestServiceMock, never()).find(anyString());
  }

  @Test
  @DisplayName("should not fetch user by username when the username is requested, but is only whitespace")
  void shouldNotFetchUserByUsernameIfTheUsernameInTheRequestIsNullOrAnEmptyString() throws Exception {
    mockMvc.perform(
        get(USER_ENDPOINT).requestAttr(REQUEST_USER, " \n \t")
    ).andExpect(status().isOk());

    verify(userRestServiceMock, never()).find(anyString());
  }

  @Test
  @DisplayName("should fetch user by username when the username is requested")
  void shouldFetchTheUserIfChooseParameterExist() throws Exception {
    when(userRestServiceMock.find(USER_JACTOR)).thenReturn(Optional.of(new UserDto()));

    ModelAndView modelAndView = mockMvc.perform(
        get(USER_ENDPOINT).param(REQUEST_USER, USER_JACTOR)
    ).andExpect(status().isOk()).andReturn().getModelAndView();

    var model = modelAndView != null ? modelAndView.getModel() : new HashMap<>();

    assertThat(model.get("user")).isNotNull();
  }

  @Test
  @DisplayName("should fetch user by username, but not find user")
  void shouldFetchTheUserByUsernameButNotFindUser() throws Exception {
    when(userRestServiceMock.find(anyString())).thenReturn(Optional.empty());

    ModelAndView modelAndView = mockMvc.perform(
        get(USER_ENDPOINT).param(REQUEST_USER, "someone")
    ).andExpect(status().isOk()).andReturn().getModelAndView();

    assertThat(Objects.requireNonNull(modelAndView).getModel().get("unknownUser")).isEqualTo("someone");
  }

  @Test
  @DisplayName("should add context path to target of the user names")
  void shouldAddContextPathToTheMenuItemsInTheUsersMenu() throws Exception {
    when(userRestServiceMock.findAllUsernames()).thenReturn(List.of("jactor"));

    ModelAndView modelAndView = mockMvc.perform(get(USER_ENDPOINT))
        .andExpect(status().isOk()).andReturn().getModelAndView();

    Map<String, Object> model = Objects.requireNonNull(modelAndView).getModel();
    assertThat(model).as("model").isNotNull();
    Object menuItem = model.get("usersMenu");

    assertThat(menuItem).as("users menu among: " + model.keySet()).isEqualTo(List.of(
        new MenuItem("menu.users.choose", List.of(
            new MenuItem("jactor", contextPath + "/user?choose=jactor", "user.choose.desc")
        ))
    ));
  }
}
