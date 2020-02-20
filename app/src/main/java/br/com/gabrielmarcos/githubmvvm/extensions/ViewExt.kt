package br.com.gabrielmarcos.githubmvvm.extensions

import android.view.View

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide(keepInLayout: Boolean = false) {
    visibility = if (keepInLayout) View.INVISIBLE else View.GONE
}

fun View.showIf(setVisible: Boolean, keepInLayout: Boolean = false) {
    setVisible.then { show() } ?: hide(keepInLayout)
}