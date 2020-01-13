package com.applicaster.plugin.coppa.pocketwatch.ui.controllers

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.applicaster.plugin.coppa.pocketwatch.R
import com.bluelinelabs.conductor.Controller
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.pawegio.kandroid.hide
import com.pawegio.kandroid.show
import kotlinx.android.synthetic.main.initial_screen.view.*
import java.io.InputStreamReader


class ParentGateScreen : Controller() {

    private lateinit var problems: List<Problem>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup) =
        inflater.inflate(R.layout.initial_screen, container, false).apply {
            InputStreamReader(resources?.openRawResource(R.raw.problems), "UTF-8").use {
                problems =
                    GsonBuilder().create().fromJson(it, object : TypeToken<List<Problem>>() {}.type)
            }
            var problem = getRandomProblem()
            fun submit() {
                if (problemAnswer.text.toString() == problem.answer) {
                    onKeyboardHidden { router.push(TermsAgreeScreen()) }
                } else {
                    problemAnswer.text = null
                    error.show()
                    problemAnswer.setBackgroundResource(R.drawable.input_text_error)
                    problem = getRandomProblem()
                    problemText.text = problem.question
                }
            }

            problemText.text = problem.question
            problemAnswer.textChanged {
                error.hide()
                problemAnswerLayout.isErrorEnabled = false
                problemAnswer.setBackgroundResource(R.drawable.input_text)
            }
            submit.setOnClickListener { submit() }
            skip.setOnClickListener { onKeyboardHidden { router.push(WarningDisableScreen()) } }

            problemAnswer.onDoneAction { submit() }
        }

    @SuppressLint("CheckResult")
    private fun View.onKeyboardHidden(function: () -> Unit) {
        observeKeyboard()
            .startWith(isKeyboardShown())
            .filter { !it }
            .firstElement()
            .subscribe { function.invoke() }
        hideKeyboard()
    }

    private fun getRandomProblem() = problems[(System.currentTimeMillis() % problems.size).toInt()]

    data class Problem(@SerializedName("question") val question: String, @SerializedName("answer") val answer: String)
}

