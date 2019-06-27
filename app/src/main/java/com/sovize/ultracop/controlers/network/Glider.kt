package com.sovize.ultracop.controlers.network


import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.sovize.ultracop.R

object Glider {

    /**
     * this class is a wrapper around the regular way of handling glide requests
     * supporting animations ans setting a default placeholder for both erros and
     * temporal ones
     */
    private val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()

    fun load(
        address: String,
        v: ImageView,
        placeholder: Int = R.drawable.ic_image_black_48dp,
        errorHolder: Int = R.drawable.ic_broken_image_black_48dp
    ) {
        Glide.with(v.context).load(address)
            .fitCenter()
            .transition(DrawableTransitionOptions.withCrossFade(factory))
            .placeholder(placeholder)
            .error(errorHolder)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(v)
    }

    //same as above but in a circle
    fun loadCircle(
        address: String,
        v: ImageView,
        placeholder: Int = R.drawable.ic_image_black_48dp,
        errorHolder: Int = R.drawable.ic_broken_image_black_48dp
    ) {
        Glide.with(v.context)
            .load(address)
            .fitCenter()
            .transition(DrawableTransitionOptions.withCrossFade(factory))
            .placeholder(placeholder)
            .error(errorHolder)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .apply(RequestOptions.circleCropTransform())
            .into(v)
    }

}