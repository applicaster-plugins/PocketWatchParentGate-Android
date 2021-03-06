package com.applicaster.plugin.coppa.pocket_watch.sample

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.applicaster.plugin.coppa.pocketwatch.ui.controllers.openAppNotificationSettings
import com.applicaster.plugin_manager.PluginManager
import com.applicaster.plugin_manager.screen.PluginScreen
import com.google.firebase.FirebaseApp
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
        FirebaseApp.initializeApp(this)

        val hookPlugin = PluginManager.getInstance().hookPluginList.first()
        executeOnApplicationReady.setOnClickListener {
            hookPlugin.executeOnApplicationReady(this) {
                Timber.i("Home screen loaded")
                Toast.makeText(this, "Home screen loaded", Toast.LENGTH_LONG).show()
            }
        }
        notificationSettings.setOnClickListener { openAppNotificationSettings() }

        startInAppSettings.setOnClickListener {
            (hookPlugin as PluginScreen).present(this, null, null, true)
        }
    }
}
