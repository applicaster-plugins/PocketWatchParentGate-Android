package com.applicaster.plugin.coppa.pocketwatch.ui.controllers

import android.app.Activity
import android.content.Intent
import android.support.v4.app.NotificationManagerCompat
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.applicaster.plugin.coppa.pocketwatch.R
import com.applicaster.plugin.coppa.pocketwatch.ui.PrivacyPolicyActivity
import com.bluelinelabs.conductor.Controller
import kotlinx.android.synthetic.main.terms_agree_screen.view.*

class TermsAgreeScreen : Controller() {

    override fun onActivityResumed(activity: Activity) {
        super.onActivityResumed(activity)
        if (NotificationManagerCompat.from(activity).areNotificationsEnabled()) {
            router.push(SuccessScreen())
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup) =
        inflater.inflate(R.layout.terms_agree_screen, container, false).apply {
            ok.setOnClickListener {
                val notificationManager = NotificationManagerCompat.from(context)
                if (notificationManager.areNotificationsEnabled()) {
                    router.push(SuccessScreen())
                } else {
                    context.openAppNotificationSettings()
                    Toast.makeText(
                        context,
                        R.string.enable_notifications_manually,
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
            noThanks.setOnClickListener { router.push(WarningDisableScreen()) }
            privacyPolicy.setOnClickListener {
                context.startActivity(Intent(context, PrivacyPolicyActivity::class.java))
            }
        }
}
