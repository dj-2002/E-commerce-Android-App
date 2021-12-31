package com.sdp.ecommerce.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sdp.ecommerce.R
import com.sdp.ecommerce.models.Product

class ProductAdapter( var context: Context, var list:MutableList<Product>) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    lateinit var onClickListener: OnClickListener

    class ViewHolder(itemView : View,var context: Context) : RecyclerView.ViewHolder(itemView)
    {

        private  val proName:TextView
        private val proPrice:TextView
        private val proActualPrice:TextView
        private val productImage: ImageView
        private val proDeleteButton : ImageView
        private val proEditBtn : ImageView
        private val proOfferValue : TextView
        private val proRatingBar : RatingBar
        private val proLikeButton : CheckBox
        private val proAddToCartButton: ImageView
        val productCard:CardView

        init {
            proName = itemView.findViewById(R.id.product_name_tv)
            proPrice = itemView.findViewById(R.id.product_price_tv)
            proActualPrice = itemView.findViewById(R.id.product_actual_price_tv)
            productImage = itemView.findViewById(R.id.product_image_view)
            proDeleteButton = itemView.findViewById(R.id.product_delete_button)
            proEditBtn = itemView.findViewById(R.id.product_edit_button)
            proOfferValue = itemView.findViewById(R.id.product_offer_value_tv)
            proRatingBar = itemView.findViewById(R.id.product_rating_bar)
            proLikeButton = itemView.findViewById(R.id.product_like_checkbox)
            proAddToCartButton = itemView.findViewById(R.id.product_add_to_cart_button)
            productCard = itemView.findViewById(R.id.product_card)

        }

        fun setValue(product : Product){

            proName.setText(product.name)
            proPrice.setText(product.price.toString()+"$")
            proActualPrice.setText(product.mrp.toString()+"$")
            setImage(product)
            proDeleteButton.visibility = View.GONE
            proEditBtn.visibility = View.GONE
            proOfferValue.setText(calculateOfferPer(product))
            proRatingBar.rating = product.rating.toFloat()

        }
        fun calculateOfferPer(product: Product):String
        {
            val actualP = product.mrp
            val cp = product.price
            val perc = ((actualP - cp) / actualP * 100).toInt()
            return "$perc %OFF"
        }

        fun setImage(product: Product)
        {
            if(product.images.size > 0) {
                val imgUrl = product.images[0].toUri().buildUpon().scheme("https").build()
                Glide.with(context)
                    .asBitmap()
                    .load(imgUrl)
                    .into(productImage)
                productImage.clipToOutline = true
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.products_list_item, parent, false)
        return ViewHolder(view,context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val cItem = list[position]
        holder.setValue(cItem)

        holder.productCard.setOnClickListener {
            onClickListener.onClick(cItem)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnClickListener {
        fun onClick(productData: Product)
    }

}