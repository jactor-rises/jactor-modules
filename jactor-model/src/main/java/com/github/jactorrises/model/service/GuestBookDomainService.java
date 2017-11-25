package com.github.jactorrises.model.service;

import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.model.domain.PersistentDomain;
import com.github.jactorrises.model.domain.guestbook.GuestBookDomain;
import com.github.jactorrises.model.domain.guestbook.GuestBookEntryDomain;
import com.github.jactorrises.model.domain.user.UserDomain;
import com.github.jactorrises.persistence.client.dao.PersistentDao;
import com.github.jactorrises.persistence.client.entity.GuestBookEntity;
import com.github.jactorrises.persistence.client.entity.GuestBookEntryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GuestBookDomainService {
    private final PersistentDao persistentDao;

    @Autowired public GuestBookDomainService(PersistentDao persistentDao) {
        this.persistentDao = persistentDao;
    }

    void saveOrUpdate(PersistentDomain<?> persistentDomain) {
        persistentDao.saveOrUpdate(persistentDomain.getPersistence());
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

    GuestBookDomain findGuestBook(Long id) {
        GuestBookEntity guestBookEntity = persistentDao.load(GuestBookEntity.class, id);
        return new GuestBookDomain(guestBookEntity);
    }

    GuestBookEntryDomain findGuestBookEntry(Long id) {
        GuestBookEntryEntity guestBookEntryEntity = persistentDao.load(GuestBookEntryEntity.class, id);
        return new GuestBookEntryDomain(guestBookEntryEntity);
    }
}

