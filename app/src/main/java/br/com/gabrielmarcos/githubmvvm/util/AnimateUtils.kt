package br.com.gabrielmarcos.githubmvvm.util

import android.view.View

object AnimateUtils {
    fun handleOrientationRotate(view: View) {
        takeIf { view.rotation != 0f }?.run {
            rotateDownLayout(view)
        } ?: rotateUpLayout(view)
    }

    fun rotateUpLayout(
        view: View
    ) = view.animate().rotation(90F)

    fun rotateDownLayout(
        view: View
    ) = view.animate().rotation(0F)
}