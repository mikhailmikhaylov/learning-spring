package com.glindev.learningspringboot

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Image(
        @Id val id: String,
        val name: String
)