package com.alexp.shoppinglist.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.alexp.shoppinglist.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var shopListAdapter: ShopListAdapter
    private var shopItemContainer : FragmentContainerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        shopItemContainer = findViewById(R.id.shop_item_container)
        setupRecyclerView()

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.shopList.observe(this) {

            shopListAdapter.submitList(it)

        }
        val buttonAddItem = findViewById<FloatingActionButton>(R.id.button_add_shop_item)

        buttonAddItem.setOnClickListener {
            if(isOnsePaneMode()) {
                val intent = ShopItemActivity.newIntenAddItem(this)
                startActivity(intent)
            }
            else
            {
                launchFragment(ShopItemFragment.newIntenAddItem())
            }
        }

    }

    private fun isOnsePaneMode() : Boolean
    {
        return shopItemContainer == null
    }
    private fun launchFragment(fragment: Fragment)
    {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction().add(R.id.shop_item_container,fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setupRecyclerView() {
        val rvShopList = findViewById<RecyclerView>(R.id.rv_shop_list)


        with(rvShopList) {
            shopListAdapter = ShopListAdapter()
            adapter = shopListAdapter
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_ENABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_DISABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )

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
            if(isOnsePaneMode()) {
                val intent = ShopItemActivity.newIntenEditItem(this,it.id)
                startActivity(intent)
            }
            else
            {
                launchFragment(ShopItemFragment.newIntenEditItem(it.id))
            }
        }
    }

    private fun setupLongClickListener() {


        shopListAdapter.onShopItemLongClickListener = { viewModel.changeEnableState(it) }
    }
}