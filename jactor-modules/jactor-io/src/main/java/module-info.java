module com.github.jactor.rises.io {
    requires com.github.jactor.rises.client;
    requires spring.beans;
    requires spring.web;
    requires spring.context;

    exports com.github.jactor.rises.io.ctx to com.github.jactor.rises.model.facade;
    exports com.github.jactor.rises.io.rest to com.github.jactor.rises.model, com.github.jactor.rises.model.facade;
}