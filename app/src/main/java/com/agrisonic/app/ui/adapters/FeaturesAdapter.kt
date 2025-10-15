package com.agrisonic.app.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.agrisonic.app.databinding.ItemFeatureBinding

class FeaturesAdapter(
    private val features: List<FeatureItem>,
    private val onFeatureClick: (FeatureItem) -> Unit
) : RecyclerView.Adapter<FeaturesAdapter.FeatureViewHolder>() {

    inner class FeatureViewHolder(private val binding: ItemFeatureBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(feature: FeatureItem) {
            binding.tvFeatureTitle.text = feature.title
            binding.ivFeatureIcon.setImageResource(feature.iconRes)

            binding.root.setOnClickListener {
                onFeatureClick(feature)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureViewHolder {
        val binding = ItemFeatureBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FeatureViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FeatureViewHolder, position: Int) {
        holder.bind(features[position])
    }

    override fun getItemCount(): Int = features.size
}
