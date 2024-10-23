package com.alexp.shoppinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alexp.shopinglist.domain.ShopItem
import com.alexp.shoppinglist.R

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.shopList.observe(this, object:Observer<List<ShopItem>>
        {
            override fun onChanged(value: List<ShopItem>) {
                TODO("Not yet implemented")
            }

        })
        viewModel.shopList.observe(this){

            Log.d("MainActivity2", it.toString())
        }
        viewModel.getShopList()
    }
}