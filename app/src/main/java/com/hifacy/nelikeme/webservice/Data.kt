package com.hifacy.nelikeme.webservice

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Data (

    @SerializedName("name")
    @Expose
    var name : String? = null,
    @SerializedName("group")
    @Expose
    var group : String? = null,
    @SerializedName("imageBase64")
    @Expose
    var imageBase64: String? = null,
    @SerializedName("imageMimeType")
    @Expose
    var imageMimeType: String? = null,
    @SerializedName("similarPercentage")
    @Expose
    var similarPercentage : Int?= 0,
    @SerializedName("similarDistance")
    @Expose
    var similarDistance: Double? = null

)