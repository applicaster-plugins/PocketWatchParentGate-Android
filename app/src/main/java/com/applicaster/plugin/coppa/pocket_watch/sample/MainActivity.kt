package com.applicaster.plugin.coppa.pocket_watch.sample

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.widget.Toast
import com.applicaster.plugin.coppa.pocketwatch.controllers.openAppNotificationSettings
import com.applicaster.plugin_manager.PluginManager
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

/**
 *
 * Check plugin_configurations.json to configure
 */
class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Timber.plant(Timber.DebugTree())

        val hookPlugin = PluginManager.getInstance().hookPluginList.first()
        executeOnApplicationReady.setOnClickListener {
            hookPlugin.executeOnApplicationReady(this) {
                Timber.i("Home screen loaded")
                Toast.makeText(this, "Home screen loaded", Toast.LENGTH_LONG).show()
            }
        }
        notificationSettings.setOnClickListener { openAppNotificationSettings() }
    }
}
