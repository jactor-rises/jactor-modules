package com.github.jactor.cucumber.persistence

data class Application(val contextUrl: String, var completeUrl: String?) {
    constructor(contextUrl: String) : this(contextUrl, null)
}
