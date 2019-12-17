package com.applicaster.plugin.coppa.pocketwatch.data.service

import com.applicaster.plugin.coppa.pocketwatch.data.service.api.UninstallChannel
import com.applicaster.plugin.coppa.pocketwatch.data.service.api.UrbanAirshipAPI
import com.urbanairship.UAirship
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

interface UrbanAirshipService {
    fun unsubscribe(): Single<Boolean>
}

class UrbanAirshipServiceImpl(val urbanAirshipAPI: UrbanAirshipAPI) : UrbanAirshipService {

    override fun unsubscribe(): Single<Boolean> {
        return Single.create<String> { it.onSuccess(UAirship.shared().pushManager.channelId!!) }
            .flatMap { urbanAirshipAPI.uninstall(listOf(UninstallChannel(it, "android"))) }
            .map { it.success }
            .subscribeOn(Schedulers.io())
    }
}
