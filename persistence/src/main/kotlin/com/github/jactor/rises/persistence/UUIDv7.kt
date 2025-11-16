package com.github.jactor.rises.persistence

import java.util.UUID
import com.fasterxml.uuid.Generators

object UUIDv7 {

    private val generator = Generators.timeBasedEpochGenerator()
    fun generate(): UUID = generator.generate()
}
