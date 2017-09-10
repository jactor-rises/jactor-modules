package com.github.jactorrises.model.internal.service;

import com.github.jactorrises.client.datatype.UserName;
import com.github.jactorrises.model.internal.domain.PersistentDomain;
import com.github.jactorrises.model.internal.domain.guestbook.GuestBookDomain;
import com.github.jactorrises.model.internal.domain.guestbook.GuestBookDomainBuilder;
import com.github.jactorrises.model.internal.domain.user.UserDomain;
import com.github.jactorrises.model.internal.persistence.entity.guestbook.GuestBookEntity;
import com.github.jactorrises.model.internal.persistence.repository.HibernateRepository;
import com.github.jactorrises.model.internal.persistence.repository.RepositoryCriterion;
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
        saveOrUpdate(guestBookDomain);

        return guestBookDomain;
    }

    Optional<UserDomain> findUser(UserName userName) {
        return hibernateRepository.findUsing(userName).map(UserDomain::new);
    }

    GuestBookDomain findUnique(GuestBookCriterion guestBookCriterion) {
        return GuestBookDomainBuilder.build(hibernateRepository.load(
                 new RepositoryCriterion<>(GuestBookEntity.class).with(guestBookCriterion.id)
        ));
    }

    public static class GuestBookCriterion {
        Long id;

        public GuestBookCriterion with(Long id) {
            this.id = id;
            return this;
        }
    }
}
