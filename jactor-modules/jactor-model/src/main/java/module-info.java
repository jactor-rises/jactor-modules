module com.github.jactor.rises.model {
    requires com.github.jactor.rises.client;
    requires com.github.jactor.rises.io;
    requires org.apache.commons.lang3;

    exports com.github.jactor.rises.model.domain.address to com.github.jactor.rises.model.facade, com.github.jactor.rises.web;
    exports com.github.jactor.rises.model.domain.guestbook to com.github.jactor.rises.model.facade;
    exports com.github.jactor.rises.model.domain.person to com.github.jactor.rises.model.facade, com.github.jactor.rises.web;
    exports com.github.jactor.rises.model.domain.user to com.github.jactor.rises.model.facade, com.github.jactor.rises.web;
    exports com.github.jactor.rises.model.service to com.github.jactor.rises.model.facade;
}