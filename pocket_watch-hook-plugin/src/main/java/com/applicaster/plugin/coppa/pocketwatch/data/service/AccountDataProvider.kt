package com.applicaster.plugin.coppa.pocketwatch.data.service

import com.applicaster.util.PreferenceUtil

interface AccountDataProvider {
    var parentGatePassed: Boolean
}

class AccountDataProviderImpl : AccountDataProvider {
    companion object {
        private const val PARENT_GATE_PASSED_KEY = "parent_gate_passed_key_coppa_pocket_watch"
        private const val PREFERENCES_NAME = "pocket_watch_coppa_plugin_account_prefs"
    }

    private val prefs = PreferenceUtil.getInstance(PREFERENCES_NAME)

    override var parentGatePassed: Boolean
        get() = prefs.getBooleanPref(PARENT_GATE_PASSED_KEY, false)
        set(value) {
            prefs.setBooleanPref(PARENT_GATE_PASSED_KEY, value)
        }
}
