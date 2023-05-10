package com.example.data.db

import com.example.data.model.Beach
import com.example.data.model.Flat
import com.example.data.model.Hall
import com.example.data.model.House
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo


val db = KMongo.createClient().coroutine.getDatabase("rently")

val houses = db.getCollection<House>()

suspend fun getHouses(): List<House> {
    return houses.find().toList()
}

suspend fun addHouse(newHouse: House): Boolean {
    return try {
        houses.insertOne(newHouse).wasAcknowledged()
    }catch (ex: Exception){
        ex.printStackTrace()
        false
    }
}

//--------------------------------------------------------

val flats = db.getCollection<Flat>()

suspend fun getFlats(): List<Flat>{
    return flats.find().toList()
}

suspend fun addFlat(newFlat: Flat):Boolean{
    return try {
        flats.insertOne(newFlat).wasAcknowledged()
    }catch (ex:Exception){
        ex.printStackTrace()
        false
    }
}

//--------------------------------------------------------

val beaches = db.getCollection<Beach>()

suspend fun getBeaches(): List<Beach>{
    return beaches.find().toList()
}

suspend fun addBeach(newbeach: Beach):Boolean{
    return try {
        beaches.insertOne(newbeach).wasAcknowledged()
    }catch (ex:Exception){
        ex.printStackTrace()
        false
    }
}
val halls = db.getCollection<Hall>()

suspend fun getHalls(): List<Hall>{
    return halls.find().toList()
}
suspend fun addHall(newHall:Hall):Boolean{
    return try {
        halls.insertOne(newHall).wasAcknowledged()
    }catch (ex:Exception){
        ex.printStackTrace()
        false
    }
}
