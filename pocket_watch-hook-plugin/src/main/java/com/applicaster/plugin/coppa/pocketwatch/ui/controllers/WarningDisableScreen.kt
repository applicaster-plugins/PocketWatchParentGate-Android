package com.applicaster.plugin.coppa.pocketwatch.ui.controllers

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
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
                val notificationManager = NotificationManagerCompat.from(context)
                if (notificationManager.areNotificationsEnabled()) {
                    context.openAppNotificationSettings()
                    Toast.makeText(
                        context,
                        R.string.disable_notifications_manually,
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    activity?.sendBroadcast(PocketWatchCoppaHookContract.NOTIFICATIONS_DISABLED)
                    activity?.finish()
                }
            }
            enable.setOnClickListener {
                if (router.backstackSize > 1) {
                    router.pop()
                } else {
                    (context as Activity).finish()
                }
            }
        }
}
