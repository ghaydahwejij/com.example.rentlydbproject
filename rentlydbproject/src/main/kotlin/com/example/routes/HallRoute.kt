package com.example.routes

import com.example.data.db.addFlat
import com.example.data.db.addHall
import com.example.data.db.getHalls
import com.example.data.db.getHouses
import com.example.data.model.Flat
import com.example.data.model.Hall
import com.example.data.model.SimpleResponse
import com.example.utils.Constants
import com.example.utils.save
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File


fun Route.hallRoute() {
    get("/hall/{id?}") {
        call.respond(HttpStatusCode.OK, getHalls())
    }


    post("/add-hall"){
        try {
            val multipart = call.receiveMultipart()
            var fileName: String? = null
            var title: String? = null
            var desc1: String?= null
            var desc2: String?= null
            var desc3: String?= null
            var desc4: String?= null
            var price: String?= null
            var imageUrl1: String? = null
            var imageUrl2: String? = null
            var imageUrl3: String? = null
            var location: String? = null

            try {
                // loop through each part of our multipart
                multipart.forEachPart { partData ->
                    when (partData) {
                        is PartData.FormItem -> {
                            // to read parameters that we sent with the image
                            when (partData.name) {
                                "title" -> title = partData.value
                                "desc1" -> desc1 = partData.value
                                "desc2" -> desc2 = partData.value
                                "desc3" -> desc3= partData.value
                                "desc4" -> desc4 = partData.value
                                "price" -> price = partData.value
                                "location" -> location = partData.value
                            }
                        }

                        is PartData.FileItem -> {
                            // to read the image data we call the 'save' utility function passing our path
                            if (partData.name == "image1") {
                                fileName = partData.save(Constants.FLAT_IMAGE_PATH)
                                imageUrl1 = "${Constants.BASE_URL}${Constants.EXTERNAL_FLAT_IMAGE_PATH}/$fileName"
                            }
                            if (partData.name == "image2") {
                                fileName = partData.save(Constants.FLAT_IMAGE_PATH)
                                imageUrl2 = "${Constants.BASE_URL}${Constants.EXTERNAL_FLAT_IMAGE_PATH}/$fileName"
                            }
                            if (partData.name == "image3") {
                                fileName = partData.save(Constants.FLAT_IMAGE_PATH)
                                imageUrl3 = "${Constants.BASE_URL}${Constants.EXTERNAL_FLAT_IMAGE_PATH}/$fileName"
                            }
                        }

                        else -> Unit
                    }
                }
            } catch (ex: Exception) {
                // something went wrong with the image part, delete the file
                File("${Constants.FLAT_IMAGE_PATH}/$fileName").delete()
                ex.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, "Error")
            }
            // create a new fruit object using data we collected above
            val newHall = Hall(
                title = title!!,
                // the valueOf function will find the enum class type that matches this string and return it, an Exception is thrown if the string does not match any type
                desc1=desc1!!,
                desc2=desc2!!,
                desc3=desc3!!,
                desc4=desc4!!,
                image1 = imageUrl1,
                image2 = imageUrl2,
                image3 = imageUrl3,
                price = price!!,
                location = location!!,
            )
            // add the received fruit to the database
            if (!addHall(newHall)) {
                // if not added successfully return with an error
                return@post call.respond(
                    HttpStatusCode.Conflict,
                    SimpleResponse(successful = false, message = "Item already exits")
                )
            }
            call.respond(HttpStatusCode.Created, newHall)
        } catch (ex: Exception) {
            ex.printStackTrace()
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Invalid data"))
        }}

    patch("/add-hall") {
        try {
            val newHall = call.receive<Flat>()
            if (!addFlat(newHall)) {
                return@patch call.respond(
                    HttpStatusCode.Conflict,
                    SimpleResponse(successful = false, message = "Item already exits")
                )
            }
            call.respond(
                HttpStatusCode.Created, SimpleResponse(
                    successful = true,
                    message = "Successfully added ${newHall.title}"
                )
            )
        } catch (ex: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Invalid hall format"))
        }

    }
}