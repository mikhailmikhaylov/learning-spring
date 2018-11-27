package com.glindev.learningspringboot

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
class ChapterController(private val repository: ChapterRepository) {

    @GetMapping("/chapters")
    fun listing(): Flux<Chapter> = repository.findAll()
}