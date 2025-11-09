package com.github.jactor.rises.web.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.ModelAndView

@RestController
class AboutController {
    @GetMapping(value = ["/about"])
    fun get(): ModelAndView {
        return ModelAndView("about")
    }
}
