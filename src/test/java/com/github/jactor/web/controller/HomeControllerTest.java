package com.github.jactor.web.controller;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import com.github.jactor.web.i18n.MyMessages;
import com.github.jactor.web.model.HomePageModel;
import java.util.Map;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@SpringBootTest
@DisplayName("The HomeController")
class HomeControllerTest {

  private MockMvc mockMvc;

  @Autowired
  private MyMessages myMessages;
  @Value("${spring.mvc.view.prefix}")
  private String prefix;
  @Value("${spring.mvc.view.suffix}")
  private String suffix;

  @BeforeEach
  void mockMvcWithViewResolver() {
    InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
    internalResourceViewResolver.setPrefix(prefix);
    internalResourceViewResolver.setSuffix(suffix);

    mockMvc = standaloneSetup(new HomeController(myMessages))
        .setViewResolvers(internalResourceViewResolver)
        .build();
  }

  @Test
  @DisplayName("should create a homepage dto with my messages")
  void shouldCreateHomepageDtoWithMyMessages() throws Exception {
    ModelAndView modelAndView = mockMvc.perform(
        get("/home")).andExpect(status().isOk()
    ).andReturn().getModelAndView();

    assertAll(
        () -> assertThat(modelAndView).as("modelAndView").isNotNull(),
        () -> {
          Map<String, Object> model = Objects.requireNonNull(modelAndView).getModel();

          assertThat(model).isNotNull();

          HomePageModel homePageModel = (HomePageModel) model.get("homepage");

          assertAll(
              () -> assertThat(homePageModel.getTechnologies()).as("technologies").hasSize(10)
          );
        }
    );
  }
}