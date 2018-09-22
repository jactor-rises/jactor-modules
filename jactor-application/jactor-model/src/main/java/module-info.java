module jactor.rises.model {
    requires jactor.rises.commons;
    requires org.apache.commons.lang3;

    exports com.gitlab.jactor.rises.model.domain to jactor.rises.facade;
    exports com.gitlab.jactor.rises.model.domain.address to jactor.rises.facade, jactor.rises.web;
    exports com.gitlab.jactor.rises.model.domain.guestbook to jactor.rises.facade;
    exports com.gitlab.jactor.rises.model.domain.person to jactor.rises.facade, jactor.rises.web;
    exports com.gitlab.jactor.rises.model.domain.user to jactor.rises.facade, jactor.rises.web;
    exports com.gitlab.jactor.rises.model.service to jactor.rises.facade;
}