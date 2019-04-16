package com.github.jactor.web.interceptor;

import com.github.jactor.web.model.Language;
import com.github.jactor.web.model.RequestManager;
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

  private static final String CHOSEN_VIEW = "chosenView";
  static final String CHOSEN_LANGUAGE = "chosenLanguage";
  static final String CURRENT_URL = "currentUrl";
  static final String LANGUAGE_ENGLISH = "English";
  static final String LANGUAGE_NORSK = "Norsk";
  static final String LANGUAGE_THAI = "ไทย";

  private static final Language LANGUAGE_DEFAULT_IS_ENGLISH = new Language(new Locale("en"), LANGUAGE_ENGLISH);
  private static final List<Language> SUPPORTED_LANGUAGES = List.of(
      LANGUAGE_DEFAULT_IS_ENGLISH,
      new Language(new Locale("no"), LANGUAGE_NORSK),
      new Language(new Locale("th"), LANGUAGE_THAI)
  );

  private final String contextPath;

  @Autowired
  public RequestInterceptor(@Value("${server.servlet.context-path}") String contextPath) {
    this.contextPath = contextPath;
  }

  @Override
  public void postHandle(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler,
      ModelAndView modelAndView
  ) {
    if (modelAndView != null) {
      RequestManager requestManager = new RequestManager(contextPath, request);
      modelAndView.addObject(CHOSEN_VIEW, requestManager.fetchChosenView());
      modelAndView.addObject(CURRENT_URL, requestManager.fetchCurrentUrl());

      Language chosenLanguage;

      if (requestManager.noLanguageParameters()) {
        chosenLanguage = requestManager.fetchFrom(SUPPORTED_LANGUAGES, LocaleContextHolder.getLocale(), LANGUAGE_DEFAULT_IS_ENGLISH);
      } else {
        chosenLanguage = requestManager.fetchFromParameters(SUPPORTED_LANGUAGES, LANGUAGE_DEFAULT_IS_ENGLISH);
      }

      modelAndView.addObject(CHOSEN_LANGUAGE, chosenLanguage);
    }
  }
}
