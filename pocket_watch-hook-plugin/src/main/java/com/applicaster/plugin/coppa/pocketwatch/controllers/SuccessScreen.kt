package com.applicaster.plugin.coppa.pocketwatch.controllers

import android.view.LayoutInflater
import android.view.ViewGroup
import com.applicaster.plugin.coppa.pocketwatch.PocketWatchCoppaHookContract
import com.applicaster.plugin.coppa.pocketwatch.R
import com.applicaster.plugin.coppa.pocketwatch.data.service.util.sendBroadcast
import com.bluelinelabs.conductor.Controller
import kotlinx.android.synthetic.main.success_screen.view.*


class SuccessScreen : Controller() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup) =
        inflater.inflate(R.layout.success_screen, container, false).apply {
            ok.setOnClickListener {
                context.sendBroadcast(PocketWatchCoppaHookContract.ALL_CHECKS_PASSED)
                activity?.finish()
            }
        }
}
