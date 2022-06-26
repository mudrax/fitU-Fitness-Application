package com.mudrax.fitu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mudrax.fitu.databinding.ItemViewBinding

class HistoryAdapter(val items:ArrayList<String>):RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    class ViewHolder(binding: ItemViewBinding):RecyclerView.ViewHolder(binding.root)
    {
        val llHistoryMainItem = binding.llItem
        val tvItem = binding.tvllDate
        val tvPosition = binding.tvllNum
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemViewBinding.inflate
            (LayoutInflater.from(parent.context)
            , parent
            , false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val date:String = items.get(position)////////////////////////////
        val num:String =(position+1).toString()//////////////////

        holder.tvPosition.text=num
        holder.tvItem.text=date

        if(position%2==0)
        {
            holder.llHistoryMainItem.setBackgroundColor(ContextCompat.getColor(holder.itemView.context ,R.color.LightGray))
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}