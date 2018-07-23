module jactor.rises.io {
    requires jactor.rises.commons;
    requires spring.beans;
    requires spring.web;
    requires spring.context;

    exports com.gitlab.jactor.rises.io.ctx to jactor.rises.facade;
    exports com.gitlab.jactor.rises.io.rest to jactor.rises.model, jactor.rises.facade;
}