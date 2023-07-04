package com.kosa.gather_e.ui.searchlocation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kosa.gather_e.model.entity.location.SearchLocationEntity
import com.kosa.gather_e.databinding.ItemLocationListBinding

interface OnLocationSelectedListener {
    fun onLocationSelected(location: SearchLocationEntity)
}

class SearchLocationAdapter(
    private val locationList: List<SearchLocationEntity>,
    private val listener: OnLocationSelectedListener
    ): RecyclerView.Adapter<SearchLocationAdapter.SearchLocationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchLocationViewHolder {
        val binding = ItemLocationListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchLocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchLocationViewHolder, position: Int) {
        val location = locationList[position]
        holder.bind(location)
    }

    override fun getItemCount(): Int {
        return locationList.size
    }

    inner class SearchLocationViewHolder(private val binding: ItemLocationListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(location: SearchLocationEntity) {
            binding.categoryName.text = location.category_name
            binding.placeName.text = location.place_name
            binding.roadAddressName.text = location.road_address_name

            binding.root.setOnClickListener {
                listener.onLocationSelected(location)
            }

        }
    }
}
