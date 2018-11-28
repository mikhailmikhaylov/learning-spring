package com.glindev.learningspringboot

import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service
import org.springframework.util.FileCopyUtils
import org.springframework.util.FileSystemUtils
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.lang.RuntimeException
import java.nio.file.Files
import java.nio.file.Paths

@Service class ImageService(private val resourceLoader: ResourceLoader) {

    @Bean fun setUp() = CommandLineRunner {
        FileSystemUtils.deleteRecursively(File(UPLOAD_ROOT))
        Files.createDirectory(Paths.get(UPLOAD_ROOT))
        FileCopyUtils.copy("Test file1", FileWriter("$UPLOAD_ROOT/cover.jpg"))
        FileCopyUtils.copy("Test file2", FileWriter("$UPLOAD_ROOT/cover2.jpg"))
        FileCopyUtils.copy("Test file3", FileWriter("$UPLOAD_ROOT/icon.jpg"))
    }

    fun findAllImages(): Flux<Image> {
        // TODO:mmykhailov: This is definitely the bad way to do it. Rewrite the reactive way later.
        return try {
            Flux.fromIterable(Files.newDirectoryStream(Paths.get(UPLOAD_ROOT)))
                    .map { Image(it.hashCode().toString(), it.fileName.toString()) }
        } catch (e: IOException) {
            Flux.empty()
        }
    }

    fun findOneImage(fileName: String): Mono<Resource> {
        return Mono.fromSupplier { resourceLoader.getResource("file:$UPLOAD_ROOT/$fileName") }
    }

    fun createImage(files: Flux<FilePart>): Mono<Void> {
        return files.flatMap { it.transferTo(Paths.get(UPLOAD_ROOT, it.filename()).toFile()) }.then()
    }

    fun deleteImage(fileName: String): Mono<Void> {
        return Mono.fromRunnable {
            try {
                Files.deleteIfExists(Paths.get(UPLOAD_ROOT, fileName))
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }
    }

    companion object {
        const val UPLOAD_ROOT = "upload-dir"
    }
}