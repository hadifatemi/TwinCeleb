package com.hifacy.nelikeme.webservice

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface RetroService {

    @Headers("Accept: application/json")
    @Multipart
    @POST("/FaceSimilar")
    fun getSimilarFace(
        @PartMap map: HashMap<String, RequestBody>
    ): Call<FaceSimilarModel?>?

}