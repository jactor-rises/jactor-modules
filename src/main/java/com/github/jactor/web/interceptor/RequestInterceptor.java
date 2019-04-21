package com.github.jactor.web.interceptor;

import com.github.jactor.web.Language;
import com.github.jactor.web.Request;
import com.github.jactor.web.RequestManager;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
@PropertySource("classpath:application.properties")
public class RequestInterceptor implements HandlerInterceptor {

  static final String CHOSEN_LANGUAGE = "chosenLanguage";
  static final String CURRENT_REQUEST = "currentRequest";

  private static final Language LANGUAGE_DEFAULT_IS_ENGLISH = new Language(new Locale("en"), "English");
  private static final List<Language> SUPPORTED_LANGUAGES = List.of(
      LANGUAGE_DEFAULT_IS_ENGLISH,
      new Language(new Locale("no"), "Norsk"),
      new Language(new Locale("th"), "ไทย")
  );

  private final String contextPath;

  @Autowired
  public RequestInterceptor(@Value("${server.servlet.context-path}") String contextPath) {
    this.contextPath = contextPath;
  }

  @Override
  public void postHandle(
      HttpServletRequest servletRequest,
      HttpServletResponse servletResponse,
      Object handler,
      ModelAndView modelAndView
  ) {
    RequestManager requestManager = new RequestManager(contextPath, servletRequest);
    Request request = new Request(requestManager.fetchCurrentUrl(), requestManager.fetchChosenView());
    Language chosenLanguage = fetchChosenLangugae(requestManager);

    modelAndView.getModelMap().addAttribute(CHOSEN_LANGUAGE, chosenLanguage);
    modelAndView.getModelMap().addAttribute(CURRENT_REQUEST, request);
  }

  private Language fetchChosenLangugae(RequestManager requestManager) {
    if (requestManager.noLanguageParameters()) {
      return requestManager.fetchFrom(SUPPORTED_LANGUAGES, LocaleContextHolder.getLocale(), LANGUAGE_DEFAULT_IS_ENGLISH);
    }

    return requestManager.fetchFromParameters(SUPPORTED_LANGUAGES, LANGUAGE_DEFAULT_IS_ENGLISH);
  }
}
