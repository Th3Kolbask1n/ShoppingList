package com.alexp.shoppinglist.di

import android.app.Application
import android.app.Fragment
import android.app.NativeActivity
import com.alexp.shoppinglist.presentation.MainActivity
import com.alexp.shoppinglist.presentation.ShopItemFragment
import dagger.BindsInstance
import dagger.Component
import dagger.Component.Factory

@ApplicationScope
@Component(
    modules = [
        DataModule::class,
        ViewModelModule::class
    ]
)
interface ApplicationComponent {

    fun inject(activity: MainActivity)
    fun inject(fragment: ShopItemFragment)

    @Component.Factory

    interface Factory {

        fun create(
            @BindsInstance application: Application

        ): ApplicationComponent
    }
}