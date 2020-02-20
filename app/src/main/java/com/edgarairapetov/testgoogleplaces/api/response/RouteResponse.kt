package com.edgarairapetov.testgoogleplaces.api.response

import com.edgarairapetov.testgoogleplaces.api.model.route.Route
import com.google.gson.annotations.SerializedName

class RouteResponse {
    @SerializedName("routes")
    var routes: List<Route>? = null
}