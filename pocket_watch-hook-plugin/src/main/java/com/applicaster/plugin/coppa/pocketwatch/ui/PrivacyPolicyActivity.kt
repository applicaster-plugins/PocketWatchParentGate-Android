package com.applicaster.plugin.coppa.pocketwatch.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.applicaster.plugin.coppa.pocketwatch.R
import com.applicaster.plugin.coppa.pocketwatch.data.service.AccountDataProviderImpl
import kotlinx.android.synthetic.main.privacy_policy_screen.*


class PrivacyPolicyActivity  : AppCompatActivity() {

    private val accountDataProvider by lazy { AccountDataProviderImpl() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.privacy_policy_screen)
        webView.loadUrl(accountDataProvider.termsUrl)
    }
}
