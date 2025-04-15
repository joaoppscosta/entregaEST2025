package com.example

import com.example.entrega.*
import io.ktor.http.*
import io.ktor.server.application.Application
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import java.io.File

val Application.CAMINHOBASE : String
    get () = "./src/main/resources/data"

var entregas = mutableListOf<Entrega>()

fun Application.startServer() {
    val pessoas = Json.decodeFromString<List<Pessoa>>(File("${CAMINHOBASE}/pessoas.json").readText())
    pessoas.forEach { Pessoa.registar(it) }

    val entregasSerializadas = Json.decodeFromString<List<EntregaSerializada>>(File("${CAMINHOBASE}/entregas.json").readText())
    val entregasTemp = entregasSerializadas.map { it.reconstruir()!! }
    entregas = entregasTemp as MutableList<Entrega>
}

fun Application.configureAPI() {
//    val tiago = Pessoa("p1", "Tiago")
//    val davi = Pessoa("p2", "Davi")
//    val izabella = Pessoa("p3", "Izabella")
//    listOf(tiago, davi, izabella).forEach { Pessoa.registar(it) }

    routing {
        get("/pessoas") {
            call.respond(Pessoa.todas())
            val params: Parameters = call.request.queryParameters
            // println(params.entries().toList())
            if (params["entregue"] == null) {
                call.respond(entregas.map { EntregaSerializada.construir(it) })
            } else {
                val entregue = params["entregue"].toBoolean()
                call.respond(entregas.filter{ it.entregue == entregue }.map { EntregaSerializada.construir(it) })
            }
        }
        get("/entregas") {
            call.respond(entregas.map { EntregaSerializada.construir(it) })
        }
        post("/registarEntrega") {
            val formData = call.receiveParameters()
            if ((formData["remetente"] != null) and
                (formData["destinatario"] != null)) {
                val remetente = Pessoa.porId(formData["remetente"]!!)
                val destinatario = Pessoa.porId(formData["destinatario"]!!)
                entregas.add(Entrega(remetente!!, destinatario!!, false))

                val jsonEntregas = Json.encodeToString(entregas.map { EntregaSerializada.construir(it) } )
                File("$CAMINHOBASE/entregas.json").writeText(jsonEntregas)

                call.respondRedirect ("entregas.html")
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
            // call.respondText(formData.entries().toList().toString())
            // call.respond(HttpStatusCode.Accepted)
        }
//        get("/entregas-n-feitas") {
//            call.respond(entregas.filter{ !!it.entregue }.map { EntregaSerializada.construir(it) })
        }
}


