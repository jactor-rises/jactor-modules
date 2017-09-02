package com.github.jactorrises.facade;

import com.github.jactorrises.business.facade.UserFacadeImpl;
import com.github.jactorrises.client.facade.UserFacade;
import com.github.jactorrises.persistence.client.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Locale;

@SpringBootApplication
public class JactorModule {

    public JactorModule() {
        Locale.setDefault(Locale.ENGLISH);
    }

    @Autowired
    @Bean(name = "jactor.userFacade") public UserFacade userFacade(UserDao userDao) {
        return new UserFacadeImpl(userDao);
    }

    public static void main(String... args) {
        SpringApplication.run(JactorModule.class, args);
    }
}
