module com.github.jactor.rises.model.facade {
    requires com.github.jactor.rises.client;
    requires com.github.jactor.rises.io;
    requires com.github.jactor.rises.model;

    requires spring.context;

    exports com.github.jactor.rises.model.facade;
    exports com.github.jactor.rises.model.facade.menu to com.github.jactor.rises.web;
}