package com.applicaster.plugin.coppa.pocketwatch.data.service

import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

interface PushService {
    fun unsubscribePush(): Completable
    fun subscribePush(): Completable
}

class FirebasePushService : PushService {

    override fun unsubscribePush(): Completable {
        return Completable.create {
            FirebaseInstanceId.getInstance().deleteInstanceId()
            Timber.d("delete InstanceID request initiated to the backend")
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
            Timber.d("create InstanceID finished successfully")
            it.onComplete()
        }
            .subscribeOn(Schedulers.io())
    }
}
