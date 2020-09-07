package com.example.farmerapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.farmerapp.model.Farm
import com.example.farmerapp.R
import kotlinx.android.synthetic.main.list_item.view.*


class RecyclerAdapter(val list:List<Farm>, val onClick:(id:Int)->Unit) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){

        fun bind(farm: Farm){
            itemView.setOnClickListener {
                onClick(farm.id)
            }
            itemView.img.setImageResource(farm.img)
            itemView.title.text = farm.title
            itemView.desc.text = "${farm.latLng.latitude},${farm.latLng.longitude}"
        }

    }

}