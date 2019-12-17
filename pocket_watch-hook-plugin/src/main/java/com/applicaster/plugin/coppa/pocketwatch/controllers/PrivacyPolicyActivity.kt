package com.applicaster.plugin.coppa.pocketwatch.controllers

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.applicaster.plugin.coppa.pocketwatch.R
import kotlinx.android.synthetic.main.privacy_policy_screen.*


class PrivacyPolicyActivity  : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.privacy_policy_screen)
        webView.loadUrl("https://pocket.watch/app/terms-of-service")
    }
}
