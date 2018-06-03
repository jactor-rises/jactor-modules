package com.github.jactor.rises.persistence.aop;

import com.github.jactor.rises.persistence.entity.PersistentEntity;
import com.github.jactor.rises.persistence.entity.address.AddressEntity;
import com.github.jactor.rises.persistence.entity.blog.BlogEntity;
import com.github.jactor.rises.persistence.entity.blog.BlogEntryEntity;
import com.github.jactor.rises.persistence.entity.person.PersonEntity;
import com.github.jactor.rises.persistence.entity.user.UserEntity;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class IdentitySequencer {
    private static final Long START_SEQUENCE = 999999L; // + 1 (computed before value retrieved)
    private final Map<Class<?>, Long> idSequenceMap = new HashMap<>();

    @SuppressWarnings("unchecked") @Before("execution(* com.github.jactor.rises.persistence.repository.*Repository.save(..))")
    public Object addIdentity(JoinPoint joinPoint) {
        return Arrays.stream(joinPoint.getArgs())
                .filter(obj -> obj instanceof PersistentEntity)
                .map(obj -> (PersistentEntity<Long>) obj)
                .map(this::addSequencedId)
                .findAny().orElse(null);
    }

    private PersistentEntity<Long> addSequencedId(PersistentEntity<Long> persistentEntity) {
        addSequence(persistentEntity);

        if (persistentEntity instanceof PersonEntity) {
            addSequenceToDependencies(((PersonEntity) persistentEntity));
        } else if (persistentEntity instanceof UserEntity) {
            addSequenceToDependencies((UserEntity) persistentEntity);
        } else if (persistentEntity instanceof BlogEntity) {
            addSequenceToDependencies((BlogEntity) persistentEntity);
        } else if (persistentEntity instanceof BlogEntryEntity) {
            addSequenceToDependencies((BlogEntryEntity) persistentEntity);
        }

        return persistentEntity;
    }

    private void addSequenceToDependencies(PersonEntity personEntity) {
        addSequence(personEntity);
        AddressEntity addressEntity = personEntity.getAddressEntity();
        UserEntity userEntity = personEntity.getUserEntity();

        if (addressEntity.getId() == null) {
            addressEntity.setId(fetchNextValFor(addressEntity.getClass()));
        }

        if (userEntity != null && userEntity.getId() == null) {
            userEntity.setId(fetchNextValFor(userEntity.getClass()));
        }
    }

    private void addSequenceToDependencies(UserEntity userEntity) {
        addSequence(userEntity);
        PersonEntity personEntity = userEntity.getPerson();

        if (personEntity.getId() == null) {
            personEntity.setId(fetchNextValFor(personEntity.getClass()));
        }

        addSequenceToDependencies(personEntity);
    }

    private void addSequenceToDependencies(BlogEntity blogEntity) {
        addSequence(blogEntity);
        addSequenceToDependencies(blogEntity.getUser());
        blogEntity.getEntries().forEach(this::addSequenceToDependencies);
    }

    private void addSequenceToDependencies(BlogEntryEntity blogEntryEntity) {
        addSequence(blogEntryEntity);

        if (blogEntryEntity.getBlog().getId() == null) {
            addSequenceToDependencies(blogEntryEntity.getBlog());
        }
    }

    private void addSequence(PersistentEntity<Long> persistentEntity) {
        if (persistentEntity.getId() == null) {
            persistentEntity.setId(fetchNextValFor(persistentEntity.getClass()));
        }
    }

    private Long fetchNextValFor(Class<?> aClass) {
        idSequenceMap.putIfAbsent(aClass, START_SEQUENCE);
        idSequenceMap.computeIfPresent(aClass, (k, v) -> ++v);

        return idSequenceMap.get(aClass);
    }
}
