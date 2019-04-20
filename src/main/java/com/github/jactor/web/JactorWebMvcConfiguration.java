package com.github.jactor.web;

import com.github.jactor.web.interceptor.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafView;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
@PropertySource("classpath:application.properties")
public class JactorWebMvcConfiguration implements WebMvcConfigurer {

  private final RequestInterceptor requestInterceptor;
  private final String prefix;
  private final String suffix;

  public JactorWebMvcConfiguration(
      @Autowired RequestInterceptor requestInterceptor,
      @Value("${spring.mvc.view.prefix}") String prefix,
      @Value("${spring.mvc.view.suffix}") String suffix
  ) {
    this.requestInterceptor = requestInterceptor;
    this.prefix = prefix;
    this.suffix = suffix;
  }

  @Bean
  public ClassLoaderTemplateResolver templateResolver() {
    ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();

    templateResolver.setCacheable(false);
    templateResolver.setCharacterEncoding("UTF-8");

    templateResolver.setPrefix(prefix);
    templateResolver.setSuffix(suffix);

    templateResolver.setTemplateMode(TemplateMode.HTML);
    templateResolver.setOrder(0);

    return templateResolver;
  }

  @Bean
  public SpringTemplateEngine templateEngine() {
    SpringTemplateEngine templateEngine = new SpringTemplateEngine();
    templateEngine.setTemplateResolver(templateResolver());

    return templateEngine;
  }

  @Bean
  public ThymeleafViewResolver viewResolver() {
    ThymeleafViewResolver thymeleafViewResolver = new ThymeleafViewResolver();
    thymeleafViewResolver.setViewClass(ThymeleafView.class);
    thymeleafViewResolver.setTemplateEngine(templateEngine());

    return thymeleafViewResolver;
  }

  @Bean
  public LocaleResolver localeResolver() {
    return new CookieLocaleResolver();
  }

  @Bean
  public LocaleChangeInterceptor localeChangeInterceptor() {
    LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
    lci.setParamName("lang");

    return lci;
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry
        .addResourceHandler("static/**")
        .addResourceLocations("resources");
  }

  @Override
  public void configureViewResolvers(ViewResolverRegistry registry) {
    registry.viewResolver(viewResolver());
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(localeChangeInterceptor());
    registry.addInterceptor(requestInterceptor);
  }
}
