package com.sdp.ecommerce

import android.content.Context
import android.util.Log
import android.widget.Toast

import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.google.android.gms.tasks.OnFailureListener

import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


class ProductRepository(mcontext: Context) {
    var db: FirebaseFirestore
     var  context: Context = mcontext;
     var   list:MutableList<Product>  = arrayListOf()

     private  val TAG = "ProductRepository"

    init {
        db = FirebaseFirestore.getInstance();
    }

    fun addDataToFirestore(
        productName: String,
        productDescription: String,
    ) {

        val dbCourses: CollectionReference = db.collection("products")
        val product = Product(name = productName, description = productDescription)

        dbCourses.add(product)
            .addOnSuccessListener(OnSuccessListener<Any?> { // after the data addition is successful
                Toast.makeText(context,
                    "Your Course has been added to Firebase Firestore",
                    Toast.LENGTH_SHORT
                ).show()
            })
            .addOnFailureListener(OnFailureListener { e ->
                Toast.makeText(context, "Fail to add course \n$e", Toast.LENGTH_SHORT).show()
            })


         val productsDb = db.collection("products")
         productsDb.addSnapshotListener { value, error ->

             if(value!=null)
             Log.e(TAG, "addDataToFirestore: ${value.documents[0].toString()}", )

         }

    }

    fun changeData(productName: String, productDescription: String, )
    {
        val dbCourses: CollectionReference = db.collection("products")
        val product = Product(name = productName, description = productDescription)
        val toChange = dbCourses.whereEqualTo("name","samsung m31").get().addOnCompleteListener {
            val docId = it.result.documents[0].id
            dbCourses.document(docId).set(product)
        }
    }

     fun getProductList()
    {

            val db: CollectionReference = db.collection("products")
           val job=  db.get().addOnCompleteListener {
                var snapshot = it.result
                for (snapProduct in snapshot) {
                    val product = snapProduct.toObject(Product::class.java)
                    list.add(product)
                }
            }

        while(!job.isComplete)
        {
            runBlocking {
                delay(500)
            }

        }




    }

}