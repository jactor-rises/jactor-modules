package com.github.jactor.web.interceptor;

import static com.github.jactor.web.interceptor.RequestInterceptor.CHOSEN_LANGUAGE;
import static com.github.jactor.web.interceptor.RequestInterceptor.CURRENT_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import com.github.jactor.web.Language;
import com.github.jactor.web.LanguageKt;
import com.github.jactor.web.Request;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.ModelAndView;

@SpringBootTest
@DisplayName("A RequestInterceptor")
class RequestInterceptorTest {

  @Autowired
  private RequestInterceptor requestInterceptorToTest;
  @MockBean
  private HttpServletRequest httpServletRequestMock;

  @BeforeEach
  void mockRequest() {
    when(httpServletRequestMock.getRequestURI()).thenReturn("/page");
    when(httpServletRequestMock.getQueryString()).thenReturn("some=param");
  }

  @Test
  @DisplayName("should add current url without language to the model")
  void shouldAddCurrentUrlWithoutLangugaeToTheModel() {
    ModelAndView modelAndView = new ModelAndView();

    when(httpServletRequestMock.getRequestURI()).thenReturn("/somewhere");
    when(httpServletRequestMock.getQueryString()).thenReturn("out=there&lang=something&another=param");

    requestInterceptorToTest.postHandle(httpServletRequestMock, null, null, modelAndView);

    Map<String, Object> model = modelAndView.getModel();

    var currentRequest = Optional.ofNullable(model.get(CURRENT_REQUEST));

    assertThat(currentRequest).hasValueSatisfying(request-> assertAll(
        () -> assertThat(request).isInstanceOf(Request.class),
        () -> assertThat((Request) request).extracting(Request::getCurrentUrl).isEqualTo("/somewhere?out=there&another=param")
    ));
  }

  @Test
  @DisplayName("should add Norwegian language to model")
  void shouldAddNorwegianLanguageToModel() {
    LocaleContextHolder.setLocale(new Locale("no"));
    ModelAndView modelAndView = new ModelAndView();

    requestInterceptorToTest.postHandle(httpServletRequestMock, null, null, modelAndView);

    Language language = (Language) modelAndView.getModel().getOrDefault(CHOSEN_LANGUAGE, new Language(new Locale("svada"), "there"));

    assertAll(
        () -> assertThat(language.getName()).as("name").isEqualTo(LanguageKt.NORSK),
        () -> assertThat(language.getLocale()).isEqualTo(new Locale("no"))
    );
  }

  @Test
  @DisplayName("should add English language to model")
  void shouldAddEnglishLanguageToModel() {
    LocaleContextHolder.setLocale(new Locale("en"));
    ModelAndView modelAndView = new ModelAndView();

    requestInterceptorToTest.postHandle(httpServletRequestMock, null, null, modelAndView);

    Language language = (Language) modelAndView.getModel().getOrDefault(CHOSEN_LANGUAGE, new Language(new Locale("svada"), "there"));

    assertAll(
        () -> assertThat(language.getName()).as("name").isEqualTo(LanguageKt.ENGLISH),
        () -> assertThat(language.getLocale()).isEqualTo(new Locale("en"))
    );
  }

  @Test
  @DisplayName("should add Thai language to model")
  void shouldAddThaiLanguageToModel() {
    LocaleContextHolder.setLocale(new Locale("th"));
    ModelAndView modelAndView = new ModelAndView();

    requestInterceptorToTest.postHandle(httpServletRequestMock, null, null, modelAndView);

    Language language = (Language) modelAndView.getModel().getOrDefault(CHOSEN_LANGUAGE, new Language(new Locale("svada"), "there"));

    assertAll(
        () -> assertThat(language.getName()).as("name").isEqualTo(LanguageKt.THAI),
        () -> assertThat(language.getLocale()).isEqualTo(new Locale("th"))
    );
  }

  @Test
  @DisplayName("should add English language to model when locale is not supported")
  void shouldAddEnglishLanguageToModelWhenLocaleIsUnsupported() {
    ModelAndView modelAndView = new ModelAndView();

    requestInterceptorToTest.postHandle(httpServletRequestMock, null, null, modelAndView);

    Language language = (Language) modelAndView.getModel().getOrDefault(CHOSEN_LANGUAGE, new Language(new Locale("svada"), "there"));

    assertAll(
        () -> assertThat(language.getName()).as("name").isEqualTo(LanguageKt.ENGLISH),
        () -> assertThat(language.getLocale()).isEqualTo(new Locale("en"))
    );
  }

  @Test
  @DisplayName("should add Thai language to model from language parameters")
  void shouldAddThaiLanguageToModelFromParameters() {
    LocaleContextHolder.setLocale(new Locale("no"));
    ModelAndView modelAndView = new ModelAndView();

    when(httpServletRequestMock.getQueryString()).thenReturn("select=something&lang=th");

    requestInterceptorToTest.postHandle(httpServletRequestMock, null, null, modelAndView);

    Language language = (Language) modelAndView.getModel().getOrDefault(CHOSEN_LANGUAGE, new Language(new Locale("svada"), "there"));

    assertAll(
        () -> assertThat(language.getName()).as("name").isEqualTo(LanguageKt.THAI),
        () -> assertThat(language.getLocale()).isEqualTo(new Locale("th"))
    );
  }

  @Test
  @DisplayName("should add English language to model from language parameters")
  void shouldAddEnglishLanguageToModelFromParameters() {
    LocaleContextHolder.setLocale(new Locale("no"));
    ModelAndView modelAndView = new ModelAndView();

    when(httpServletRequestMock.getQueryString()).thenReturn("select=something&lang=en");

    requestInterceptorToTest.postHandle(httpServletRequestMock, null, null, modelAndView);

    Language language = (Language) modelAndView.getModel().getOrDefault(CHOSEN_LANGUAGE, new Language(new Locale("svada"), "there"));

    assertAll(
        () -> assertThat(language.getName()).as("name").isEqualTo(LanguageKt.ENGLISH),
        () -> assertThat(language.getLocale()).isEqualTo(new Locale("en"))
    );
  }

  @Test
  @DisplayName("should add Norwegian language to model from language parameters")
  void shouldAddNorwegianLanguageToModelFromParameters() {
    LocaleContextHolder.setLocale(new Locale("en"));
    ModelAndView modelAndView = new ModelAndView();

    when(httpServletRequestMock.getQueryString()).thenReturn("select=something&lang=no");

    requestInterceptorToTest.postHandle(httpServletRequestMock, null, null, modelAndView);

    Language language = (Language) modelAndView.getModel().getOrDefault(CHOSEN_LANGUAGE, new Language(new Locale("svada"), "there"));

    assertAll(
        () -> assertThat(language.getName()).as("name").isEqualTo(LanguageKt.NORSK),
        () -> assertThat(language.getLocale()).isEqualTo(new Locale("no"))
    );
  }

  @Test
  @DisplayName("should add English language to model when unknown language parameter")
  void shouldAddEnglishLanguageToModelWhenUnknownLanguageParameter() {
    LocaleContextHolder.setLocale(new Locale("th"));
    ModelAndView modelAndView = new ModelAndView();

    when(httpServletRequestMock.getQueryString()).thenReturn("select=something&lang=fi");

    requestInterceptorToTest.postHandle(httpServletRequestMock, null, null, modelAndView);

    Language language = (Language) modelAndView.getModel().getOrDefault(CHOSEN_LANGUAGE, new Language(new Locale("svada"), "there"));

    assertAll(
        () -> assertThat(language.getName()).as("name").isEqualTo(LanguageKt.ENGLISH),
        () -> assertThat(language.getLocale()).isEqualTo(new Locale("en"))
    );
  }

  @Test
  @DisplayName("should add chosen view to the model")
  void shouldAddChosenViewToTheModel() {
    ModelAndView modelAndView = new ModelAndView();
    when(httpServletRequestMock.getRequestURI()).thenReturn("/user");

    requestInterceptorToTest.postHandle(httpServletRequestMock, null, null, modelAndView);

    Map<String, Object> model = modelAndView.getModel();

    var currentRequest = Optional.ofNullable(model.get(CURRENT_REQUEST));

    assertThat(currentRequest).hasValueSatisfying(request-> assertAll(
        () -> assertThat(request).isInstanceOf(Request.class),
        () -> assertThat((Request) request).extracting(Request::getChosenView).isEqualTo("user")
    ));
  }
}
