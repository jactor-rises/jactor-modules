module com.gitlab.jactor.rises.model.facade {
    requires com.gitlab.jactor.rises.commons;
    requires com.gitlab.jactor.rises.io;
    requires com.gitlab.jactor.rises.model;

    requires spring.context;

    exports com.gitlab.jactor.rises.model.facade;
    exports com.gitlab.jactor.rises.model.facade.menu to com.gitlab.jactor.rises.web;
}