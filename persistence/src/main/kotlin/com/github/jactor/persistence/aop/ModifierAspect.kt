package com.github.jactor.persistence.aop

import com.github.jactor.persistence.common.PersistentEntity
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.stereotype.Component

@Aspect
@Component
class ModifierAspect {
    @Before("execution(* com.github.jactor.persistence.repository.*Repository.save(..))")
    fun modifyPersistentEntity(joinPoint: JoinPoint): Any? {
        return joinPoint.args
            .filterIsInstance<PersistentEntity<*>>()
            .filter { persistentEntity: PersistentEntity<*> -> persistentEntity.id != null }
            .forEach { persistentEntity -> persistentEntity.modifiedBy("todo") }
    }
}