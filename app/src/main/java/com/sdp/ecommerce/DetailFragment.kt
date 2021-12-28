package com.sdp.ecommerce

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.core.view.setMargins
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.PagerSnapHelper
import com.sdp.ecommerce.databinding.FragmentDetailBinding


class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private lateinit var  repo :ProductRepository
    private lateinit var mProductList: MutableList<Product>
    private var productData : Product = Product()
    private var isSeller =false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDetailBinding.inflate(layoutInflater)
        repo = ProductRepository(requireContext())
        mProductList = repo.list
        productData.name =
            "Tizum HDMI to VGA Adapter Cable 1080P for Projector, Computer, Laptop, TV, Projectors & TV"
        productData.mrp = 500.0
        productData.price = 259.0
        productData.description =
            "COMPACT DESIGN- The compact-made portable HDMI to VGA adapter connects a computer, desktop, laptop, or other devices with HDMI port to a monitor, projector, HDTV, or other devices with VGA port. Tuck this lightweight gadget into your bag or pocket to do a business presentation with your laptop and projector, or extend your desktop screen to a monitor or TV; A VGA cable is required. Does not Support Audio. SUPERIOR STABILITY- Built-in advanced Certified AG6200 IC chip converts HDMI digital signal to VGA analog signal it is NOT a bi-directional converter and cannot transmit signals from VGA to HDMI. INCREDIBLE PERFORMANCE- The HDMI male to VGA female converter supports resolutions up to 1920x1080 60Hz (1080p Full HD) including 720p, 1600x1200, 1280x1024 for high definition monitors. COMPATIBLE with computer, pc, desktop, laptop, or other devices with HDMI port Comes with 1 Year Warranty. For Support call + 91-7718-841-111 (Mon to Friday 9:30 am to 5:30pm)"
        productData.availableColors = listOf("Blue", "Black")
        productData.images = arrayListOf(
            "https://m.media-amazon.com/images/I/61F4szMc-gL._SL1500_.jpg",
            "https://m.media-amazon.com/images/I/71oBJ-DYRUL._SL1500_.jpg",
            "https://m.media-amazon.com/images/I/71eRNg6id9L._SL1500_.jpg"
        )
        productData.rating = 4.2
        productData.availableSizes = arrayListOf("4-64 GB", "6-64 GB", "6-128 GB")


        if (isSeller) {
            binding.proDetailsAddCartBtn.visibility = View.GONE
        } else {
            binding.proDetailsAddCartBtn.visibility = View.VISIBLE
            binding.proDetailsAddCartBtn.setOnClickListener {


                binding.layoutViewsGroup.visibility = View.GONE
                binding.proDetailsAddCartBtn.visibility = View.GONE
            }
        }
                setViews()

                return binding.root;
    }


    fun setViews() {
                binding.layoutViewsGroup.visibility = View.VISIBLE
                binding.proDetailsAddCartBtn.visibility = View.VISIBLE
                binding.addProAppBar.topAppBar.title = productData.name
                binding.addProAppBar.topAppBar.setNavigationOnClickListener {
                    requireActivity().findNavController(R.id.fragmentContainerView).navigate(R.id.action_detailFragment_to_homeFragment)
                }
                // binding.addProAppBar.topAppBar.inflateMenu(R.menu.app_bar_menu)
                binding.addProAppBar.topAppBar.overflowIcon?.setTint(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.gray
                    )
                )

                setImagesView()
                binding.proDetailsTitleTv.text = productData.name ?: ""
                binding.proDetailsLikeBtn.apply {
                }
                binding.proDetailsRatingBar.rating = (productData.rating ?: 0.0).toFloat()
                binding.proDetailsPriceTv.text = resources.getString(
                    R.string.pro_details_price_value,
                    productData.price.toString()
                )
                setShoeSizeButtons()
                setShoeColorsButtons()
                binding.proDetailsSpecificsText.text = productData.description ?: ""
            }

    fun setImagesView() {
                binding.proDetailsImagesRecyclerview.isNestedScrollingEnabled = false
                val adapter = ProductImagesAdapter(
                    requireContext(),
                    productData.images ?: emptyList()
                )
                binding.proDetailsImagesRecyclerview.adapter = adapter
                val rad = resources.getDimension(R.dimen.radius)
                val dotsHeight = resources.getDimensionPixelSize(R.dimen.dots_height)
                val inactiveColor = ContextCompat.getColor(requireContext(), R.color.gray)
                val activeColor =
                    ContextCompat.getColor(requireContext(), R.color.blue_accent_300)
                val itemDecoration =
                    DotsIndicatorDecoration(rad, rad * 4, dotsHeight, inactiveColor, activeColor)
                binding.proDetailsImagesRecyclerview.addItemDecoration(itemDecoration)
                PagerSnapHelper().attachToRecyclerView(binding.proDetailsImagesRecyclerview)

            }

    fun setShoeColorsButtons() {
                binding.proDetailsColorsRadioGroup.apply {


                    var ind = 1
                    val ShoeColors = mapOf(
                        "Black" to "#000000",
                        "White" to "#FFFFFF",
                        "red" to "#FF0000",
                        "green" to "#00FF00",
                        "Blue" to "#0000FF",
                        "yellow" to "#FFFF00",
                        "cyan" to "#00FFFF",
                        "magenta" to "#FF00FF"
                    )

                    for ((k, v) in ShoeColors) {
                        if (productData.availableColors.contains(k) == true) {
                            val radioButton = RadioButton(context)
                            radioButton.id = ind
                            radioButton.tag = k
                            val param =
                                binding.proDetailsColorsRadioGroup.layoutParams as ViewGroup.MarginLayoutParams
                            param.setMargins(resources.getDimensionPixelSize(R.dimen.radio_margin_size))
                            param.width = ViewGroup.LayoutParams.WRAP_CONTENT
                            param.height = ViewGroup.LayoutParams.WRAP_CONTENT
                            radioButton.layoutParams = param
                            radioButton.background =
                                ContextCompat.getDrawable(context, R.drawable.color_radio_selector)
                            radioButton.setButtonDrawable(R.color.transparent)
                            radioButton.backgroundTintList =
                                ColorStateList.valueOf(Color.parseColor(v))
                            if (k == "white") {
                                radioButton.backgroundTintMode = PorterDuff.Mode.MULTIPLY
                            } else {
                                radioButton.backgroundTintMode = PorterDuff.Mode.ADD
                            }
                            radioButton.setOnCheckedChangeListener { buttonView, isChecked ->
                                val tag = buttonView.tag.toString()
                                if (isChecked) {
                                    // selectedColor = tag
                                }
                            }
                            addView(radioButton)
                            ind++
                        }
                    }
                    invalidate()
                }
            }

    fun setShoeSizeButtons() {


                val ShoeSizes = mapOf(
                    "4-64 GB" to 4,
                    "6-64 GB" to 5,
                    "6-128 GB" to 6,
                    "UK7" to 7,
                    "UK8" to 8,
                    "UK9" to 9,
                    "UK10" to 10,
                    "UK11" to 11,
                    "UK12" to 12
                )

                binding.proDetailLinearLayout.apply {

                    removeAllViews()

                    for ((v, _) in ShoeSizes) {
                        if (productData.availableSizes.contains(v) == true) {

                            //cardView.setCardBackgroundColor(Color.LTGRAY)
                            val textView = Button(requireContext())
                            textView.id = ShoeSizes.get(v)!!
                            textView.text = v
                            val param =
                                binding.proDetailLinearLayout.layoutParams as ViewGroup.MarginLayoutParams
                            param.setMargins(resources.getDimensionPixelSize(R.dimen.radio_margin_size))
                            param.width = ViewGroup.LayoutParams.WRAP_CONTENT
                            param.height = ViewGroup.LayoutParams.WRAP_CONTENT
                            textView.layoutParams = param
                            // textView.background = ContextCompat.getDrawable(context, R.drawable.radio_selector)
                            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F)
                            textView.setTextColor(Color.BLACK)
                            textView.setTypeface(null, Typeface.BOLD)
                            textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
//                    textView.setOnClickListener { buttonView, isChecked ->
//                        val tag = buttonView.tag.toString().toInt()
//                        if (isChecked) {
//                            //selectedSize = tag
//                        }
//                    }
                            textView.setOnClickListener {
                                it.setBackgroundColor(Color.WHITE)
                            }

                            addView(textView)
                        }
                    }
                    invalidate()
                }
            }


        }
