package com.github.jactorrises;

import com.github.jactorrises.business.facade.UserFacadeImpl;
import com.github.jactorrises.client.facade.UserFacade;
import com.github.jactorrises.persistence.client.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import static java.util.Arrays.sort;

@SpringBootApplication
public class JactorModule {


    @Autowired
    @Bean(name = "jactor.userFacade") public UserFacade userFacade(UserDao userDao) {
        return new UserFacadeImpl(userDao);
    }

    public static void main(String... args) {
        display(SpringApplication.run(JactorModule.class, args));
    }

    private static void display(ApplicationContext ctx) {
        System.out.println("Available beans:");

        String[] beanDefinitions = ctx.getBeanDefinitionNames();
        sort(beanDefinitions);

        System.out.println(namesOf(beanDefinitions));
    }

    private static StringBuilder namesOf(String[] beanNames) {
        int count = 0;
        StringBuilder names = new StringBuilder();

        for (String name : beanNames) {
            if (count != 0) {
                names.append(", ");
            }

            count++;

            if (count == 5 || name.contains(".")) {
                names.append("\n-> ");
                count = 0;
            }

            names.append(name);
        }

        return names;
    }
}
