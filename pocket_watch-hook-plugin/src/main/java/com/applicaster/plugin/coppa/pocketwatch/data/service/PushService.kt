package com.applicaster.plugin.coppa.pocketwatch.data.service

import com.google.firebase.FirebaseApp
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

interface PushService {
    fun unsubscribePush(): Completable
    fun subscribePush(): Completable
}

class FirebasePushService : PushService {
    private val firebaseMessaging = lazy {
        FirebaseMessaging.getInstance()
    }

    private val firebaseInstallations = lazy { FirebaseInstallations.getInstance() }

    override fun unsubscribePush(): Completable {
        return Completable.create { emitter ->
            if (isFirebaseAppInitialized().not()) {
                emitter.onComplete()
                return@create
            }
            firebaseInstallations.value.delete().apply {
                addOnSuccessListener {
                    firebaseMessaging.value.deleteToken().apply {
                        addOnSuccessListener { emitter.onComplete() }
                        addOnFailureListener { emitter.tryOnError(it) }
                    }
                }
                addOnFailureListener { emitter.tryOnError(it) }
            }
        }
            .subscribeOn(Schedulers.io())
    }

    override fun subscribePush(): Completable {
        return Completable.create { emitter ->
            if (isFirebaseAppInitialized().not()) {
                emitter.onComplete()
                return@create
            }
            firebaseInstallations.value.id.apply {
                addOnSuccessListener {
                    firebaseMessaging.value.token.apply {
                        addOnSuccessListener { emitter.onComplete() }
                        addOnFailureListener { emitter.tryOnError(it) }
                    }
                }
                addOnFailureListener { emitter.tryOnError(it) }
            }
        }
            .subscribeOn(Schedulers.io())
    }

    private fun isFirebaseAppInitialized() = try {
        FirebaseApp.getInstance()
        true
    } catch (e: IllegalStateException) {
        false
    }

}
