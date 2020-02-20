package br.com.gabrielmarcos.githubmvvm.util

import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import br.com.gabrielmarcos.githubmvvm.R

object NavigationCustom {
    val navOptionsRight: NavOptions.Builder
        get() = NavOptions.Builder()
            .setEnterAnim(R.anim.navigation_pop_enter_slide_right)
            .setExitAnim(R.anim.navigation_pop_exit_slide_right)
            .setPopEnterAnim(R.anim.navigation_enter_slide_left)
            .setPopExitAnim(R.anim.navigation_exit_slide_left)

    fun navigateRight(
        fragment: Fragment,
        directAction: NavDirections
    ) {
        val navController = fragment.findNavController()
        navController.navigate(directAction, navOptionsRight.build())
    }
}
