package com.applicaster.plugin.coppa.pocketwatch.controllers

import android.app.Activity
import android.support.v4.app.NotificationManagerCompat
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.applicaster.plugin.coppa.pocketwatch.PocketWatchCoppaHookContract
import com.applicaster.plugin.coppa.pocketwatch.R
import com.applicaster.plugin.coppa.pocketwatch.data.service.util.sendBroadcast
import com.bluelinelabs.conductor.Controller
import kotlinx.android.synthetic.main.warning_disable_screen.view.*


class WarningDisableScreen : Controller() {

    override fun onActivityResumed(activity: Activity) {
        super.onActivityResumed(activity)
        if (!NotificationManagerCompat.from(activity).areNotificationsEnabled()) {
            activity.sendBroadcast(PocketWatchCoppaHookContract.NOTIFICATIONS_DISABLED)
            activity.finish()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup) =
        inflater.inflate(R.layout.warning_disable_screen, container, false).apply {
            ok.setOnClickListener {
                context.openAppNotificationSettings()
                Toast.makeText(context, R.string.disable_notifications_manually, Toast.LENGTH_LONG).show()
            }
            enable.setOnClickListener { router.pop() }
        }
}
