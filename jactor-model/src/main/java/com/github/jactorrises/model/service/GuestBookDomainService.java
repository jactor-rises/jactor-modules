package com.github.jactorrises.model.service;

import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.client.domain.Persistent;
import com.github.jactorrises.model.domain.DefaultPersistentDomain;
import com.github.jactorrises.model.domain.guestbook.GuestBookDomain;
import com.github.jactorrises.model.domain.guestbook.GuestBookEntryDomain;
import com.github.jactorrises.model.domain.user.UserDomain;
import com.github.jactorrises.persistence.client.dao.PersistentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Optional;

@Service
public class PersistentDomainService {
    private final PersistentDao persistentDao;

    @Autowired
    public PersistentDomainService(PersistentDao persistentDao) {
        this.persistentDao = persistentDao;
    }

    void saveOrUpdate(DefaultPersistentDomain<?, ?> defaultPersistentDomain) {
        persistentDao.saveOrUpdate(defaultPersistentDomain.getEntity());
    }

    GuestBookDomain saveOrUpdateGuestBook(GuestBookDomain guestBookDomain) {
        saveOrUpdate(guestBookDomain.getUser().getPerson().getAddress());
        saveOrUpdate(guestBookDomain.getUser().getPerson());
        saveOrUpdate(guestBookDomain.getUser());
        saveOrUpdate(guestBookDomain);

        return guestBookDomain;
    }

    GuestBookEntryDomain saveOrUpdateGuestBookEntry(GuestBookEntryDomain guestBookEntryDomain) {
        saveOrUpdateGuestBook(guestBookEntryDomain.getGuestBook());
        saveOrUpdate(guestBookEntryDomain);

        return guestBookEntryDomain;
    }

    Optional<UserDomain> findUser(UserName userName) {
        return persistentDao.findUsing(userName).map(UserDomain::new);
    }

    <T extends Persistent<I>, I extends Serializable> T findUnique(T persistentDomain) {
        I id = persistentDomain.getId();

        return persistentDao.load(persistentDomain.getPersistent().getClass(), id);
    }
}
