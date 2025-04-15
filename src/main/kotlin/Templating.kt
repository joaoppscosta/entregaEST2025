package com.example

import com.example.entrega.Pessoa
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.thymeleaf.Thymeleaf
import io.ktor.server.thymeleaf.ThymeleafContent
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver

fun Application.configureTemplating() {
    install(Thymeleaf) {
        setTemplateResolver(ClassLoaderTemplateResolver().apply {
            prefix = "templates/thymeleaf/"
            suffix = ".html"
            characterEncoding = "utf-8"
        })
    }
    routing {
        get("/html-thymeleaf") {
            // call.respond(ThymeleafContent("index", mapOf("user" to ThymeleafUser(1, "user1"))))
        }
        get ("/entrega.html") {
            call.respond(ThymeleafContent("entrega", mapOf("entrega" to entregas[0])))
        }
        get ("/entregas.html") {
            call.respond(ThymeleafContent("entregas", mapOf("entregas" to entregas)))
        }
        get("/registar-entrega.html") {
            call.respond(ThymeleafContent("registar-entrega", mapOf("pessoas" to Pessoa.todas())))
        }
    }
}
// data class ThymeleafUser(val id: Int, val name: String)
