package com.applicaster.plugin.coppa.pocketwatch.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.applicaster.plugin.coppa.pocketwatch.R
import com.applicaster.plugin.coppa.pocketwatch.ui.ParentGateActivity.Companion.WARNING_SCREEN
import kotlinx.android.synthetic.main.notification_settings_fragment.*


class NotificationSettingsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.notification_settings_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = view.run {
        val notificationManager = NotificationManagerCompat.from(context)
        notificationsSwitch.isChecked = notificationManager.areNotificationsEnabled()
        notificationsSwitch.setOnClickListener {
            if (notificationManager.areNotificationsEnabled()) {
                ParentGateActivity.launch(context, WARNING_SCREEN)
            } else {
                ParentGateActivity.launch(context)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        view?.apply {
            val notificationManager = NotificationManagerCompat.from(context)
            notificationsSwitch.isChecked = notificationManager.areNotificationsEnabled()
        }
    }
}
