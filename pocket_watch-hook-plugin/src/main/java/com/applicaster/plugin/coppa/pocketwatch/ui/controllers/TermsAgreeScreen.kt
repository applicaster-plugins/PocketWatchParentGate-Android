package com.applicaster.plugin.coppa.pocketwatch.ui.controllers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.NotificationManagerCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.applicaster.plugin.coppa.pocketwatch.R
import com.applicaster.plugin.coppa.pocketwatch.ui.PrivacyPolicyActivity
import com.bluelinelabs.conductor.Controller
import kotlinx.android.synthetic.main.terms_agree_screen.view.*

class TermsAgreeScreen : Controller() {

    companion object {
        private const val SHOW_SUCCESS_SCREEN_KEY = "show_success_screen_key"
    }

    private var showSuccessScreen: Boolean = false

    override fun onActivityResumed(activity: Activity) {
        super.onActivityResumed(activity)
        if (NotificationManagerCompat.from(activity).areNotificationsEnabled() && showSuccessScreen) {
            showSuccessScreen = false
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
                    showSuccessScreen = true
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

    override fun onSaveViewState(view: View, outState: Bundle) {
        super.onSaveViewState(view, outState)
        outState.putBoolean(SHOW_SUCCESS_SCREEN_KEY, showSuccessScreen)
    }

    override fun onRestoreViewState(view: View, savedViewState: Bundle) {
        super.onRestoreViewState(view, savedViewState)
        showSuccessScreen = savedViewState.getBoolean(SHOW_SUCCESS_SCREEN_KEY, false)
    }
}
