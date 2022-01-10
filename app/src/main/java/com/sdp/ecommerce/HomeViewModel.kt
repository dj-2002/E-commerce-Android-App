package com.sdp.ecommerce

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.sdp.ecommerce.models.Product
import com.sdp.ecommerce.repository.ProductRepository

private const val TAG = "HomeViewModel"
class HomeViewModel(context: Context) :ViewModel() {

    val repository = ProductRepository(context)
    var product : Product? = null

    fun getProductList(): LiveData<MutableList<Product>> {
        return repository.getListOfProduct()
    }



}