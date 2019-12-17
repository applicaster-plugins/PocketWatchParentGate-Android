package com.applicaster.plugin.coppa.pocketwatch

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.app.NotificationManagerCompat
import com.applicaster.plugin.coppa.pocketwatch.data.service.AccountDataProviderImpl
import com.applicaster.plugin.coppa.pocketwatch.data.service.ApiFactoryImpl
import com.applicaster.plugin.coppa.pocketwatch.data.service.UrbanAirshipServiceImpl
import com.applicaster.plugin.coppa.pocketwatch.data.service.util.registerLocalBroadcast
import com.applicaster.plugin_manager.hook.ApplicationLoaderHookUpI
import com.applicaster.plugin_manager.hook.HookListener
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber

class PocketWatchCoppaHookContract : ApplicationLoaderHookUpI {

    private var masterSecret: String? = null

    private val apiFactory by lazy { ApiFactoryImpl(masterSecret!!) }
    private val airshipService by lazy { UrbanAirshipServiceImpl(apiFactory.urbanAirshipAPI) }
    private val accountDataProvider by lazy { AccountDataProviderImpl() }

    companion object {
        const val ALL_CHECKS_PASSED = "all_checks_passed_coppa_key"
        const val NOTIFICATIONS_DISABLED = "check_skipped_coppa_key"
    }

    init {
        if (Timber.treeCount() == 0) Timber.plant(Timber.DebugTree())
    }

    override fun executeOnStartup(context: Context, listener: HookListener) {
        Timber.d("executeOnStartup")
        listener.onHookFinished()
    }

    @SuppressLint("CheckResult")
    override fun executeOnApplicationReady(context: Context, listener: HookListener) {
        Timber.d("executeOnApplicationReady")
        if (!NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            airshipService.unsubscribe()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ listener.onHookFinished() }, {
                    Timber.i(it)
                    listener.onHookFinished()
                })
            accountDataProvider.parentGatePassed = false
        } else if (accountDataProvider.parentGatePassed) {
            listener.onHookFinished()
        } else {
            context.registerLocalBroadcast(ALL_CHECKS_PASSED) {
                accountDataProvider.parentGatePassed = true
                listener.onHookFinished()
            }
            context.registerLocalBroadcast(NOTIFICATIONS_DISABLED) {
                airshipService.unsubscribe()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ listener.onHookFinished() }, {
                        Timber.i(it)
                        listener.onHookFinished()
                    })
            }
            ParentGateActivity.launch(context)
        }
    }

    override fun setPluginConfigurationParams(params: MutableMap<Any?, Any?>) {
        Timber.d("Plugin params: $params")
        this.masterSecret = params["masterSecret"] as? String
    }
}
