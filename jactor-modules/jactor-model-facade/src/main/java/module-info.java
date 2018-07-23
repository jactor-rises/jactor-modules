module jactor.rises.facade {
    requires jactor.rises.commons;
    requires jactor.rises.io;
    requires jactor.rises.model;

    requires org.apache.commons.lang3;
    requires spring.context;

    exports com.gitlab.jactor.rises.model.facade;
}