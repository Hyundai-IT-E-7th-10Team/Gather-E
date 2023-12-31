package com.kosa.gather_e.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kosa.gather_e.databinding.FragmentHomeBinding
import com.kosa.gather_e.databinding.HomeCardBinding
import com.kosa.gather_e.model.entity.gather.GatherEntity
import com.kosa.gather_e.util.GatherUtil

class HomeRecyclerViewAdapter: RecyclerView.Adapter<HomeRecyclerViewAdapter.ViewHolder>() {

    var dataList = listOf<GatherEntity>()

    inner class ViewHolder(private val binding: HomeCardBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(gather: GatherEntity) {
            binding.cardCnt.text = gather.gatherUserCnt.toString().plus("/").plus(gather.gatherLimit.toString())
            binding.cardStatus.text = if (GatherUtil.isGathering(gather) && !GatherUtil.isFull(gather)) {"모집중"}else{"모집 종료"}
            binding.cardTitle.text = gather.gatherTitle
            binding.writer.text = gather.creatorName
            binding.itemTime.text = gather.gatherDate
            binding.cardPlace.text = gather.gatherLocationName
            binding.cardCategory.text = gather.categoryName
            Glide.with(binding.root).load(gather.creatorImgUrl).into(binding.writerImg)
            itemView.setOnClickListener{
                Intent(it.context, PostDetailActivity::class.java).apply {
                    putExtra("gather", gather)
                }.run { it.context.startActivity(this) }
            }
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