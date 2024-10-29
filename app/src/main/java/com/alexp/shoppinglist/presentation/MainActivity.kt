package com.alexp.shoppinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.alexp.shopinglist.domain.ShopItem
import com.alexp.shoppinglist.R

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var shopListAdapter : ShopListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecyclerView()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.shopList.observe(this){

            shopListAdapter.submitList(it)

        }


    }

    private fun setupRecyclerView()
    {
        val rvShopList= findViewById<RecyclerView>(R.id.rv_shop_list)


        with(rvShopList){
            shopListAdapter = ShopListAdapter()
            adapter = shopListAdapter
            recycledViewPool.setMaxRecycledViews(ShopListAdapter.VIEW_TYPE_ENABLED,ShopListAdapter.MAX_POOL_SIZE)
            recycledViewPool.setMaxRecycledViews(ShopListAdapter.VIEW_TYPE_DISABLED,ShopListAdapter.MAX_POOL_SIZE)

        }
        setupLongClickListener()
        setupClickListener()

        setSwipeListener(rvShopList)
    }

    private fun setSwipeListener(rvShopList: RecyclerView?) {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = shopListAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteShopItem(item)
            }

        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvShopList)
        //        shopListAdapter.onShopItemLongClickListener = object : ShopListAdapter.OnShopItemLongClickListener{
        //            override fun onShopItemLongClick(shopItem: ShopItem) {
        //                viewModel.changeEnableState(shopItem)
        //            }
        //        }
    }

    private fun setupClickListener() {
        shopListAdapter.onShopItemClickListener = {
            Log.d("Main", "it")
        }
    }

    private fun setupLongClickListener() {
        shopListAdapter.onShopItemLongClickListener = { viewModel.changeEnableState(it) }
    }
}