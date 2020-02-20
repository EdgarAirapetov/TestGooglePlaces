package com.edgarairapetov.testgoogleplaces.api.model.place

import com.google.gson.annotations.SerializedName

class Result {
    @SerializedName("geometry")
    val geometry: Geometry? = null
    var type: String = ""


}