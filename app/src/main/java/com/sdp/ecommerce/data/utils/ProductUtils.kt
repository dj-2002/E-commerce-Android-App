package com.sdp.ecommerce.data.utils

val ShoeSizes = mapOf(
	"UK4" to 4,
	"UK5" to 5,
	"UK6" to 6,
	"UK7" to 7,
	"UK8" to 8,
	"UK9" to 9,
	"UK10" to 10,
	"UK11" to 11,
	"UK12" to 12
)

val ShoeColors = mapOf(
	"black" to "#000000",
	"white" to "#FFFFFF",
	"red" to "#FF0000",
	"green" to "#00FF00",
	"blue" to "#0000FF",
	"yellow" to "#FFFF00",
	"cyan" to "#00FFFF",
	"magenta" to "#FF00FF"
)

val ProductCategories = arrayOf("Mobiles, Computers", "Tv , Appliances , Electronics","Men's Fashion","Women's Fashion","Home,Kitchen,Pets","Beauty,Health,Grocery","Sports,Fitness,Bags,Luggage","Toys,Baby Products,Kids Fashion","Car , MotorBike ,Industrial","Books","Other")
val PriceCategories = arrayOf("500 - 1000","1000-2000","2000-5000")
val PriceCategoriesInt = hashMapOf<String,Pair<Int,Int>>(Pair("500 - 1000",Pair(500,1000)),Pair("1000 - 2000",Pair(1000,2000)),Pair("2000 - 5000",Pair(2000,5000)))