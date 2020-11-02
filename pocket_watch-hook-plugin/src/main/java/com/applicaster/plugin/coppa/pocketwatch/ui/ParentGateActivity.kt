package com.applicaster.plugin.coppa.pocketwatch.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.applicaster.plugin.coppa.pocketwatch.R
import com.applicaster.plugin.coppa.pocketwatch.ui.controllers.ParentGateScreen
import com.applicaster.plugin.coppa.pocketwatch.ui.controllers.WarningDisableScreen
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import kotlinx.android.synthetic.main.activity_parent_gate.*


/**
 * Parent Gate (age verification) Activity.
 * @see PocketWatchCoppaHookContract for usage
 */
class ParentGateActivity : AppCompatActivity() {

    private lateinit var router: Router

    companion object {
        private const val INITIAL_SCREEN_KEY = "initial_screen_key"
        const val WARNING_SCREEN = "warning_screen"

        fun launch(context: Context, initialState: String? = null) {
            context.startActivity(Intent(context, ParentGateActivity::class.java).apply {
                putExtra(INITIAL_SCREEN_KEY, initialState)
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent_gate)

        router = Conductor.attachRouter(this, controllerContainer, savedInstanceState)
        if (!router.hasRootController()) {
            router.setRoot(
                RouterTransaction.with(
                    if (intent.getStringExtra(INITIAL_SCREEN_KEY) == WARNING_SCREEN) {
                        WarningDisableScreen()
                    } else {
                        ParentGateScreen()
                    }
                )
            )
        }
    }

    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }
}
