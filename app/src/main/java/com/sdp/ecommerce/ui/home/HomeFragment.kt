package com.sdp.ecommerce.ui.home

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sdp.ecommerce.R
import com.sdp.ecommerce.data.Product
import com.sdp.ecommerce.data.utils.PriceCategories
import com.sdp.ecommerce.data.utils.PriceCategoriesInt
import com.sdp.ecommerce.data.utils.ProductCategories
import com.sdp.ecommerce.data.utils.StoreDataStatus
import com.sdp.ecommerce.databinding.FragmentHomeBinding
import com.sdp.ecommerce.ui.MyOnFocusChangeListener
import com.sdp.ecommerce.ui.RecyclerViewPaddingItemDecoration
import com.sdp.ecommerce.viewModels.HomeViewModel
import kotlinx.coroutines.*


private const val TAG = "HomeFragment"

class HomeFragment : Fragment() {

	private lateinit var binding: FragmentHomeBinding
	private lateinit var viewModel: HomeViewModel
	private val focusChangeListener = MyOnFocusChangeListener()
	private lateinit var productAdapter: ProductAdapter

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		// Inflate the layout for this fragment
		binding = FragmentHomeBinding.inflate(layoutInflater)
		viewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
		setViews()
		setObservers()
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		viewModel.getUserLikes()
	}

//	override fun onResume() {
//		super.onResume()
//		viewModel.getLikedProducts()
//	}

	private fun setViews() {
		setHomeTopAppBar()
		if (context != null) {
			setProductsAdapter(viewModel.products.value)
			binding.productsRecyclerView.apply {
				val gridLayoutManager = GridLayoutManager(context, 2)
				gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
					override fun getSpanSize(position: Int): Int {
						return when (productAdapter.getItemViewType(position)) {
							2 -> 2 //ad
							else -> {
								val proCount = productAdapter.data.count { it is Product }
								val adCount = productAdapter.data.size - proCount
								val totalCount = proCount + (adCount * 2)
								// product, full for last item
								if (position + 1 == productAdapter.data.size && totalCount % 2 == 1) 2 else 1
							}
						}
					}
				}
				layoutManager = gridLayoutManager
				adapter = productAdapter
				val itemDecoration = RecyclerViewPaddingItemDecoration(requireContext())
				if (itemDecorationCount == 0) {
					addItemDecoration(itemDecoration)
				}
			}
		}

		if (!viewModel.isUserASeller) {
			binding.homeFabAddProduct.visibility = View.GONE
		}
		binding.homeFabAddProduct.setOnClickListener {
			showDialogWithItems(ProductCategories, 0, false)
		}
		binding.loaderLayout.loaderFrameLayout.visibility = View.VISIBLE
		binding.loaderLayout.circularLoader.showAnimationBehavior
	}

	private fun setObservers() {
		viewModel.storeDataStatus.observe(viewLifecycleOwner) { status ->
			when (status) {
				StoreDataStatus.LOADING -> {
					binding.loaderLayout.loaderFrameLayout.visibility = View.VISIBLE
					binding.loaderLayout.circularLoader.showAnimationBehavior
					binding.productsRecyclerView.visibility = View.GONE
				}
				else -> {
					binding.loaderLayout.circularLoader.hideAnimationBehavior
					binding.loaderLayout.loaderFrameLayout.visibility = View.GONE
				}
			}
			if (status != null && status != StoreDataStatus.LOADING) {
				viewModel.products.observe(viewLifecycleOwner) { productsList ->
					if (productsList.isNotEmpty()) {
						binding.loaderLayout.circularLoader.hideAnimationBehavior
						binding.loaderLayout.loaderFrameLayout.visibility = View.GONE
						binding.productsRecyclerView.visibility = View.VISIBLE
						binding.productsRecyclerView.adapter?.apply {
							productAdapter.data =
								getMixedDataList(productsList, getAdsList())
							notifyDataSetChanged()
						}
					}
				}
			}
		}
		viewModel.allProducts.observe(viewLifecycleOwner) {
			if (it.isNotEmpty()) {
				viewModel.setDataLoaded()
				viewModel.filterProducts("All")
			}
		}
		viewModel.userLikes.observe(viewLifecycleOwner) {
			if (it.isNotEmpty()) {
				binding.productsRecyclerView.adapter?.apply {
					notifyDataSetChanged()
				}
			}
		}
	}

	private fun performSearch(query: String) {
		viewModel.filterBySearch(query)
	}

	private fun setAppBarItemClicks(menuItem: MenuItem): Boolean {
		return when (menuItem.itemId) {
			R.id.home_filter -> {
				val extraFilters = arrayOf("All", "None")
				val categoryList = ProductCategories.plus(extraFilters)
				val checkedItem = categoryList.indexOf(viewModel.filterCategory.value)
				//showDialogWithItems(categoryList, checkedItem, true)
				showPriceFilterDialog()
				true
			}
			R.id.home_favorites -> {
				// show favorite products list
				findNavController().navigate(R.id.action_homeFragment_to_favoritesFragment)
				true
			}
			else -> false
		}
	}

	private fun showPriceFilterDialog() {

		viewModel.priceFilter = Pair(0, Int.MAX_VALUE)
		viewModel.catageroyFilter ="All"
		val window = PopupWindow(requireContext())

		val v = layoutInflater.inflate(R.layout.product_filter_layout,null,false)
		window.setContentView(v)
		window.apply {

			setBackgroundDrawable(AppCompatResources.getDrawable(requireContext(),R.drawable.plain_white))
			width=WindowManager.LayoutParams.MATCH_PARENT
			height=WindowManager.LayoutParams.WRAP_CONTENT
		}
		val priceChipGroup = v.findViewById<ChipGroup>(R.id.filter_chip_price)
		val catageroyChipGroup = v.findViewById<ChipGroup>(R.id.filter_chip_catageroy)
		val cancelButton = v.findViewById<Button>(R.id.filterCancel)
		val applyButton = v.findViewById<Button>(R.id.filterApply)

		cancelButton.setOnClickListener {
			window.dismiss()
		}
		applyButton.setOnClickListener {

			viewModel.applyFilter()
			window.dismiss()
		}

		catageroyChipGroup.apply {
			ProductCategories.forEach {
				val chip = Chip(requireContext())
				chip.text=it
				chip.isCheckable=true
				chip.setOnClickListener {
					Log.e(TAG, "showPriceFilterDialog: ${chip.text}",)
					viewModel.catageroyFilter = chip.text.toString()

				}
				this.addView(chip)
			}
		}
		priceChipGroup.apply {
			PriceCategories.forEach {
				val chip = Chip(requireContext())
				chip.text=it
				chip.isCheckable=true
				chip.setOnClickListener {
					Log.e(TAG, "showPriceFilterDialog: ${chip.text}",)
					val pair = PriceCategoriesInt.get(chip.text)
					pair?.let {
						viewModel.priceFilter = it
					}
					}
				this.addView(chip)
			}
			}

		//window.showAsDropDown(binding.root.findViewById(R.id.home_filter))
		window.showAtLocation(binding.root,Gravity.BOTTOM,0,0)
	}

	private fun setHomeTopAppBar() {
		var lastInput = ""
		val debounceJob: Job? = null
		val uiScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
		binding.homeTopAppBar.topAppBar.inflateMenu(R.menu.home_app_bar_menu)
		if (viewModel.isUserASeller) {
			binding.homeTopAppBar.topAppBar.menu.removeItem(R.id.home_favorites)
		}
		binding.homeTopAppBar.homeSearchEditText.onFocusChangeListener = focusChangeListener
		binding.homeTopAppBar.homeSearchEditText.doAfterTextChanged { editable ->
			if (editable != null) {
				val newtInput = editable.toString()
				debounceJob?.cancel()
				if (lastInput != newtInput) {
					lastInput = newtInput
					uiScope.launch {
						delay(500)
						if (lastInput == newtInput) {
							performSearch(newtInput)
						}
					}
				}
			}
		}
		//search bar
		binding.homeTopAppBar.homeSearchEditText.setOnEditorActionListener { textView, actionId, _ ->
			// if search clicked hide keyboard and do search
			if (actionId == EditorInfo.IME_ACTION_SEARCH) {
				textView.clearFocus()
				val inputManager =
					requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
				inputManager.hideSoftInputFromWindow(textView.windowToken, 0)
				performSearch(textView.text.toString())
				true
			} else {
				false
			}
		}

		// close button click - hide keyboard
		binding.homeTopAppBar.searchOutlinedTextLayout.setEndIconOnClickListener {
			it.clearFocus()
			binding.homeTopAppBar.homeSearchEditText.setText("")
			val inputManager =
				requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
			inputManager.hideSoftInputFromWindow(it.windowToken, 0)
//			viewModel.filterProducts("All")
		}
		binding.homeTopAppBar.topAppBar.setOnMenuItemClickListener { menuItem ->
			setAppBarItemClicks(menuItem)
		}
	}

	private fun setProductsAdapter(productsList: List<Product>?) {
		val likesList = viewModel.userLikes.value ?: emptyList()
		productAdapter = ProductAdapter(productsList ?: emptyList(), likesList, requireContext())
		productAdapter.onClickListener = object : ProductAdapter.OnClickListener {
			override fun onClick(productData: Product) {
				Log.e(TAG, "onClick: navigate to detail fragment ${productData.name}", )
				findNavController().navigate(
					R.id.action_seeProduct,
					bundleOf("productId" to productData.productId)
				)
				//findNavController().navigate()

			}

			override fun onDeleteClick(productData: Product) {
				Log.e(TAG, "onDeleteProduct: initiated for ${productData.productId}")
				showDeleteDialog(productData.name, productData.productId)
			}

			override fun onEditClick(productId: String) {
				Log.e(TAG, "onEditProduct: initiated for $productId")
				navigateToAddEditProductFragment(isEdit = true, productId = productId)
			}

			override fun onLikeClick(productId: String) {
				Log.e(TAG, "onToggleLike: initiated for $productId")
				viewModel.toggleLikeByProductId(productId)
			}

			override fun onAddToCartClick(productData: Product) {
				Log.e(TAG, "onToggleCartAddition: initiated")
				viewModel.toggleProductInCart(productData)
			}
		}
		productAdapter.bindImageButtons = object : ProductAdapter.BindImageButtons {
			@SuppressLint("ResourceAsColor")
			override fun setLikeButton(productId: String, button: CheckBox) {
				button.isChecked = viewModel.isProductLiked(productId)
			}

			override fun setCartButton(productId: String, imgView: ImageView) {
				if (viewModel.isProductInCart(productId)) {
					imgView.setImageResource(R.drawable.ic_remove_shopping_cart_24)
				} else {
					imgView.setImageResource(R.drawable.ic_add_shopping_cart_24)
				}
			}

		}
	}

	private fun showDeleteDialog(productName: String, productId: String) {
		context?.let {
			MaterialAlertDialogBuilder(it)
				.setTitle(getString(R.string.delete_dialog_title_text))
				.setMessage(getString(R.string.delete_dialog_message_text, productName))
				.setNegativeButton(getString(R.string.pro_cat_dialog_cancel_btn)) { dialog, _ ->
					dialog.cancel()
				}
				.setPositiveButton(getString(R.string.delete_dialog_delete_btn_text)) { dialog, _ ->
					viewModel.deleteProduct(productId)
					dialog.cancel()
				}
				.show()
		}
	}

	private fun showDialogWithItems(
		categoryItems: Array<String>,
		checkedOption: Int = 0,
		isFilter: Boolean
	) {
		var checkedItem = checkedOption
		context?.let {
			MaterialAlertDialogBuilder(it)
				.setTitle(getString(R.string.pro_cat_dialog_title))
				.setSingleChoiceItems(categoryItems, checkedItem) { _, which ->
					checkedItem = which
				}
				.setNegativeButton(getString(R.string.pro_cat_dialog_cancel_btn)) { dialog, _ ->
					dialog.cancel()
				}
				.setPositiveButton(getString(R.string.pro_cat_dialog_ok_btn)) { dialog, _ ->
					if (checkedItem == -1) {
						dialog.cancel()
					} else {
						if (isFilter) {
							viewModel.filterProducts(categoryItems[checkedItem])
						} else {
							navigateToAddEditProductFragment(
								isEdit = false,
								catName = categoryItems[checkedItem]
							)
						}
					}
					dialog.cancel()
				}
				.show()
		}
	}

	private fun navigateToAddEditProductFragment(
		isEdit: Boolean,
		catName: String? = null,
		productId: String? = null
	) {
		findNavController().navigate(
			R.id.action_goto_addProduct,
			bundleOf("isEdit" to isEdit, "categoryName" to catName, "productId" to productId)
		)
	}

	private fun getMixedDataList(data: List<Product>, adsList: List<Int>): List<Any> {
		val itemsList = mutableListOf<Any>()
		//itemsList.addAll(data.sortedBy { it.productId })
		itemsList.addAll(data)
		var currPos = 0
		if (itemsList.size >= 4) {
			adsList.forEach label@{ ad ->
				if (itemsList.size > currPos + 1) {
					itemsList.add(currPos, ad)
				} else {
					return@label
				}
				currPos += 5
			}
		}
		return itemsList
	}

	private fun getAdsList(): List<Int> {
		return listOf(R.drawable.ad_ex_2, R.drawable.ad_ex_1, R.drawable.ad_ex_3)
	}
}