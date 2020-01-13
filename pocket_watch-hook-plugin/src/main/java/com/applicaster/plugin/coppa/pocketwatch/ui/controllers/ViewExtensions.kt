package com.applicaster.plugin.coppa.pocketwatch.ui.controllers

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.design.widget.TextInputEditText
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

fun Router.push(controller: Controller) {
    pushController(
        RouterTransaction.with(controller)
            .pushChangeHandler(FadeChangeHandler())
            .popChangeHandler(FadeChangeHandler())
    )
}

fun Router.pop() = popCurrentController()

fun TextView.textChanged(function: () -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            function.invoke()
        }

    })
}

fun TextInputEditText.onDoneAction(function: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            function.invoke()
            true
        } else {
            false
        }
    }
}

fun Context.openAppNotificationSettings() {
    val intent = Intent().apply {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                action = "android.settings.APP_NOTIFICATION_SETTINGS"
                putExtra("app_package", packageName)
                putExtra("app_uid", applicationInfo.uid)
            }
            else -> {
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                addCategory(Intent.CATEGORY_DEFAULT)
                data = Uri.parse("package:" + packageName)
            }
        }
    }
    startActivity(intent)
}

@Suppress("MagicNumber")
fun View.observeKeyboard() = RxView.globalLayouts(rootView)
    .map { isKeyboardShown() }
    .distinctUntilChanged()
    .flatMap {
        if (it) {
            Observable.just(true)
        } else {
            Observable.just(it)
                .debounce(50, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

/**
 * Hides keyboard
 */
fun View?.hideKeyboard() {
    val imm = this?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(this?.windowToken, 0)
}

/**
 * Returns true if keyboard was shown
 */
@Suppress("MagicNumber")
fun View.isKeyboardShown(): Boolean {
    val screenHeight = resources.displayMetrics.heightPixels.toFloat()
    val r = Rect()
    rootView.getWindowVisibleDisplayFrame(r)
    // Ratio, which helps to understand that you app window was reduced more then on 20%
    return r.height() / screenHeight < 0.8f
}

