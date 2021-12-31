package com.sdp.ecommerce.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.google.android.gms.tasks.OnFailureListener

import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.sdp.ecommerce.constant.Constant
import com.sdp.ecommerce.models.Product
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.collections.HashMap

private const val TAG = "ProductRepository"
class ProductRepository(val context: Context) {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val listOfProduct : LiveData<MutableList<Product>> = MutableLiveData(arrayListOf())

    init {
        readProductList()
    }

    fun addNewProduct(product : Product){
        // Add a new document with a generated ID
        db.collection(Constant.DB_PRODUCT_COLLECTION)
            .add(product)
            .addOnSuccessListener { documentReference ->
                Log.e(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                Toast.makeText(context,
                    "Your product has been successfully added to Firestore",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error adding document", e)
                Toast.makeText(context, "Fail to add product \n$e", Toast.LENGTH_SHORT).show()
            }
    }

    fun addNewProduct(productHashMap : HashMap<String,Any>){
        // Add a new document with a generated ID
        db.collection(Constant.DB_PRODUCT_COLLECTION)
            .add(productHashMap)
            .addOnSuccessListener { documentReference ->
                Log.e(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                Toast.makeText(context,
                    "Your product has been successfully added to Firestore",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error adding document", e)
                Toast.makeText(context, "Fail to add product \n$e", Toast.LENGTH_SHORT).show()
            }
    }

    fun getListOfProduct(): LiveData<MutableList<Product>> {
        return listOfProduct
    }

    fun readProductList(){

        db.collection(Constant.DB_PRODUCT_COLLECTION)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.e(TAG, "${document.id} => ${document.data}")

                    val product = document.toObject(Product::class.java)
                    listOfProduct.value?.add(product)
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error getting documents.", exception)
            }
    }

    fun getProductFromLocalList(productId: String):Product?{
        if(listOfProduct.value==null) return null
        for(product in listOfProduct.value!!){
            if(product.productId == productId){
                return product
            }
        }

        return null
    }


    suspend fun getProduct(productDocumentId : String):Product?{

        val docRef = db.collection(Constant.DB_PRODUCT_COLLECTION).document(productDocumentId)
        var product : Product? = null
        val job = docRef.get()
            .addOnSuccessListener { document ->

                if (document != null) {
                    product= document.toObject(Product::class.java)
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }

        while(!job.isComplete){
            delay(500)
        }
        return product
    }

    fun updateProduct(productDocumentId: String,product: Product){

        val docRef = db.collection(Constant.DB_PRODUCT_COLLECTION).document(productDocumentId)

// Update the timestamp field with the value from the server
//        val updates = hashMapOf<String, Any>(
//            "timestamp" to FieldValue.serverTimestamp()
//        )

        val updates = product.getHashMap()

        docRef.update(updates).addOnSuccessListener {
            Log.e(TAG, "updateProduct: Updated successfully", )
            Toast.makeText(context, "Updated successfully", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Log.e(TAG, "updateProduct: error while updating!!", )
            Toast.makeText(context, "Error while updating!!", Toast.LENGTH_SHORT).show()
        }
    }

    fun updateProduct(productDocumentId: String,updateHashMap: HashMap<String,Any>){
        val docRef = db.collection(Constant.DB_PRODUCT_COLLECTION).document(productDocumentId)
        docRef.update(updateHashMap).addOnSuccessListener {
            Log.e(TAG, "updateProduct: Updated successfully", )
            Toast.makeText(context, "Updated successfully", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Log.e(TAG, "updateProduct: error while updating!!", )
            Toast.makeText(context, "Error while updating!!", Toast.LENGTH_SHORT).show()
        }
    }

    fun deleteProduct(productDocumentId: String){

        // Warning: Deleting a document does not delete its subcollections!

        db.collection(Constant.DB_PRODUCT_COLLECTION).document(productDocumentId)
            .delete()
            .addOnSuccessListener { Log.e(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.e(TAG, "Error deleting document", e) }
    }




//    fun getSingleProduct(id:String) : Product
//    {
//       return list[0]
//    }

}