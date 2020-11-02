package com.applicaster.plugin.coppa.pocketwatch.util

import android.content.Context
import android.content.pm.ApplicationInfo

object DebugUtil {
    fun isDebug(context: Context): Boolean =
        context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE !== 0
}
