package com.applicaster.plugin.coppa.pocketwatch.data.service

import com.applicaster.util.PreferenceUtil

interface AccountDataProvider {
    var parentGatePassed: Boolean
    var termsUrl: String
}

class AccountDataProviderImpl : AccountDataProvider {
    companion object {
        private const val PARENT_GATE_PASSED_KEY = "parent_gate_passed_key_coppa_pocket_watch"
        private const val TERMS_URL_KEY = "terms_url_key_coppa_pocket_watch"
        private const val PREFERENCES_NAME = "pocket_watch_coppa_plugin_account_prefs"
    }

    private val prefs = PreferenceUtil.getInstance(PREFERENCES_NAME)

    override var parentGatePassed: Boolean
        get() = prefs.getBooleanPref(PARENT_GATE_PASSED_KEY, false)
        set(value) {
            prefs.setBooleanPref(PARENT_GATE_PASSED_KEY, value)
        }

    override var termsUrl: String
        get() = prefs.getStringPref(TERMS_URL_KEY, "https://pocket.watch/app/privacy")
        set(value) {
            prefs.setStringPref(TERMS_URL_KEY, value)
        }
}
