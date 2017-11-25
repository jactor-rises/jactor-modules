package com.github.jactorrises.model;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import static java.util.Arrays.sort;

@SpringBootApplication
public class JactorModel {
    public static void main(String... args) {
        display(SpringApplication.run(JactorModel.class, args));
    }

    private static void display(ApplicationContext ctx) {
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
