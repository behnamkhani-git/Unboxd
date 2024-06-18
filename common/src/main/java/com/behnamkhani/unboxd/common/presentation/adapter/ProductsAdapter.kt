package com.behnamkhani.unboxd.common.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.behnamkhani.unboxd.common.databinding.RecyclerViewProductItemBinding
import com.behnamkhani.unboxd.common.presentation.model.entities.UIProduct
import com.behnamkhani.unboxd.common.utils.setImage

class ProductsAdapter(
    private val onProductTouch: (Long) -> Unit,
    private val onHeartTouch: (Long, Boolean) -> Unit,
) : ListAdapter<UIProduct, ProductsAdapter.ProductsViewHolder>(ITEM_COMPARATOR) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val binding = RecyclerViewProductItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val item: UIProduct = getItem(position)
        holder.bind(item, onProductTouch, onHeartTouch)
    }

    inner class ProductsViewHolder(
        private val binding: RecyclerViewProductItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: UIProduct,
            onProductTouch: (Long) -> Unit,
            onHeartTouch: (Long, Boolean) -> Unit,
        ) {
            binding.name.text = item.title
            binding.photo.setImage(item.images[0])
            binding.recyclerViewItem.setOnClickListener {
                onProductTouch(item.id)
            }
            binding.favorite.setOnClickListener {
                onHeartTouch(item.id, binding.favorite.isChecked)
            }

            binding.favorite.isChecked = item.isInWishlist
        }
    }
}

private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<UIProduct>() {
    override fun areItemsTheSame(oldItem: UIProduct, newItem: UIProduct): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UIProduct, newItem: UIProduct): Boolean {
        return oldItem == newItem
    }
}