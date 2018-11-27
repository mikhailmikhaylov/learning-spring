package com.glindev.learningspringboot

import lombok.Data
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Data
@Document
class Chapter(val name: String) {
    @Id private lateinit var id: String
}