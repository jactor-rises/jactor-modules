package com.github.jactor.rises.persistence.aop

import com.github.jactor.rises.persistence.PersistentDao
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.stereotype.Component

@Aspect
@Component
class ModifierAspect {
    @Before("execution(* com.github.jactor.rises.persistence.*Repository.save(..))")
    fun modifyPersistentDao(joinPoint: JoinPoint): Any? {
        return joinPoint.args
            .filterIsInstance<PersistentDao<*>>()
            .filter { it.isPersisted }
            .forEach { it.modifiedBy(modifier = "todo") }
    }
}
