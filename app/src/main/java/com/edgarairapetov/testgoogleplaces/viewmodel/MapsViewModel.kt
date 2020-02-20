package com.edgarairapetov.testgoogleplaces.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edgarairapetov.testgoogleplaces.R
import com.edgarairapetov.testgoogleplaces.api.ApiService
import com.edgarairapetov.testgoogleplaces.api.model.place.Result
import com.edgarairapetov.testgoogleplaces.api.response.PlacesResponse
import com.edgarairapetov.testgoogleplaces.utils.GoogleConstants.Companion.WALKING_MODE
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.PolyUtil
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

class MapsViewModel @Inject constructor(var context: Context) : ViewModel() {

    @Inject
    lateinit var apiService: ApiService

    var placesLiveData: MutableLiveData<ArrayList<Result>> = MutableLiveData()
    var errorLiveData: MutableLiveData<String?> = MutableLiveData()
    val pathLiveData = MutableLiveData<MutableList<List<LatLng>>>()

    fun places(typeSet: MutableSet<String>, location: String, radius: Int) {
        viewModelScope.launch {
            try {
                val arrayList = arrayListOf<PlacesResponse>()
                for (type in typeSet) {
                    val request = apiService.getPlaces(type, location, radius)
                    if (!request.isSuccessful) {
                        errorLiveData.postValue(request.message())
                        return@launch
                    }

                    val body = request.body()
                    body?.let { placesResponse ->
                        placesResponse.type = type
                        placesResponse.results?.let { results ->
                            results.forEach { result ->
                                result.type = type
                            }
                        }
                        arrayList.add(placesResponse)
                    }
                }

                val arrayPlaces = arrayListOf<Result>()

                while (arrayList.isNotEmpty()) {
                    val neededElementsCount = (12 - arrayPlaces.size) / arrayList.size

                    if (arrayList.any { it.results?.let { result -> result.size <= neededElementsCount } != false }) {
                        var i = 0
                        while (i < arrayList.size) {
                            arrayList[i].results?.let { results ->
                                if (results.size <= neededElementsCount) {
                                    arrayPlaces.addAll(results)
                                    arrayList.removeAt(i)
                                    i--
                                }
                            } ?: kotlin.run {
                                arrayList.removeAt(i)
                                i--
                            }
                            i++
                        }

                    } else {
                        arrayList[0].results?.let { results ->
                            arrayPlaces.addAll(
                                results.take(
                                    minOf(
                                        neededElementsCount,
                                        results.size
                                    )
                                )
                            )
                        }

                        arrayList.removeAt(0)
                    }
                }

                if (arrayPlaces.isEmpty()) {
                    errorLiveData.postValue(getString(R.string.no_places))
                    return@launch
                }

                placesLiveData.postValue(arrayPlaces)

            } catch (eU: UnknownHostException) {
                errorLiveData.postValue(getString(R.string.server_unavailable))
                eU.stackTrace
            } catch (e: Exception) {
                errorLiveData.postValue(getString(R.string.unknown_error))
                e.stackTrace
            }
        }
    }

    fun route(pointA: LatLng, pointB: LatLng?) {

        if (pointB == null) {
            errorLiveData.postValue(getString(R.string.route_unavailable))
            return
        }

        viewModelScope.launch {
            try {
                val routeRes = apiService.getRoute(
                    "${pointA.latitude},${pointA.longitude}",
                    "${pointB.latitude},${pointB.longitude}", WALKING_MODE
                )
                val path: MutableList<List<LatLng>> = ArrayList()

                if (!routeRes.isSuccessful) {
                    errorLiveData.postValue(getString(R.string.route_error))
                    return@launch
                }

                val steps =
                    routeRes.body()?.routes?.firstOrNull()?.legs?.firstOrNull()?.steps

                steps?.let {

                    it.forEach { step ->
                        step.polyline?.points?.let { stringPath ->
                            path.add(PolyUtil.decode(stringPath))
                        }
                    }

                    pathLiveData.postValue(path)


                } ?: kotlin.run {
                    errorLiveData.postValue(getString(R.string.route_unavailable))
                }


            } catch (e: UnknownHostException) {
                errorLiveData.postValue(getString(R.string.internet_unavailable))
            } catch (e: Exception) {
                errorLiveData.postValue(getString(R.string.error_unknown))
            }
        }
    }

    private fun getString(resId: Int): String {
        return context.resources.getString(resId)
    }
}