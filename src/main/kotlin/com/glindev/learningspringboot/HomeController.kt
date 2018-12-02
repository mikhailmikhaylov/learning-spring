package com.glindev.learningspringboot

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Controller
class HomeController(private val imageService: ImageService) {

    @GetMapping("/")
    fun index(model: Model): String {
        model.addAttribute("images", imageService.findAllImages())
        return "index"
    }

    @GetMapping(value = ["$BASE_PATH/$FILENAME/raw"], produces = [MediaType.IMAGE_JPEG_VALUE])
    @ResponseBody
    fun oneRawImage(@PathVariable filename: String): Mono<ResponseEntity<out Any>> {
        return imageService.findOneImage(filename)
                .map {
                    try {
                        ResponseEntity.ok()
                                .contentLength(it.contentLength())
                                .body(it.inputStream)
                    } catch (e: Throwable) {
                        ResponseEntity.badRequest()
                                .body("Couldn't find $filename => ${e.localizedMessage}")
                    }
                }
    }

    @PostMapping(value = [BASE_PATH])
    fun createFile(@RequestPart(name = "file") files: Flux<FilePart>): Mono<String> {
        return imageService.createImage(files).then(Mono.just("redirect:/"))
    }

    @DeleteMapping("$BASE_PATH/$FILENAME")
    fun deleteFile(@PathVariable filename: String) : Mono<String> {
        return imageService.deleteImage(filename)
                .then(Mono.just("redirect:/"))
    }

    companion object {
        const val BASE_PATH = "/images"
        const val FILENAME = "{filename:.+}"
    }
}