module com.github.jactor.rises.persistence.orm {
    requires com.github.jactor.rises.client;

    requires aspectjweaver;
    requires java.persistence;
    requires org.apache.commons.lang3;
    requires slf4j.api;

    requires spring.beans;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.data.commons;
    requires spring.web;
    requires spring.webmvc;
}