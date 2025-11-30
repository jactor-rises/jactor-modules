package com.github.jactor.rises.persistence

import com.fasterxml.uuid.Generators
import java.util.UUID

object UUIDv7 {
    private val generator = Generators.timeBasedEpochGenerator()
    fun generate(): UUID = generator.generate()
}
