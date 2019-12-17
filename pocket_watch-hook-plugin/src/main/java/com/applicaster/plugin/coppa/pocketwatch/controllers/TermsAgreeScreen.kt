package com.applicaster.plugin.coppa.pocketwatch.controllers

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.applicaster.plugin.coppa.pocketwatch.R
import com.bluelinelabs.conductor.Controller
import com.pawegio.kandroid.start
import kotlinx.android.synthetic.main.terms_agree_screen.view.*


class TermsAgreeScreen : Controller() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup) =
        inflater.inflate(R.layout.terms_agree_screen, container, false).apply {
            ok.setOnClickListener { router.push(SuccessScreen()) }
            noThanks.setOnClickListener { router.push(WarningDisableScreen()) }
            privacyPolicy.setOnClickListener {
                Intent(context, PrivacyPolicyActivity::class.java).start(context)
            }
        }
}
