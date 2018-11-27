package com.glindev.learningspringboot

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController class HomeController {

    @GetMapping fun greeting(
            @RequestParam(required = false, defaultValue = "") name: String
    ): String {
        return if (name == "") "Hey!" else "Hey, $name!"
    }
}