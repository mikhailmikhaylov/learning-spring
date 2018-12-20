package com.glindev.learningspringboot

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ImageTests {
    @Test fun imageDataClassWorks() {
        val image = Image("id", "filename.jpg")
        assertThat(image.id).isEqualTo("id")
        assertThat(image.name).isEqualTo("filename.jpg")
    }
}