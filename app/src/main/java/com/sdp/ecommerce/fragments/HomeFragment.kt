package com.sdp.ecommerce.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sdp.ecommerce.models.Product
import com.sdp.ecommerce.adapters.ProductAdapter
import com.sdp.ecommerce.repository.ProductRepository
import com.sdp.ecommerce.R


class HomeFragment : Fragment() {

    private lateinit var mProductList: MutableList<Product>
    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: ProductAdapter
    lateinit var mLayoutManager: RecyclerView.LayoutManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val repository = ProductRepository(requireContext())
        repository.getProductList()
        mRecyclerView = view.findViewById(R.id.products_recycler_view)
        mRecyclerView.setHasFixedSize(true)
        mLayoutManager = LinearLayoutManager(context)
        mProductList = repository.list
        mAdapter = ProductAdapter(requireContext(), mProductList)
        mRecyclerView.setLayoutManager(mLayoutManager)
        mRecyclerView.setAdapter(mAdapter)
        mAdapter.onClickListener = object : ProductAdapter.OnClickListener {
            override fun onClick(productData: Product) {
                view.findNavController().navigate(R.id.action_homeFragment_to_detailFragment)
            }
        }

        return view
    }

}