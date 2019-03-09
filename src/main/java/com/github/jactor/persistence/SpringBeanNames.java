package com.github.jactor.persistence;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

class SpringBeanNames {
    private final List<String> beanNames = new ArrayList<>();
    private final List<String> namesOfSpringBeans = new ArrayList<>();
    private List<String> fiveNames = new ArrayList<>();

    public void add(String name) {
        if (name.contains(".springframework.")) {
            namesOfSpringBeans.add(name);
        } else {
            fiveNames.add(name);
        }

        if (fiveNames.size() == 5) {
            beanNames.add(String.join(", ", fiveNames));
            fiveNames = new ArrayList<>();
        }
    }

    private List<String> mergeBeanNamesWithFiveNames() {
        beanNames.addAll(fiveNames);
        fiveNames = new ArrayList<>();
        return beanNames;
    }

    List<String> getBeanNames() {
        return unmodifiableList(mergeBeanNamesWithFiveNames());
    }

    List<String> getNamesOfSpringBeans() {
        return unmodifiableList(namesOfSpringBeans);
    }
}
