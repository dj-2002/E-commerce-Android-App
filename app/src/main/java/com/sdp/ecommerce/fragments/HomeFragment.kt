package com.sdp.ecommerce.fragments

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sdp.ecommerce.HomeViewModel
import com.sdp.ecommerce.HomeViewModelFactory
import com.sdp.ecommerce.models.Product
import com.sdp.ecommerce.adapters.ProductAdapter
import com.sdp.ecommerce.repository.ProductRepository
import com.sdp.ecommerce.R

private const val TAG = "HomeFragment"
class HomeFragment : Fragment() {

    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: ProductAdapter
    lateinit var mLayoutManager: RecyclerView.LayoutManager
    lateinit var  model: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        mRecyclerView = view.findViewById(R.id.products_recycler_view)
        mRecyclerView.setHasFixedSize(true)
        mLayoutManager = LinearLayoutManager(context)
        model = ViewModelProvider(requireActivity(), HomeViewModelFactory(requireContext())).get(
            HomeViewModel::class.java
        )

        mAdapter = ProductAdapter(requireContext(), arrayListOf())
        model.getProductList().observe(viewLifecycleOwner , {
            Log.e(TAG, "onCreateView: list of product observer in homeFragment : ${it.size} $it", )
            mAdapter.list = it
            mAdapter.notifyDataSetChanged()

        })


        mRecyclerView.setLayoutManager(mLayoutManager)
        mRecyclerView.setAdapter(mAdapter)
        mAdapter.onClickListener = object : ProductAdapter.OnClickListener {
            override fun onClick(productData: Product) {
                model.product = productData
                view.findNavController().navigate(R.id.action_homeFragment_to_detailFragment)
            }
        }

        return view
    }

}