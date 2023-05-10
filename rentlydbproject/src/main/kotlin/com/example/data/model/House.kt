package com.example.data.model


import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class House(
 @BsonId
 var id : String = ObjectId().toString(),
 var title : String,
 val image1: String?,
 val image2: String?,
 val image3: String?,
 val desc1: String?,
 val desc2: String?,
 val desc3: String?,
 val desc4: String?,
 val price: String?,
 val feature1: String?,
 val feature2: String?,
 val feature3: String?,
 val location: String?,

)