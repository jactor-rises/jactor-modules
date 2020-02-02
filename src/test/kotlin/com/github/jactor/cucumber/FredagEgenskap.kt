package com.github.jactor.cucumber

import io.cucumber.java.no.Gitt
import io.cucumber.java.no.Når
import io.cucumber.java.no.Så
import org.assertj.core.api.Assertions.assertThat

private data class Ukedag(val dagen: String) {
    internal lateinit var svaret: String

    fun erDetFredag() {
        svaret = when (dagen) {
            "fredag" -> "jada, helg"
            "lørdag" -> "nei, midt i helga"
            "onsdag" -> "snart, lille lørdag"
            else -> "neida"
        }
    }
}

class FredagEgenskap {
    private lateinit var ukedag: Ukedag

    @Gitt("dagen er søndag")
    fun `dagen er sondag`() {
        ukedag = Ukedag("søndag")
    }

    @Gitt("dagen er fredag")
    fun `dagen er fredag`() {
        ukedag = Ukedag("fredag")
    }

    @Gitt("dagen er {string}")
    fun `dagen er`(dagen: String) {
        this.ukedag = Ukedag(dagen)
    }

    @Når("jeg spør om det er fredag")
    fun `jeg spor om det er fredag`() {
        ukedag.erDetFredag()
    }

    @Så("er svaret {string}")
    fun `er svaret`(svaret: String) {
        assertThat(ukedag.svaret).isEqualTo(svaret)
    }
}
