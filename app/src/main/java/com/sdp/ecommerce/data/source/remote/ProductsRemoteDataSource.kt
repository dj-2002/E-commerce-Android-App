package com.sdp.ecommerce.data.source.remote

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

import com.sdp.ecommerce.data.Product
import com.sdp.ecommerce.data.Result
import com.sdp.ecommerce.data.Result.Error
import com.sdp.ecommerce.data.Result.Success
import com.sdp.ecommerce.data.source.ProductDataSource
import kotlinx.coroutines.tasks.await

class ProductsRemoteDataSource : ProductDataSource {
	private val firebaseDb: FirebaseFirestore = Firebase.firestore
	private val firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
	private val observableProducts = MutableLiveData<Result<List<Product>>?>()
	private fun storageRef() = firebaseStorage.reference
	private fun productsCollectionRef() = firebaseDb.collection(PRODUCT_COLLECTION)

	override suspend fun refreshProducts() {
		observableProducts.value = getAllProducts()
	}

	override fun observeProducts(): LiveData<Result<List<Product>>?> {
		return observableProducts
	}

	override suspend fun getAllProducts(): Result<List<Product>> {
		Log.e(TAG, "getAllProducts: ")
		val resRef = productsCollectionRef().get().await()
		return if (!resRef.isEmpty) {
			Log.e(TAG, "getAllProducts: ${resRef.toString()}")
			Success(resRef.toObjects(Product::class.java))
		} else {
			Error(Exception("Error getting Products!"))
		}
	}

	override suspend fun insertProduct(newProduct: Product) {
		productsCollectionRef().add(newProduct.toHashMap()).await()
	}

	override suspend fun updateProduct(proData: Product) {
		val resRef =
			productsCollectionRef().whereEqualTo(PRODUCT_ID_FIELD, proData.productId).get().await()
		if (!resRef.isEmpty) {
			val docId = resRef.documents[0].id
			productsCollectionRef().document(docId).set(proData.toHashMap()).await()
		} else {
			Log.e(TAG, "onUpdateProduct: product with id: $proData.productId not found!")
		}
	}

	override suspend fun getProductById(productId: String): Result<Product> {
		val resRef = productsCollectionRef().whereEqualTo(PRODUCT_ID_FIELD, productId).get().await()
		return if (!resRef.isEmpty) {
			Success(resRef.toObjects(Product::class.java)[0])
		} else {
			Error(Exception("Product with id: $productId Not Found!"))
		}
	}

	override suspend fun deleteProduct(productId: String) {
		Log.e(TAG, "onDeleteProduct: delete product with Id: $productId initiated")
		val resRef = productsCollectionRef().whereEqualTo(PRODUCT_ID_FIELD, productId).get().await()
		if (!resRef.isEmpty) {
			val product = resRef.documents[0].toObject(Product::class.java)
			val imgUrls = product?.images

			//deleting images first
			imgUrls?.forEach { imgUrl ->
				deleteImage(imgUrl)
			}

			//deleting doc containing product
			val docId = resRef.documents[0].id
			productsCollectionRef().document(docId).delete().addOnSuccessListener {
				Log.e(TAG, "onDelete: DocumentSnapshot successfully deleted!")
			}.addOnFailureListener { e ->
				Log.w(TAG, "onDelete: Error deleting document", e)
			}
		} else {
			Log.e(TAG, "onDeleteProduct: product with id: $productId not found!")
		}
	}

	override suspend fun updateProductQuanity(productId: String, ownerId: String, quantity: Int) {
		val resRef = productsCollectionRef().whereEqualTo(PRODUCT_ID_FIELD, productId).get().await()
		val product = resRef.toObjects(Product::class.java)
		product[0].quantity-=quantity
		updateProduct(product[0])
	}

	override suspend fun uploadImage(uri: Uri, fileName: String): Uri? {
		val imgRef = storageRef().child("$SHOES_STORAGE_PATH/$fileName")
		val uploadTask = imgRef.putFile(uri)
		val uriRef = uploadTask.continueWithTask { task ->
			if (!task.isSuccessful) {
				task.exception?.let { throw it }
			}
			imgRef.downloadUrl
		}
		return uriRef.await()
	}

	override fun deleteImage(imgUrl: String) {
		val ref = firebaseStorage.getReferenceFromUrl(imgUrl)
		ref.delete().addOnSuccessListener {
			Log.e(TAG, "onDelete: image deleted successfully!")
		}.addOnFailureListener { e ->
			Log.e(TAG, "onDelete: Error deleting image, error: $e")
		}
	}

	override fun revertUpload(fileName: String) {
		val imgRef = storageRef().child("${SHOES_STORAGE_PATH}/$fileName")
		imgRef.delete().addOnSuccessListener {
			Log.e(TAG, "onRevert: File with name: $fileName deleted successfully!")
		}.addOnFailureListener { e ->
			Log.e(TAG, "onRevert: Error deleting file with name = $fileName, error: $e")
		}
	}

	companion object {
		private const val PRODUCT_COLLECTION = "products"
		private const val PRODUCT_ID_FIELD = "productId"
		private const val SHOES_STORAGE_PATH = "Shoes"
		private const val TAG = "ProductsRemoteSource"
	}
}