module com.gitlab.jactor.rises.io {
    requires com.gitlab.jactor.rises.commons;
    requires spring.beans;
    requires spring.web;
    requires spring.context;

    exports com.gitlab.jactor.rises.io.ctx to com.gitlab.jactor.rises.model.facade;
    exports com.gitlab.jactor.rises.io.rest to com.gitlab.jactor.rises.model, com.gitlab.jactor.rises.model.facade;
}