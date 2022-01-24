package com.sdp.ecommerce

import android.app.Application
import com.sdp.ecommerce.data.source.repository.AuthRepoInterface
import com.sdp.ecommerce.data.source.repository.ProductsRepoInterface

class
ShoppingApplication(val application: Application) : Application() {
	val authRepository: AuthRepoInterface
		get() = ServiceLocator.provideAuthRepository(application)

	val productsRepository: ProductsRepoInterface
		get() = ServiceLocator.provideProductsRepository(application)

	override fun onCreate() {
		super.onCreate()
	}
}