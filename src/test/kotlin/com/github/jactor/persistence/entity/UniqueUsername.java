package com.github.jactor.persistence.entity;

public final class UniqueUsername {
  private UniqueUsername() {
  }

  public static String generate(String name) {
    return name + '@' + Long.toHexString(System.currentTimeMillis());
  }
}
