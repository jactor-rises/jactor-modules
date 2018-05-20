package com.github.jactor.rises.persistence.aop;

import com.github.jactor.rises.persistence.entity.PersistentEntity;
import com.github.jactor.rises.persistence.entity.address.AddressEntity;
import com.github.jactor.rises.persistence.entity.person.PersonEntity;
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
        if (persistentEntity.getId() == null) {
            persistentEntity.setId(fetchNextValFor(persistentEntity.getClass()));
        }

        if (persistentEntity instanceof PersonEntity) {
            AddressEntity addressEntity = ((PersonEntity) persistentEntity).getAddressEntity();

            if (addressEntity.getId() == null) {
                addressEntity.setId(fetchNextValFor(addressEntity.getClass()));
            }
        }

        return persistentEntity;
    }

    private Long fetchNextValFor(Class<?> aClass) {
        idSequenceMap.putIfAbsent(aClass, START_SEQUENCE);
        idSequenceMap.computeIfPresent(aClass, (k, v) -> ++v);

        return idSequenceMap.get(aClass);
    }
}
