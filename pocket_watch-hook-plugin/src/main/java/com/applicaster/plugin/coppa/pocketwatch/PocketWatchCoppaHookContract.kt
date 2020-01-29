package com.applicaster.plugin.coppa.pocketwatch

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.NotificationManagerCompat
import com.applicaster.plugin.coppa.pocketwatch.data.service.AccountDataProviderImpl
import com.applicaster.plugin.coppa.pocketwatch.data.service.FirebasePushService
import com.applicaster.plugin.coppa.pocketwatch.data.service.util.registerLocalBroadcast
import com.applicaster.plugin.coppa.pocketwatch.ui.NotificationSettingsActivity
import com.applicaster.plugin.coppa.pocketwatch.ui.NotificationSettingsFragment
import com.applicaster.plugin.coppa.pocketwatch.ui.ParentGateActivity
import com.applicaster.plugin_manager.hook.ApplicationLoaderHookUpI
import com.applicaster.plugin_manager.hook.HookListener
import com.applicaster.plugin_manager.screen.PluginScreen
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber
import java.io.Serializable
import java.util.*

class PocketWatchCoppaHookContract : ApplicationLoaderHookUpI, PluginScreen {


    //    private var masterSecret: String? = null
//    private val apiFactory by lazy { ApiFactoryImpl(masterSecret!!) }
    private val firebasePushService by lazy { FirebasePushService() }
    private val accountDataProvider by lazy { AccountDataProviderImpl() }

    companion object {
        const val ALL_CHECKS_PASSED = "all_checks_passed_coppa_key"
        const val NOTIFICATIONS_DISABLED = "check_skipped_coppa_key"
    }

    init {
        if (Timber.treeCount() == 0) Timber.plant(Timber.DebugTree())
    }

    override fun present(
        context: Context?,
        screenMap: HashMap<String, Any>?,
        dataSource: Serializable?,
        isActivity: Boolean
    ) {
        context?.registerLocalBroadcast(ALL_CHECKS_PASSED) {
            accountDataProvider.parentGatePassed = true
            firebasePushService.subscribePush().subscribe()
        }
        context?.registerLocalBroadcast(NOTIFICATIONS_DISABLED) {
            firebasePushService.unsubscribePush().subscribe()
        }
        if (isActivity) {
            NotificationSettingsActivity.launch(context!!)
        } else {
            generateFragment(screenMap, dataSource)
        }
    }

    override fun generateFragment(
        screenMap: HashMap<String, Any>?,
        dataSource: Serializable?
    ): Fragment {
        return NotificationSettingsFragment()
    }

    override fun executeOnStartup(context: Context, listener: HookListener) {
        Timber.d("executeOnStartup")
        listener.onHookFinished()
    }

    @SuppressLint("CheckResult")
    override fun executeOnApplicationReady(context: Context, listener: HookListener) {
        Timber.d("executeOnApplicationReady")
        if (!NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            firebasePushService.unsubscribePush()
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
                firebasePushService.subscribePush()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ listener.onHookFinished() }, {
                        Timber.i(it)
                        listener.onHookFinished()
                    })
            }
            context.registerLocalBroadcast(NOTIFICATIONS_DISABLED) {
                accountDataProvider.parentGatePassed = false
                firebasePushService.unsubscribePush()
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
        accountDataProvider.termsUrl = params["terms_url"] as String
    }
}
