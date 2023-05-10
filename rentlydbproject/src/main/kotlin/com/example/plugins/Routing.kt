package com.example.plugins

import com.example.routes.*
import com.example.utils.Constants
import io.ktor.http.ContentDisposition.Companion.File
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import java.io.File

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        getSimpleResponse()
        houseRoute()
        flatRoute()
        beachRoute()
        hallRoute()

        static("/") {
            // sets the base route for static routes in this block, in other words all static blocks here will start at "static/fruit_pictures/" by default instead of project root
            staticRootFolder = File(Constants.STATIC_ROOT)

            // the path the client will use to access files: /images
            static(Constants.EXTERNAL_FLAT_IMAGE_PATH){

                // serve all files in fruit_pictures as static content under /images
                files(Constants.FLAT_IMAGE_DIRECTORY)
            }
        }

    }

}
