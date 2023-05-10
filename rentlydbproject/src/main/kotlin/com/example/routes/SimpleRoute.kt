package com.example.routes

import com.example.data.model.SimpleResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.getSimpleResponse(){
    route("/get-one"){
        get {
            val simpleResponse = SimpleResponse(true,"Hey im one")
            call.respond(
                HttpStatusCode.OK,simpleResponse
            )
        }
    }

}