package nu.hjemme.business.domain;

import nu.hjemme.client.domain.GuestBook;
import nu.hjemme.client.domain.User;
import nu.hjemme.persistence.GuestBookEntity;

/** @author Tor Egil Jacobsen */
public class GuestBookDomain extends PersistentDomain<GuestBookEntity, Long> implements GuestBook {

    public GuestBookDomain(GuestBookEntity guestBookEntity) {
        super(guestBookEntity);
    }

    @Override
    public String getTitle() {
        return getEntity().getTitle();
    }

    @Override
    public User getUser() {
        return getEntity().getUser();
    }
}