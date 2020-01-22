package com.applicaster.plugin.coppa.pocketwatch.data.service

import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

interface PushService {
    fun unsubscribePush(): Completable
    fun subscribePush(): Completable
}

class FirebasePushService : PushService {

    override fun unsubscribePush(): Completable {
        return Completable.create {
            FirebaseInstanceId.getInstance().deleteInstanceId()
            it.onComplete()
        }
            .subscribeOn(Schedulers.io())
    }

    override fun subscribePush(): Completable {
        return Completable.create {
            var token = FirebaseInstanceId.getInstance().token
            while(token == null) {
                token = FirebaseInstanceId.getInstance().token
            }
            it.onComplete()
        }
            .subscribeOn(Schedulers.io())
    }
}
