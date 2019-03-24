package com.github.jactor.persistence.aop;

import com.github.jactor.persistence.entity.AddressEntity;
import com.github.jactor.persistence.entity.BlogEntryEntity;
import com.github.jactor.persistence.entity.GuestBookEntryEntity;
import com.github.jactor.persistence.entity.PersistentEntity;
import com.github.jactor.persistence.entity.PersonEntity;
import com.github.jactor.persistence.entity.UserEntity;
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

  private static final Long START_SEQUENCE = 1000000L;
  private final Map<Class<?>, Long> idSequenceMap = new HashMap<>();

  @Before("execution(* com.github.jactor.persistence.repository.*Repository.save(..))")
  public Object addIdentity(JoinPoint joinPoint) {
    Arrays.stream(joinPoint.getArgs())
        .filter(obj -> obj instanceof PersistentEntity)
        .filter(obj -> !(obj instanceof AddressEntity))
        .filter(obj -> !(obj instanceof GuestBookEntryEntity))
        .filter(obj -> !(obj instanceof BlogEntryEntity))
        .filter(obj -> !(obj instanceof PersonEntity))
        .filter(obj -> !(obj instanceof UserEntity))
        .map(obj -> (PersistentEntity) obj)
        .forEach(persistentEntity -> persistentEntity.addSequencedId(this::fetchNextValFor));

    return null;
  }

  private Long fetchNextValFor(Class<?> aClass) {
    idSequenceMap.computeIfPresent(aClass, (k, v) -> ++v);
    idSequenceMap.putIfAbsent(aClass, START_SEQUENCE);

    return idSequenceMap.get(aClass);
  }
}
