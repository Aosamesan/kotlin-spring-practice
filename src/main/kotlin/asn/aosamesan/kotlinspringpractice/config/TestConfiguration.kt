package asn.aosamesan.kotlinspringpractice.config

import asn.aosamesan.kotlinspringpractice.handler.TestDocumentHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunctions

@Configuration
@EnableWebFlux
class TestConfiguration(val testDocumentHandler: TestDocumentHandler) {
    @Bean
    fun routes() =
        RouterFunctions.nest(
            RequestPredicates.path("/"),
            RouterFunctions.route(
                RequestPredicates.GET(""),
                testDocumentHandler::retrieveAll
            ).andRoute(
                RequestPredicates.POST(""),
                testDocumentHandler::create
            ).andRoute(
                RequestPredicates.GET("/{id}"),
                testDocumentHandler::retrieve
            ).andRoute(
                RequestPredicates.PUT("/{id}"),
                testDocumentHandler::update
            ).andRoute(
                RequestPredicates.DELETE("/{id}"),
                testDocumentHandler::delete
            )
        )
}