package com.github.jactor.persistence.aop

import com.github.jactor.persistence.entity.PersistentEntity
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.stereotype.Component
import java.util.Arrays

@Aspect
@Component
class ModifierAspect {
    @Before("execution(* com.github.jactor.persistence.repository.*Repository.save(..))")
    fun modifyPersistentEntity(joinPoint: JoinPoint): Any? {
        Arrays.stream(joinPoint.args)
            .filter { obj: Any? -> obj is PersistentEntity<*> }
            .map { obj: Any -> obj as PersistentEntity<*> }
            .filter { persistentEntity: PersistentEntity<*> -> persistentEntity.id != null }
            .forEach { obj: PersistentEntity<*> -> obj.modify() }
        return null
    }
}