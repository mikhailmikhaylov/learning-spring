package com.glindev.learningspringboot

import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Flux

@Configuration
class LoadDatabase {

    @Bean fun init(repository: ChapterRepository): CommandLineRunner = CommandLineRunner {
        Flux.just(
                Chapter("Quick start with Java"),
                Chapter("Reactive Web with Spring Boot"),
                Chapter("...and more!"))
                .flatMap { repository.save(it) }
                .subscribe { println(it) }

    }
}