package com.sovize.riesgocop.views.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sovize.riesgocop.R
import com.sovize.riesgocop.models.CalculatorProduct
import kotlinx.android.synthetic.main.item_list_calculator.view.*

class CalculatorAdapter(val items: List<CalculatorProduct>, private val clickListener: (CalculatorProduct)-> Unit)
    :RecyclerView.Adapter<CalculatorAdapter.ViewHolder> (){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_calculator,parent,false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], clickListener)
        if((position % 2)==0){
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }else{
            holder.itemView.setBackgroundColor(Color.parseColor("#D8D8D8"))
        }
    }

    override fun getItemCount(): Int {
    return items.size
    }


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(item: CalculatorProduct,clickListener:(CalculatorProduct)->Unit)= with(itemView){

            this.setOnClickListener{clickListener(item)}
        }
    }


}