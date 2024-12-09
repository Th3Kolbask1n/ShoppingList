package com.alexp.shoppinglist.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexp.shopinglist.domain.AddShopItemUseCase
import com.alexp.shopinglist.domain.EditShopItemUseCase
import com.alexp.shopinglist.domain.GetShopItemUseCase
import com.alexp.shopinglist.domain.ShopItem
import com.alexp.shopinglist.domain.ShopListRepository
import com.alexp.shoppinglist.data.ShopListRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShopItemViewModel @Inject constructor(
    private val getShopItemUserCase: GetShopItemUseCase,
    private val addShopItemUseCase: AddShopItemUseCase,
    private val editShopItemUseCase: EditShopItemUseCase

) : ViewModel() {

    private val _errorInputName = MutableLiveData<Boolean>()

    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    private val _errorInputCount = MutableLiveData<Boolean>()

    val errorInputCount: LiveData<Boolean>
        get() = _errorInputName

    private val _shopItem = MutableLiveData<ShopItem>()

    val shopItem: LiveData<ShopItem>
        get() = _shopItem

    fun getShopItem(shopItemId: Int) {
        viewModelScope.launch {
            val item = getShopItemUserCase.getShopItem(shopItemId)
            _shopItem.value = item
        }

    }

    private val _isCanCloseScreen = MutableLiveData<Unit>()
    val isCanCloseScreen: LiveData<Unit>
        get() = _isCanCloseScreen

    fun addShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldValid = validateInput(name, count)
        if (fieldValid) {
            viewModelScope.launch {

                val shopItem = ShopItem(name, count, true)
                addShopItemUseCase.addShopItem(shopItem)
                finishWork()
            }
        }

    }

    fun editShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldValid = validateInput(name, count)
        if (fieldValid) {
            viewModelScope.launch {

                _shopItem.value?.let {
                    val item = it.copy(name = name, count = count)
                    editShopItemUseCase.editShopItem(item)
                    finishWork()

                }


            }
        }

    }

    private fun parseName(inputName: String?): String {
        return inputName?.trim() ?: ""
    }

    private fun parseCount(inputCount: String?): Int {
        return try {
            inputCount?.trim()?.toInt() ?: 0
        } catch (e: Exception) {
            return 0
        }
    }

    private fun validateInput(name: String, count: Int): Boolean {
        var result = true
        if (name.isBlank()) {
            _errorInputName.value = true
            result = false
        }
        if (count <= 0) {
            _errorInputCount.value = true
            result = false
        }
        return result
    }

    public fun resetErrorInputName() {
        _errorInputName.value = false
    }

    public fun resetErrorInputCount() {
        _errorInputName.value = false
    }

    private fun finishWork() {
        _isCanCloseScreen.value = Unit
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}