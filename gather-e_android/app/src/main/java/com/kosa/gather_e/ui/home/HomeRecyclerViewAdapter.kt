package com.kosa.gather_e.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kosa.gather_e.databinding.HomeCardBinding
import com.kosa.gather_e.model.entity.gather.GatherEntity
import com.kosa.gather_e.model.entity.gather.GatherInfo

class HomeRecyclerViewAdapter: RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder>() {

    var dataList = listOf<GatherEntity>()

    inner class ViewHolder(private val binding: HomeCardBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(gather: GatherEntity) {
            binding.cardCnt.text = gather.gatherUserCnt.toString().plus("/").plus(gather.gatherLimit.toString())
            binding.cardStatus.text = if (gather.gatherUserCnt!! < gather.gatherLimit) {"모집중"}else{"모집 종료"}
            binding.cardTitle.text = gather.gatherTitle
            binding.writer.text = gather.creatorName
            binding.itemTime.text = gather.gatherDate
            binding.cardPlace.text = gather.gatherLocationName
            Glide.with(binding.root).load(gather.creatorImgUrl).into(binding.writerImg)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HomeCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position])
    }
}