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
import java.util.*

@Service class ImageService(
        private val resourceLoader: ResourceLoader,
        private val imageRepository: ImageRepository
) {

    @Bean fun setUp() = CommandLineRunner {
        FileSystemUtils.deleteRecursively(File(UPLOAD_ROOT))
        Files.createDirectory(Paths.get(UPLOAD_ROOT))
        FileCopyUtils.copy("Test file1", FileWriter("$UPLOAD_ROOT/cover.jpg"))
        FileCopyUtils.copy("Test file2", FileWriter("$UPLOAD_ROOT/cover2.jpg"))
        FileCopyUtils.copy("Test file3", FileWriter("$UPLOAD_ROOT/icon.jpg"))
    }

    fun findAllImages() = imageRepository.findAll()

    fun findOneImage(fileName: String): Mono<Resource> {
        return Mono.fromSupplier { resourceLoader.getResource("file:$UPLOAD_ROOT/$fileName") }
    }

    fun createImage(files: Flux<FilePart>): Mono<Void> {
        return files.flatMap { file ->
            val saveDatabaseImage = imageRepository.save(Image(UUID.randomUUID().toString(), file.filename()))
            val copyFile = Mono.just(Paths.get(UPLOAD_ROOT, file.filename()).toFile())
                    .log("createImage-picktarget")
                    .map { destFile ->
                        try {
                            destFile.createNewFile()
                            return@map destFile
                        } catch (e: IOException) {
                            throw RuntimeException(e)
                        }
                    }
                    .log("createImage-newfile")
                    .flatMap(file::transferTo)
                    .log("createImage-copy")

            return@flatMap Mono.`when`(saveDatabaseImage, copyFile)
        }.then()
    }

    fun deleteImage(fileName: String): Mono<Void> {
        val deleteDatabaseImage = imageRepository.findByName(fileName).flatMap(imageRepository::delete)
        val deleteFile = Mono.fromRunnable<Void> {
            try {
                Files.deleteIfExists(Paths.get(UPLOAD_ROOT, fileName))
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }
        return Mono.`when`(deleteDatabaseImage, deleteFile).then()
    }

    companion object {
        const val UPLOAD_ROOT = "upload-dir"
    }
}