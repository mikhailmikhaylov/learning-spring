package com.glindev.learningspringboot

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
class ApiController {

    @GetMapping("$API_BASE_PATH/images") fun images(): Flux<Image> = Flux.just(
            Image("1", "1.jpg"),
            Image("2", "2.jpg"),
            Image("3", "3.jpg")
    )

    @PostMapping("$API_BASE_PATH/images") fun create(@RequestBody images: Flux<Image>): Mono<Void> = images
            .doOnNext { log.info("Saving ${it.name} (not actually, lol)") }
            .then()


    companion object {
        val log = LoggerFactory.getLogger(ApiController::class.java)!!
        const val API_BASE_PATH = "/api"
    }
}