package com.edgarairapetov.testgoogleplaces.data.api.wrapper.place

import com.google.gson.annotations.SerializedName

class PlaceResult {
    @SerializedName("geometry")
    val geometry: PlaceGeometry? = null
    var type: PlaceResultTypesEnum? = null


}