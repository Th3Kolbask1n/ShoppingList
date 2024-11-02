package com.alexp.shoppinglist.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.alexp.shopinglist.domain.ShopItem
import com.google.android.material.textfield.TextInputLayout
import com.alexp.shoppinglist.R

class ShopItemFragment : Fragment(){

    private lateinit var viewModel: ShopItemViewModel

    private lateinit var tilName : TextInputLayout
    private lateinit var tilCount : TextInputLayout
    private lateinit var etName: EditText
    private lateinit var etCount: EditText
    private lateinit var buttonSave: Button
    private var screenMode = MODE_UNKNOW
    private var shopShopItemId = ShopItem.UNDEFINED_ID

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_shop_item, container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        initViews(view)
        addTextChangeListeners();
        launchRightMode()
        observeViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()

    }

    private fun observeViewModel()
    {
        viewModel.errorInputCount.observe(viewLifecycleOwner){
            val message = if(it)
            {
                getString(R.string.error_input_count)
            }
            else
            {
                null
            }
            tilCount.error = message
        }

        viewModel.errorInputName.observe(viewLifecycleOwner){
            val message = if(it)
            {
                getString(R.string.error_input_name)
            }
            else
            {
                null
            }
            tilName.error = message
        }
        viewModel.isCanCloseScreen.observe(viewLifecycleOwner)
        {
            activity?.onBackPressed()
        }
    }
    private fun launchRightMode()
    {
        when (screenMode) {
            MODE_EDIT -> launchEditMode()
            MODE_ADD  -> launchAddMode()
        }

    }
    private fun addTextChangeListeners()
    {
        etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(s: Editable?) {
            }


        })
        etCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(s: Editable?) {
            }


        })
    }
    private fun launchEditMode()
    {
        viewModel.getShopItem(shopShopItemId)
        viewModel.shopItem.observe(viewLifecycleOwner){
            etName.setText(it.name)
            etCount.setText(it.count.toString())
        }
        buttonSave.setOnClickListener {
            viewModel.editShopItem(etName.text?.toString(), etCount.text?.toString())
        }
    }

    private fun launchAddMode()
    {
        buttonSave.setOnClickListener {
            viewModel.addShopItem(etName.text?.toString(), etCount.text?.toString())
        }
    }
    private fun  parseParams()
    {
        val args = requireArguments()
        if(!args.containsKey(EXTRA_SCREEN_MODE))
        {
            throw RuntimeException("Param screen mode absent")


        }
        val mode = args.getString(EXTRA_SCREEN_MODE)

        if(mode!= MODE_EDIT && mode!= MODE_ADD)
            throw RuntimeException("Unknown screen mode $mode")

        screenMode = mode

        if(screenMode == MODE_EDIT)
        {
            if(!args.containsKey(EXTRA_SHOP_ITEM_ID))
                throw RuntimeException("Param shop item id absent")

            shopShopItemId = args.getInt(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }

    }
    private fun initViews(view: View)
    {
        tilName = view.findViewById(R.id.til_name)
        tilCount = view.findViewById(R.id.til_count)
        etName = view.findViewById(R.id.et_name)
        etCount = view.findViewById(R.id.et_count)
        buttonSave = view.findViewById(R.id.save_button)


    }
    companion object {

        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"

        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOW = ""

        fun newIntenAddItem(): ShopItemFragment {

            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_SCREEN_MODE, MODE_ADD)
                }
            }
        }
        fun newIntenEditItem(shopItemId: Int): ShopItemFragment
        {

            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_SCREEN_MODE, MODE_EDIT)
                    putInt(EXTRA_SHOP_ITEM_ID,shopItemId)
                }
            }
        }
    }
}