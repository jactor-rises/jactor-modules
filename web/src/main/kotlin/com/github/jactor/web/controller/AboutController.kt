package com.github.jactor.web.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class AboutController {
    @GetMapping(value = ["/about"])
    fun get(): ModelAndView {
        return ModelAndView("about")
    }
}
