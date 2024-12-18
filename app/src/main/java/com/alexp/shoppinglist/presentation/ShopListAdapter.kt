package com.alexp.shoppinglist.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import com.alexp.shopinglist.domain.ShopItem
import com.alexp.shoppinglist.R
import com.alexp.shoppinglist.databinding.ItemShopDisabledBinding
import com.alexp.shoppinglist.databinding.ItemShopEnabledBinding

class  ShopListAdapter : ListAdapter<ShopItem, ShopItemViewHolder>(ShopItemDiffCallback()) {


//    var shopList = listOf<ShopItem>()
//        set(value) {
//            field = value
//            notifyDataSetChanged()
//        }

    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {

        val layout = when (viewType) {
            VIEW_TYPE_DISABLED -> R.layout.item_shop_disabled
            VIEW_TYPE_ENABLED -> R.layout.item_shop_enabled
            else -> throw RuntimeException("Unknown view type")
        }
        val binding = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(parent.context),
            layout,
            parent,
            false)
        return ShopItemViewHolder(binding)
    }


    override fun onBindViewHolder(viewHolder: ShopItemViewHolder, position: Int) {
        val shopItem = getItem(position)
        val binding = viewHolder.binding

        binding.root.setOnLongClickListener() {
            onShopItemLongClickListener?.invoke(shopItem)
            true
        }
        binding.root.setOnClickListener {

            onShopItemClickListener?.invoke(shopItem)
        }
        when(binding){
            is ItemShopDisabledBinding ->
            {
                binding.shopItem  =shopItem
            }

            is ItemShopEnabledBinding ->
            {
                binding.shopItem  =shopItem

            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item.enabled) {
            VIEW_TYPE_ENABLED
        } else {
            VIEW_TYPE_DISABLED
        }

    }

    companion object {
        const val VIEW_TYPE_ENABLED = 0
        const val VIEW_TYPE_DISABLED = 1
        const val MAX_POOL_SIZE = 5
    }
}