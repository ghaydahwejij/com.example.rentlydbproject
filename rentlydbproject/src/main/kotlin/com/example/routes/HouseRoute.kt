package com.example.routes

import com.example.data.db.addFlat
import com.example.data.db.addHouse
import com.example.data.db.getHouses
import com.example.data.model.Flat
import com.example.data.model.House
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

fun Route.houseRoute(){
//    var houses = mutableListOf(
//        House("home1","1"),
//        House("home2","2"),
//        House("home3","3"),
//        House("home4","4"),
//    )

    get("/houses/{id?}"){
        call.respond(HttpStatusCode.OK, getHouses())
    }
    post("/add-house"){
        try {
            val multipart = call.receiveMultipart()
            var fileName: String? = null
            var title: String? = null
            var desc1: String?= null
            var desc2: String?= null
            var desc3: String?= null
            var desc4: String?= null
            var time: String?= null
            var price: String?= null
            var location: String?= null
            var imageUrl1: String? = null
            var imageUrl2: String? = null
            var imageUrl3: String? = null
            var feature1: String? = null
            var feature2: String? = null
            var feature3: String? = null


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
                                "time" -> time = partData.value
                                "price" -> price = partData.value
                                "location" -> location = partData.value
                                "feature1" -> feature1 = partData.value
                                "feature2" -> feature2 = partData.value
                                "feature3" -> feature3 = partData.value


                            }
                        }

                        is PartData.FileItem -> {
                            // to read the image data we call the 'save' utility function passing our path
                            if (partData.name == "image1") {
                                fileName = partData.save(Constants.HOUSE_IMAGE_PATH)
                                imageUrl1 = "${Constants.BASE_URL}${Constants.EXTERNAL_HOUSE_IMAGE_PATH}/$fileName"
                            }
                            if (partData.name == "image2") {
                                fileName = partData.save(Constants.HOUSE_IMAGE_PATH)
                                imageUrl2 = "${Constants.BASE_URL}${Constants.EXTERNAL_HOUSE_IMAGE_PATH}/$fileName"
                            }
                            if (partData.name == "image3") {
                                fileName = partData.save(Constants.HOUSE_IMAGE_PATH)
                                imageUrl3 = "${Constants.BASE_URL}${Constants.EXTERNAL_HOUSE_IMAGE_PATH}/$fileName"
                            }
                        }

                        else -> Unit
                    }
                }
            } catch (ex: Exception) {
                // something went wrong with the image part, delete the file
                File("${Constants.HOUSE_IMAGE_PATH}/$fileName").delete()
                ex.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, "Error")
            }
            // create a new fruit object using data we collected above
            val newHouse = House(
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
                feature1 = feature1,
                feature2 = feature2,
                feature3 = feature3,
                location=location

            )
            // add the received fruit to the database
            if (!addHouse(newHouse )) {
                // if not added successfully return with an error
                return@post call.respond(
                    HttpStatusCode.Conflict,
                    SimpleResponse(successful = false, message = "Item already exits")
                )
            }
            call.respond(HttpStatusCode.Created, newHouse)
        } catch (ex: Exception) {
            ex.printStackTrace()
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Invalid data"))
        }}

    post("/add-house"){
        try {
            val newHouse = call.receive<House>()
            if(!addHouse(newHouse)){
                return@post call.respond(HttpStatusCode.Conflict,SimpleResponse(successful = false, message = "Item already exits"))
            }
            call.respond(
                HttpStatusCode.Created, SimpleResponse(
                    true,
                    "Successfully added ${newHouse.title}"
                )
            )
        }catch (ex: Exception){
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false,"Invalid House format"))
        }
    }
}