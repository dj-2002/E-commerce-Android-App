package com.sdp.ecommerce

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeActivity : AppCompatActivity() {
    private lateinit var mProductList: MutableList<Product>
    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: ProductAdapter
    lateinit var mLayoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val repository = ProductRepository(applicationContext)
        repository.getProductList()
        mRecyclerView = findViewById(R.id.products_recycler_view)
        mRecyclerView.setHasFixedSize(true)
        mLayoutManager = LinearLayoutManager(this)
        mProductList = repository.list
        mAdapter = ProductAdapter(applicationContext, mProductList)
        mRecyclerView.setLayoutManager(mLayoutManager)
        mRecyclerView.setAdapter(mAdapter)
        mAdapter.onClickListener = object : ProductAdapter.OnClickListener {
            override fun onClick(productData: Product) {

            }
        }
    }

}