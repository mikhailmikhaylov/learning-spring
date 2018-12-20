package com.glindev.learningspringboot

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface ImageRepository : ReactiveCrudRepository<Image, String> {
    fun findByName(name: String): Mono<Image>
}