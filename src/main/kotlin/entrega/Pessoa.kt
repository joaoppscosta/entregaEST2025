package com.example.entrega

import kotlinx.serialization.Serializable

@Serializable
data class Pessoa(val id: String, val nome: String) {
    companion object {
        private val pessoas = mutableMapOf<String, Pessoa>()

        fun registar(pessoa: Pessoa) {
            pessoas[pessoa.id] = pessoa
        }

        fun porId(id: String): Pessoa? = pessoas[id]

        fun todas(): List<Pessoa> = pessoas.values.toList()

        fun limpar() = pessoas.clear()
    }
}