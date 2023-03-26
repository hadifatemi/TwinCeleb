package com.hifacy.nelikeme.webservice

import com.google.gson.JsonObject
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class FaceSimilarModel (

    @SerializedName("statusCode")
    @Expose
    var statusCode: Int? = 0,
    @SerializedName("statusMessage")
    @Expose
    var statusMessage : String?  = null,
    @SerializedName("hasError")
    @Expose
    var hasError: Boolean? = null,
    @SerializedName("data")
    @Expose
    var data : /*JsonObject? = null */ Data? = Data()

)

