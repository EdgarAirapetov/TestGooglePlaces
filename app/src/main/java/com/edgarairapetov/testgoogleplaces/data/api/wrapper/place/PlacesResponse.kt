package com.edgarairapetov.testgoogleplaces.data.api.wrapper.place

import com.google.gson.annotations.SerializedName

class PlacesResponse {
    @SerializedName("results")
    val results: ArrayList<PlaceResult>? = null

    var type: PlaceResultTypesEnum? = null
}