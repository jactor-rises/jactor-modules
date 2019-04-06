package com.github.jactor.persistence;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

class SpringBeanNames {

  private final List<String> beanNames = new ArrayList<>();
  private final List<String> tenNames = new ArrayList<>();

  public void add(String name) {
    if (name.contains(".")) {
      int index = name.lastIndexOf('.');
      tenNames.add(name.substring(index + 1));
    } else {
      tenNames.add(name);
    }

    if (tenNames.size() == 10) {
      beanNames.add(String.join(", ", tenNames));
      tenNames.clear();
    }
  }

  private List<String> mergeBeanNamesWithFiveNames() {
    beanNames.add(String.join(", ", tenNames));
    tenNames.clear();

    return beanNames;
  }

  List<String> getBeanNames() {
    return unmodifiableList(mergeBeanNamesWithFiveNames());
  }
}
