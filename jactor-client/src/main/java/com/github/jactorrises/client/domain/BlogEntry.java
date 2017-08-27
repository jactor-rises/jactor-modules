package com.github.jactorrises.client.domain;

public interface BlogEntry extends Persistent<Long>, Entry {

    Blog getBlog();
}
