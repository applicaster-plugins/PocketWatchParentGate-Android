package com.applicaster.plugin.coppa.pocketwatch.data.service.api

import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface UrbanAirshipAPI {

    @POST("channels/uninstall")
    fun uninstall(@Body channels: List<UninstallChannel>): Single<UninstallResponse>

}
