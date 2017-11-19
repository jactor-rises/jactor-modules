package com.github.jactorrises.model.service;

import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.model.domain.PersistentDomain;
import com.github.jactorrises.model.domain.guestbook.GuestBookBuilder;
import com.github.jactorrises.model.domain.guestbook.GuestBookDomain;
import com.github.jactorrises.model.domain.guestbook.GuestBookEntryBuilder;
import com.github.jactorrises.model.domain.guestbook.GuestBookEntryDomain;
import com.github.jactorrises.model.domain.user.UserDomain;
import com.github.jactorrises.persistence.entity.guestbook.GuestBookEntryOrm;
import com.github.jactorrises.persistence.entity.guestbook.GuestBookOrm;
import com.github.jactorrises.persistence.repository.HibernateRepository;
import com.github.jactorrises.persistence.repository.RepositoryCriterion;
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

    GuestBookDomain findUnique(GuestBookCriterion guestBookCriterion) {
        return GuestBookBuilder.build(hibernateRepository.load(
                new RepositoryCriterion<>(GuestBookOrm.class).with(guestBookCriterion.id)
        ));
    }

    GuestBookEntryDomain findUnique(GuestBookEntryCriterion guestBookEntryCriterion) {
        return GuestBookEntryBuilder.build(hibernateRepository.load(
                new RepositoryCriterion<>(GuestBookEntryOrm.class).with(guestBookEntryCriterion.id)
        ));
    }

    public static class GuestBookCriterion {
        Long id;

        public GuestBookCriterion with(Long id) {
            this.id = id;
            return this;
        }
    }

    public static class GuestBookEntryCriterion {
        Long id;

        public GuestBookEntryCriterion with(Long id) {
            this.id = id;
            return this;
        }
    }
}
