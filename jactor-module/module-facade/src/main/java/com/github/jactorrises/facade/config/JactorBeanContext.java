package com.github.jactorrises.facade.config;

import com.github.jactorrises.business.facade.UserFacadeImpl;
import com.github.jactorrises.client.facade.UserFacade;
import com.github.jactorrises.persistence.client.dao.UserDao;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.Locale;

@Configuration
@EnableAspectJAutoProxy
public class JactorBeanContext {

    public JactorBeanContext() {
        Locale.setDefault(Locale.ENGLISH);
    }

    @Bean(name = "jactor.userFacade") public UserFacade userFacade(SessionFactory sessionFactory) {
        //return new UserFacadeImpl(PersistentDataService.getInstance().provideInstanceFor(UserDao.class, sessionFactory));
        return new UserFacadeImpl(null); // todo: replace with spring boot
    }

    @Bean(name = "jactor.userDao") public UserDao userDao(SessionFactory sessionFactory) {
        return null; // todo: remove, shold be a service from the persistence layer...
    }
}
