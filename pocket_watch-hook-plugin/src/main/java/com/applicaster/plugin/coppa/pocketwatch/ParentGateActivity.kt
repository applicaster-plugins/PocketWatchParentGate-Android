package com.applicaster.plugin.coppa.pocketwatch

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.applicaster.plugin.coppa.pocketwatch.controllers.ParentGateScreen
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
        fun launch(context: Context) {
            context.startActivity(Intent(context, ParentGateActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent_gate)

        router = Conductor.attachRouter(this, controllerContainer, savedInstanceState)
        if (!router.hasRootController()) {
            router.setRoot(RouterTransaction.with(ParentGateScreen()))
        }
    }


    override fun onBackPressed() {
        if (!router.handleBack()) {
            super.onBackPressed()
        }
    }
}
