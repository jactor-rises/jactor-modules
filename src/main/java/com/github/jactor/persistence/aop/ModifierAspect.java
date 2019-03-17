package com.github.jactor.persistence.aop;

import com.github.jactor.persistence.entity.PersistentEntity;
import java.util.Arrays;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ModifierAspect {

  @Before("execution(* com.github.jactor.persistence.repository.*Repository.save(..))")
  public Object modifyPersistentEntity(JoinPoint joinPoint) {
    Arrays.stream(joinPoint.getArgs())
        .filter(obj -> obj instanceof PersistentEntity)
        .map(obj -> (PersistentEntity) obj)
        .filter(persistentEntity -> persistentEntity.getId() != null)
        .forEach(PersistentEntity::modify);

    return null;
  }
}
