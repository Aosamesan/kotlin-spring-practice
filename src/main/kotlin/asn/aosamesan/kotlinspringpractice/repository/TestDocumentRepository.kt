package asn.aosamesan.kotlinspringpractice.repository

import asn.aosamesan.kotlinspringpractice.model.TestDocument
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface TestDocumentRepository : ReactiveMongoRepository<TestDocument, String>