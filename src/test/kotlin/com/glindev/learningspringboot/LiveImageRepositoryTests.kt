package com.glindev.learningspringboot

import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.test.context.junit4.SpringRunner
import reactor.test.StepVerifier

@RunWith(SpringRunner::class)
@DataMongoTest(excludeAutoConfiguration = [EmbeddedMongoAutoConfiguration::class])
class LiveImageRepositoryTests {
    @Autowired lateinit var repository: ImageRepository
    @Autowired lateinit var operations: MongoOperations

    @Before fun setUp() {
        operations.dropCollection(Image::class.java)
        operations.insert(Image("1", "1.jpg"))
        operations.insert(Image("2", "2.jpg"))
        operations.insert(Image("3", "3.jpg"))
        operations.findAll(Image::class.java).forEach { println(it.toString()) }
    }

    @Test fun findAllShouldWork() {
        val images = repository.findAll()
        StepVerifier.create(images)
                .recordWith { ArrayList() }
                .expectNextCount(3)
                .consumeRecordedWith {
                    Assertions.assertThat(it).hasSize(3)
                    Assertions.assertThat(it).extracting<String>(Image::name).contains("1.jpg", "2.jpg", "3.jpg")
                }.expectComplete().verify()
    }

    @Test fun findByNameShouldWork() {
        val image = repository.findByName("3.jpg")
        StepVerifier.create(image)
                .expectNextMatches {
                    Assertions.assertThat(it.name).isEqualTo("3.jpg")
                    Assertions.assertThat(it.id).isEqualTo("3")
                    return@expectNextMatches true
                }.expectComplete().verify()
    }
}