package com.glindev.learningspringboot

import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.stereotype.Component

@Component
class InitDatabase {

    @Bean fun init(operations: MongoOperations): CommandLineRunner = CommandLineRunner {
        operations.dropCollection(Image::class.java)
        operations.insert(Image("1", "learning-spring-boot-cover.jpg"))
        operations.insert(Image("2", "learning-spring-boot-2nd-edition-cover.jpg"))
        operations.insert(Image("3", "bazinga.png"))
        operations.findAll(Image::class.java).forEach { image -> println(image.toString()) }
    }
}