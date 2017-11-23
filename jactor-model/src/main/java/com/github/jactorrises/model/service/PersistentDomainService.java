package com.github.jactorrises.model.service;

import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.persistence.client.entity.Persistent;
import com.github.jactorrises.model.domain.PersistentDomain;
import com.github.jactorrises.model.domain.guestbook.GuestBookDomain;
import com.github.jactorrises.model.domain.guestbook.GuestBookEntryDomain;
import com.github.jactorrises.model.domain.user.UserDomain;
import com.github.jactorrises.persistence.repository.HibernateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersistentDomainService {
    private final HibernateRepository hibernateRepository;

    @Autowired
    public PersistentDomainService(HibernateRepository hibernateRepository) {
        this.hibernateRepository = hibernateRepository;
    }

    void saveOrUpdate(PersistentDomain<?, ?> persistentDomain) {
        hibernateRepository.saveOrUpdate(persistentDomain.getEntity());
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
        return hibernateRepository.findUsing(userName).map(UserDomain::new);
    }

    <I, T extends Persistent<I>> T findUnique(T persistentDomain) {
        I id = persistentDomain.getId();

        return hibernateRepository.load(persistentDomain.ge, id);
    }
}
