package com.applicaster.plugin.coppa.pocketwatch.data.service.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager

fun Context.registerLocalBroadcast(filterAction: String, callback: (intent: Intent) -> Any) =
    LocalBroadcastManager.getInstance(this).registerReceiver(object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            callback.invoke(intent)
        }
    }, IntentFilter(filterAction))

fun Context.sendBroadcast(filterAction: String) =
    LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(filterAction))
