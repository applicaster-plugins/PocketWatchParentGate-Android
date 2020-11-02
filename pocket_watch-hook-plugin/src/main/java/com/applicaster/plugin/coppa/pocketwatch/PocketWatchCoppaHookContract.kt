package com.applicaster.plugin.coppa.pocketwatch

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.applicaster.plugin.coppa.pocketwatch.data.service.AccountDataProviderImpl
import com.applicaster.plugin.coppa.pocketwatch.data.service.FirebasePushService
import com.applicaster.plugin.coppa.pocketwatch.data.service.util.registerLocalBroadcast
import com.applicaster.plugin.coppa.pocketwatch.ui.NotificationSettingsActivity
import com.applicaster.plugin.coppa.pocketwatch.ui.NotificationSettingsFragment
import com.applicaster.plugin.coppa.pocketwatch.ui.ParentGateActivity
import com.applicaster.plugin.coppa.pocketwatch.util.DebugMessageActivity
import com.applicaster.plugin.coppa.pocketwatch.util.DebugUtil
import com.applicaster.plugin_manager.hook.ApplicationLoaderHookUpI
import com.applicaster.plugin_manager.hook.HookListener
import com.applicaster.plugin_manager.screen.PluginScreen
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber
import java.io.Serializable
import java.util.*


class PocketWatchCoppaHookContract : ApplicationLoaderHookUpI, PluginScreen {

    private val firebasePushService by lazy { FirebasePushService() }
    private val accountDataProvider by lazy { AccountDataProviderImpl() }

    companion object {
        const val ALL_CHECKS_PASSED = "all_checks_passed_coppa_key"
        const val NOTIFICATIONS_DISABLED = "check_skipped_coppa_key"
    }

    override fun present(
        context: Context?,
        screenMap: HashMap<String, Any>?,
        dataSource: Serializable?,
        isActivity: Boolean
    ) {
        context?.let { initTimber(it) }
        context?.registerLocalBroadcast(ALL_CHECKS_PASSED) {
            accountDataProvider.parentGatePassed = true
            subscribePush(null, context)
        }
        context?.registerLocalBroadcast(NOTIFICATIONS_DISABLED) {
            unsubscribePush(null, context)
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
        initTimber(context)
        Timber.d("executeOnStartup")
        listener.onHookFinished()
    }

    @SuppressLint("CheckResult")
    override fun executeOnApplicationReady(context: Context, listener: HookListener) {
        initTimber(context)
        Timber.d("executeOnApplicationReady")
        if (!NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            firebasePushService.unsubscribePush().subscribe()
            accountDataProvider.parentGatePassed = false
        } else if (accountDataProvider.parentGatePassed) {
            listener.onHookFinished()
        } else {
            context.registerLocalBroadcast(ALL_CHECKS_PASSED) {
                accountDataProvider.parentGatePassed = true
                subscribePush(listener, context)
            }
            context.registerLocalBroadcast(NOTIFICATIONS_DISABLED) {
                accountDataProvider.parentGatePassed = false
                unsubscribePush(listener, context)
            }
            ParentGateActivity.launch(context)
        }
    }

    private fun initTimber(context: Context) {
        if (DebugUtil.isDebug(context)) {
            if (Timber.treeCount() == 0) Timber.plant(Timber.DebugTree())
        }
    }

    @SuppressLint("CheckResult")
    private fun subscribePush(listener: HookListener?, context: Context) {
        firebasePushService.subscribePush()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    showMessage(context, "Firebase push subscribed successfully")
                    listener?.onHookFinished()
                },
                {
                    Timber.e(it)
                    showMessage(context, "Firebase push subscribe failed")
                    listener?.onHookFinished()
                }
            )
    }

    @SuppressLint("CheckResult")
    private fun unsubscribePush(listener: HookListener?, context: Context) {
        firebasePushService.unsubscribePush()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    showMessage(context, "Firebase push unsubscribed successfully")
                    listener?.onHookFinished()
                },
                {
                    Timber.e(it)
                    showMessage(context, "Firebase push unsubscribe failed")
                    listener?.onHookFinished()
                }
            )
    }

    private fun showMessage(context: Context, message: String) {
        if (DebugUtil.isDebug(context)) {
            DebugMessageActivity.launch(context, message)
            Timber.d(message)
        }
    }

    override fun setPluginConfigurationParams(params: MutableMap<Any?, Any?>) {
        Timber.d("Plugin params: $params")
        accountDataProvider.termsUrl = params["terms_url"] as String
    }
}
