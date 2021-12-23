package com.sdp.ecommerce

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ProductDetailView : AppCompatActivity() {
    private lateinit var binding: FragmentProductDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_product_details)

        if(intent!=null)
        {


        }
    }
}