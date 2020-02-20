package com.edgarairapetov.testgoogleplaces.api.model.route

import com.google.gson.annotations.SerializedName

class Leg {
    @SerializedName("steps")
    var steps: List<Step>? = null
}