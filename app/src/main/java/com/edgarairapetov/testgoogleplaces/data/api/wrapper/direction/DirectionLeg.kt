package com.edgarairapetov.testgoogleplaces.data.api.wrapper.direction

import com.google.gson.annotations.SerializedName

class DirectionLeg {
    @SerializedName("steps")
    var steps: List<DirectionStep>? = null
}