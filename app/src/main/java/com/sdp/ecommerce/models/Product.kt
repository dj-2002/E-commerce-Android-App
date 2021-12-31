package com.sdp.ecommerce.models

import android.os.Parcelable

data class Product  constructor(
	var productId: String = "",
	var name: String = "Dummy",
	var owner: String = "owner",
	var description: String = "description goes here",
	var category: String = "Electronics",
	var price: Double = 999.99,
	var mrp: Double = 1500.99,
	var availableSizes: List<String> = ArrayList(),
	var availableColors: List<String> = ArrayList(),
	var images: MutableList<String> = ArrayList(),
	var rating: Double = 0.0
) {
    fun getHashMap(): HashMap<String, Any> {
		return hashMapOf(
			"productId" to productId,
			"name" to name,
			"owner" to owner,
			"description" to description,
			"category" to category,
			"price" to price,
			"mrp" to mrp,
			"availableSizes" to availableSizes,
			"availableColors" to availableColors,
			"images" to images,
			"rating" to rating
		)
    }
}