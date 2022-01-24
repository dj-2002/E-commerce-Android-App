package com.sdp.ecommerce.data

import com.sdp.ecommerce.data.Result.Success

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out R> {

	data class Success<Any>(val data: Any) : Result<Any>()
	data class Error(val exception: Exception) : Result<Nothing>()
	object Loading : Result<Nothing>()

	override fun toString(): String {
		return when (this) {
			is Success<*> -> "Success[data=$data]"
			is Error -> "Error[exception=$exception]"
			Loading -> "Loading"
		}
	}
}

/**
 * `true` if [Result] is of type [Success] & holds non-null [Success.data].
 */
val Result<*>.succeeded
	get() = this is Success && data != null