package asn.aosamesan.kotlinspringpractice.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
class TestDocument(@Id var id: String?, var title: String, var content: String, @CreatedDate var createdAt: Date, @LastModifiedDate var updatedAt: Date?) {
    constructor(title: String, content: String, createdAt: Date): this(null, title, content, createdAt, null)
}