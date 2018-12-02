package com.glindev.learningspringboot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.filter.reactive.HiddenHttpMethodFilter

@SpringBootApplication
class LearningSpringBootApplication {
    @Bean fun hiddenHttpMethodFilter() = HiddenHttpMethodFilter()
}

fun main(args: Array<String>) {
    runApplication<LearningSpringBootApplication>(*args)
}
