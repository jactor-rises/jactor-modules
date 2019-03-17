package com.github.jactor.persistence.aop;

import com.github.jactor.persistence.entity.PersistentEntity;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class IdentitySequencer {
    private static final Long START_SEQUENCE = 999999L; // + 1 (computed before value retrieved)
    private final Map<Class<?>, Long> idSequenceMap = new HashMap<>();

    @Before("execution(* com.github.jactor.persistence.repository.*Repository.save(..))")
    public Object addIdentity(JoinPoint joinPoint) {
        return Arrays.stream(joinPoint.getArgs())
                .filter(obj -> obj instanceof PersistentEntity)
                .map(obj -> (PersistentEntity) obj)
                .map(persistentEntity -> persistentEntity.addSequencedId(this::fetchNextValFor))
                .findAny().orElse(null);
    }

    private Long fetchNextValFor(Class<?> aClass) {
        idSequenceMap.putIfAbsent(aClass, START_SEQUENCE);
        idSequenceMap.computeIfPresent(aClass, (k, v) -> ++v);

        return idSequenceMap.get(aClass);
    }
}
