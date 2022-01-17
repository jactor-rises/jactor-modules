package com.github.jactor.web

import com.github.jactor.web.interceptor.RequestInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.CookieLocaleResolver
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
import org.thymeleaf.spring5.SpringTemplateEngine
import org.thymeleaf.spring5.view.ThymeleafView
import org.thymeleaf.spring5.view.ThymeleafViewResolver
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver

@Configuration
@PropertySource("classpath:application.properties")
class JactorWebMvcConfiguration(
    @param:Autowired private val requestInterceptor: RequestInterceptor,
    @param:Value("\${spring.mvc.view.prefix}") private val prefix: String,
    @param:Value("\${spring.mvc.view.suffix}") private val suffix: String
) : WebMvcConfigurer {

    @Bean
    fun templateResolver(): ClassLoaderTemplateResolver {
        val templateResolver = ClassLoaderTemplateResolver()

        templateResolver.isCacheable = false
        templateResolver.characterEncoding = "UTF-8"

        templateResolver.prefix = prefix
        templateResolver.suffix = suffix

        templateResolver.templateMode = TemplateMode.HTML
        templateResolver.order = 0

        return templateResolver
    }

    @Bean
    fun templateEngine(): SpringTemplateEngine {
        val templateEngine = SpringTemplateEngine()
        templateEngine.setTemplateResolver(templateResolver())

        return templateEngine
    }

    @Bean
    fun viewResolver(): ThymeleafViewResolver {
        val thymeleafViewResolver = ThymeleafViewResolver()
        thymeleafViewResolver.setViewClass(ThymeleafView::class.java)
        thymeleafViewResolver.templateEngine = templateEngine()

        return thymeleafViewResolver
    }

    @Bean
    fun localeResolver(): LocaleResolver {
        return CookieLocaleResolver()
    }

    @Bean
    fun localeChangeInterceptor(): LocaleChangeInterceptor {
        val localeChangeInterceptor = LocaleChangeInterceptor()
        localeChangeInterceptor.paramName = "lang"

        return localeChangeInterceptor
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry
            .addResourceHandler("static/**")
            .addResourceLocations("resources")
    }

    override fun configureViewResolvers(registry: ViewResolverRegistry) {
        registry.viewResolver(viewResolver())
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(localeChangeInterceptor())
        registry.addInterceptor(requestInterceptor)
    }
}
