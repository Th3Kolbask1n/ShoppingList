package com.alexp.shoppinglist.presentation

import android.app.Application
import com.alexp.shoppinglist.di.DaggerApplicationComponent

class ShopApplication :Application (){

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }

}