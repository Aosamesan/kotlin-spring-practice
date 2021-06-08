package asn.aosamesan.kotlinspringpractice.handler

import asn.aosamesan.kotlinspringpractice.model.TestDocument
import asn.aosamesan.kotlinspringpractice.repository.TestDocumentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.util.*

@Component
class TestDocumentHandler @Autowired constructor(val repository: TestDocumentRepository) {
    fun create(request: ServerRequest): Mono<ServerResponse> =
        request.formData()
            .filter {
                return@filter it.containsKey("title") && it.containsKey("content")
            }
            .map {
                val title = it["title"]!!.first()
                val content = it["content"]!!.first()
                val now = Date()
                return@map TestDocument(title, content, now)
            }
            .flatMap(repository::save)
            .map(BodyInserters::fromValue)
            .flatMap(ServerResponse.ok()::body)
            .switchIfEmpty(ServerResponse.badRequest().build())


    fun retrieveAll(request: ServerRequest): Mono<ServerResponse> =
        repository.findAll(Sort.by("createdAt").descending())
            .skip(request.queryParam("start").map(String::toLong).orElse(0))
            .take(request.queryParam("display").map(String::toLong).orElse(10))
            .collectList()
            .map(BodyInserters::fromValue)
            .flatMap(ServerResponse.ok()::body)

    fun retrieve(request: ServerRequest): Mono<ServerResponse> =
        repository.findById(request.pathVariable("id"))
            .map(BodyInserters::fromValue)
            .flatMap(ServerResponse.ok()::body)
            .switchIfEmpty(ServerResponse.notFound().build())

    fun update(request: ServerRequest): Mono<ServerResponse> =
        repository.findById(request.pathVariable("id"))
            .zipWith(request.formData())
            .filter {
                return@filter it.t2.containsKey("title") && it.t2.containsKey("content")
            }
            .map {
                val stored = it.t1
                val formData = it.t2
                var updated = false
                val now = Date()

                if (formData["title"]!!.first() != stored.title) {
                    stored.title = formData["title"]!!.first()
                    updated = true
                }

                if (formData["content"]!!.first() != stored.content) {
                    stored.content = formData["content"]!!.first()
                    updated = true
                }

                if (updated) {
                    stored.updatedAt = now
                }
                return@map stored
            }
            .flatMap(repository::save)
            .map(BodyInserters::fromValue)
            .flatMap(ServerResponse.ok()::body)
            .switchIfEmpty(ServerResponse.badRequest().build())

    fun delete(request: ServerRequest): Mono<ServerResponse> =
        repository.deleteById(request.pathVariable("id"))
            .then(ServerResponse.noContent().build())
}