package com.sdp.ecommerce

import android.app.Application
import androidx.lifecycle.*
import com.sdp.ecommerce.models.Product
import com.sdp.ecommerce.repository.ProductRepository

class HomeViewModel(application: Application,lifeCycleOwner : LifecycleOwner) : AndroidViewModel(application) {

    val repository = ProductRepository(application.applicationContext)
    var listOfProduct : MutableLiveData<MutableList<Product>> = MutableLiveData(arrayListOf())


    init {
        repository.getListOfProduct().observe(lifeCycleOwner,{
            listOfProduct.value = it
        })
    }

    fun getProductList(): LiveData<MutableList<Product>> {
        return listOfProduct
    }



}