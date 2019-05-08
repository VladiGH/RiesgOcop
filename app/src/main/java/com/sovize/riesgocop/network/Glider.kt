package com.sovize.riesgocop.network

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.sovize.riesgocop.R

object Glider {

    private val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()

    fun load(context: Context, address: String, v: ImageView){
            Glide.with(context).load(address)
                .fitCenter()
                .transition(DrawableTransitionOptions.withCrossFade(factory))
                .placeholder(R.drawable.ic_image_black_48dp)
                .error(R.drawable.ic_broken_image_black_48dp)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(v)

    }

}