package com.example.codebaseandroidapp.utils

import timber.log.Timber

class DebugTree: Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement): String {
        return String.format("%s-%s", super.createStackElementTag(element), element.lineNumber)
    }
}

class NonDebugTree: Timber.DebugTree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {

    }
}