package com.alexp.shoppinglist.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.alexp.shoppinglist.R
import com.alexp.shoppinglist.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import javax.inject.Inject

class MainActivity : AppCompatActivity(),ShopItemFragment.OnEditingFinishedListener {
    private lateinit var viewModel: MainViewModel
    private lateinit var shopListAdapter: ShopListAdapter
    private lateinit var binding : ActivityMainBinding


    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val component by lazy {
        (application as ShopApplication).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        component.inject(this)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)


        setupRecyclerView()

        viewModel = ViewModelProvider(this,viewModelFactory)[MainViewModel::class.java]

        viewModel.shopList.observe(this) {

            shopListAdapter.submitList(it)

        }

        binding.buttonAddShopItem.setOnClickListener {
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
        return binding.shopItemContainer == null
    }
    private fun launchFragment(fragment: Fragment)
    {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction().add(R.id.shop_item_container,fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setupRecyclerView() {


        with(binding.rvShopList) {
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

        setSwipeListener(binding.rvShopList)
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

    override fun onEditingFinish() {
        Toast.makeText(this@MainActivity,"Success",Toast.LENGTH_SHORT).show()
        supportFragmentManager.popBackStack()
    }

    private fun setupLongClickListener() {


        shopListAdapter.onShopItemLongClickListener = { viewModel.changeEnableState(it) }
    }
}