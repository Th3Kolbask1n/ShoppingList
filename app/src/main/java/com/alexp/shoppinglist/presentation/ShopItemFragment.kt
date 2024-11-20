package com.alexp.shoppinglist.presentation

import android.content.Context
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
import com.alexp.shoppinglist.databinding.FragmentShopItemBinding

class ShopItemFragment : Fragment(){

    private lateinit var viewModel: ShopItemViewModel
    private lateinit var onEditingFinishedListener: OnEditingFinishedListener

    private var screenMode = MODE_UNKNOW
    private var shopShopItemId = ShopItem.UNDEFINED_ID
    private var _binding : FragmentShopItemBinding? = null
    private val bindng:FragmentShopItemBinding
        get() = _binding?: throw RuntimeException("FragmentShopItemBinding is null")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentShopItemBinding.inflate(inflater, container,false)
        return bindng.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        bindng.viewModel = viewModel
        bindng.lifecycleOwner = viewLifecycleOwner
        addTextChangeListeners();
        launchRightMode()
        observeViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if(context is OnEditingFinishedListener)
        {
            onEditingFinishedListener = context
        }
        else
        {
            throw RuntimeException("Activity must implement OnEditingFinishedListener")
        }

    }

    private fun observeViewModel()
    {

        viewModel.isCanCloseScreen.observe(viewLifecycleOwner)
        {
            onEditingFinishedListener?.onEditingFinish()
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
        bindng.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(s: Editable?) {
            }


        })
        bindng.etCount.addTextChangedListener(object : TextWatcher {
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

        bindng.saveButton.setOnClickListener {
            viewModel.editShopItem(bindng.etName.text?.toString(), bindng.etCount.text?.toString())
        }
    }

    private fun launchAddMode()
    {
        bindng.saveButton.setOnClickListener {
            viewModel.addShopItem(bindng.etName.text?.toString(), bindng.etCount.text?.toString())
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


    interface OnEditingFinishedListener {
        fun onEditingFinish()
    }
}