package br.com.gabrielmarcos.githubmvvm.util

import android.widget.ImageView
import br.com.gabrielmarcos.githubmvvm.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Glide.with(view.context)
            .load(imageUrl)
            .error(R.drawable.bg_placeholder)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view)
    }
}