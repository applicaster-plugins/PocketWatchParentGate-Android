package com.applicaster.plugin.coppa.pocketwatch.data.service

import com.applicaster.plugin.coppa.pocketwatch.data.service.api.UrbanAirshipAPI
import com.urbanairship.UAirship
import io.reactivex.Single

interface UrbanAirshipService {
    fun unsubscribePush(): Single<Boolean>
    fun subscribePush(): Single<Boolean>
}

class UrbanAirshipServiceImpl(val urbanAirshipAPI: UrbanAirshipAPI) : UrbanAirshipService {

    override fun unsubscribePush(): Single<Boolean> {
        UAirship.shared().pushManager.userNotificationsEnabled = false
        return Single.just(true)
//        return Single.create<String> { it.onSuccess(!!) }
//            .flatMap { urbanAirshipAPI.uninstall(listOf(UninstallChannel(it, "android"))) }
//            .map { it.success }
//            .subscribeOn(Schedulers.io())
    }

    override fun subscribePush(): Single<Boolean> {
        UAirship.shared().pushManager.userNotificationsEnabled = true
        return Single.just(true)
    }
}
