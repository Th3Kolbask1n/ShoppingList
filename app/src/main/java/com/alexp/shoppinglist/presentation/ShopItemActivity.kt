package com.alexp.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.alexp.shopinglist.domain.ShopItem
import com.alexp.shoppinglist.R
import com.google.android.material.textfield.TextInputLayout

class ShopItemActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener {

    //    private lateinit var viewModel: ShopItemViewModel
//
//    private lateinit var tilName : TextInputLayout
//    private lateinit var tilCount : TextInputLayout
//    private lateinit var etName: EditText
//    private lateinit var etCount: EditText
//    private lateinit var buttonSave:Button
    private var screenMode = MODE_UNKNOW
    private var shopShopItemId = ShopItem.UNDEFINED_ID
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        parseIntent()
//        if(savedInstanceState==null)
//        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
//        initViews()
//        addTextChangeListeners();
        launchRightMode()
//        observeViewModel()
    }

    private fun observeViewModel()
    {
//        viewModel.errorInputCount.observe(this){
//            val message = if(it)
//            {
//                getString(R.string.error_input_count)
//            }
//            else
//            {
//                null
//            }
//            tilCount.error = message
//        }
//
//        viewModel.errorInputName.observe(this){
//            val message = if(it)
//            {
//                getString(R.string.error_input_name)
//            }
//            else
//            {
//                null
//            }
//            tilName.error = message
//        }
//        viewModel.isCanCloseScreen.observe(this)
//        {
//            finish()
//        }
    }
    private fun launchRightMode()
    {
        val fragment = when (screenMode) {
            MODE_EDIT -> ShopItemFragment.newIntenEditItem(shopShopItemId)
            MODE_ADD  -> ShopItemFragment.newIntenAddItem()
            else      -> throw RuntimeException("Unknown screen mode $screenMode")
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.shop_item_container, fragment)
            .commit()
    }
    private fun addTextChangeListeners()
    {
//        etName.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                viewModel.resetErrorInputName()
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//            }
//
//
//        })
//        etCount.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                viewModel.resetErrorInputName()
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//            }
//
//
//        })
    }
    private fun launchEditMode()
    {
//        viewModel.getShopItem(shopShopItemId)
//        viewModel.shopItem.observe(this){
//            etName.setText(it.name)
//            etCount.setText(it.count.toString())
//        }
//        buttonSave.setOnClickListener {
//            viewModel.editShopItem(etName.text?.toString(), etCount.text?.toString())
//        }
    }

    private fun launchAddMode()
    {
//        buttonSave.setOnClickListener {
//            viewModel.addShopItem(etName.text?.toString(), etCount.text?.toString())
//        }
    }
    private fun parseIntent()
    {
        if(!intent.hasExtra(EXTRA_SCREEN_MODE))
        {
            throw RuntimeException("Param screen mode absent")
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if(mode!= MODE_EDIT && mode!= MODE_ADD)
            throw RuntimeException("Unknown screen mode $mode")
        Log.d("EDit_mODe", mode)
        screenMode = mode
        if(screenMode == MODE_EDIT)
        {
            if(!intent.hasExtra(EXTRA_SHOP_ITEM_ID))
                throw RuntimeException("Param shop item id absent")

            shopShopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }

    }
    private fun initViews()
    {
//        tilName = findViewById(R.id.til_name)
//        tilCount = findViewById(R.id.til_count)
//        etName = findViewById(R.id.et_name)
//        etCount = findViewById(R.id.et_count)
//        buttonSave = findViewById(R.id.save_button)


    }
    override fun onEditingFinish() {
        finish()
    }

    companion object {

        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"

        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOW = ""

        fun newIntenAddItem(context: Context):Intent
        {
            val intent = Intent(context,ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }
        fun newIntenEditItem(context: Context, shopItemId: Int):Intent
        {
            val intent = Intent(context,ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID,shopItemId)
            return intent
        }
    }
}