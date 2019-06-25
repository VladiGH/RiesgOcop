package com.sovize.ultracop.views.adapters

import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sovize.ultracop.R
import kotlinx.android.synthetic.main.list_item_photo.view.*


class ReportPhotoAdapter(var photosItems: MutableList<String>): RecyclerView.Adapter<ReportPhotoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_photo, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return photosItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(photosItems[position])
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bind(photo: String) = with(itemView) {
            val imageBitmap = ThumbnailUtils.extractThumbnail(
                BitmapFactory.decodeFile(photo), 400, 300)
            this.list_photo.setImageBitmap(imageBitmap)
        }
    }
}