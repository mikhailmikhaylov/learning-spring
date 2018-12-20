package com.glindev.learningspringboot

import lombok.Data
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Data
@Document
class Image(
        @Id val id: String,
        val name: String
)