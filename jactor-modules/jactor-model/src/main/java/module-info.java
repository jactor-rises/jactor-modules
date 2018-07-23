module com.gitlab.jactor.rises.model {
    requires com.gitlab.jactor.rises.commons;
    requires com.gitlab.jactor.rises.io;
    requires org.apache.commons.lang3;

    exports com.gitlab.jactor.rises.model.domain to com.gitlab.jactor.rises.model.facade;
    exports com.gitlab.jactor.rises.model.domain.address to com.gitlab.jactor.rises.model.facade, com.gitlab.jactor.rises.web;
    exports com.gitlab.jactor.rises.model.domain.guestbook to com.gitlab.jactor.rises.model.facade;
    exports com.gitlab.jactor.rises.model.domain.person to com.gitlab.jactor.rises.model.facade, com.gitlab.jactor.rises.web;
    exports com.gitlab.jactor.rises.model.domain.user to com.gitlab.jactor.rises.model.facade, com.gitlab.jactor.rises.web;
    exports com.gitlab.jactor.rises.model.service to com.gitlab.jactor.rises.model.facade;
}