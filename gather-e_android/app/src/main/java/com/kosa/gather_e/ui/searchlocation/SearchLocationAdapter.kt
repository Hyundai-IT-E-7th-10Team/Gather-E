package com.kosa.gather_e.ui.searchlocation

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import com.kosa.gather_e.data.model.SearchLocationEntity
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
            binding.placeName.text = location.place_name
            binding.roadAddressName.text = location.road_address_name
            binding.x.text = location.x
            binding.y.text = location.y
            binding.distance.text = location.distance

            binding.root.setOnClickListener {
                listener.onLocationSelected(location)
            }

        }
    }
}
