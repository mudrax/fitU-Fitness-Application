package com.mudrax.fitu


import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mudrax.fitu.databinding.ItemExerciseStatusBinding

class ExerciseAdapter(val items:ArrayList<ExerciseModel>):RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {




    class ExerciseViewHolder(binding: ItemExerciseStatusBinding) :RecyclerView.ViewHolder(binding.root) {
        val tvItem = binding.tvItem
    }





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        return ExerciseViewHolder(ItemExerciseStatusBinding.inflate
            (LayoutInflater.from(parent.context),parent , false))
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val model = items[position] // now every model contains object of Exercise model
        holder.tvItem.text = model.getId().toString()

        when{
            model.getIsSelected()->{
                holder.tvItem.background = ContextCompat.getDrawable(holder.itemView.context , R.drawable.circular_item_selected_recyclerview_accent_color_background)
                holder.tvItem.setTextColor(Color.parseColor("#212121"))
            }
            model.getIsCurrent() -> {
                holder.tvItem.background = ContextCompat.getDrawable(holder.itemView.context , R.drawable.circular_item_accent_color_background)
                holder.tvItem.setTextColor(Color.parseColor("#FFFFFF"))
            }
            else -> {
                holder.tvItem.background = ContextCompat.getDrawable(holder.itemView.context ,
                    R.drawable.circular_item_completed_recyclerview_accent_color_background
                )
                holder.tvItem.setTextColor(Color.parseColor("#212121"))
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}