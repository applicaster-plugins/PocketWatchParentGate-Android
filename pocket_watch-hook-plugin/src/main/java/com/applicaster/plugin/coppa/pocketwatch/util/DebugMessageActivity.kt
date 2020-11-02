package com.applicaster.plugin.coppa.pocketwatch.util

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.applicaster.plugin.coppa.pocketwatch.R
import kotlinx.android.synthetic.main.activity_message.*


class DebugMessageActivity : AppCompatActivity() {

    companion object {
        private const val MESSAGE_KEY = "message_key"

        fun launch(context: Context, message: String) {
            if (DebugUtil.isDebug(context).not()) {
                return
            }
            context.startActivity(
                Intent(
                    context,
                    DebugMessageActivity::class.java
                ).apply {
                    putExtra(MESSAGE_KEY, message)
                })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        okButton.setOnClickListener { finish() }
        message.text = intent.getStringExtra(MESSAGE_KEY) ?: "No message"
    }
}
