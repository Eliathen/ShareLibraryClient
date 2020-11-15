package com.szymanski.sharelibrary.features.user.domain.model

data class Address(
    val id: Long?,
    val country: String?,
    val city: String?,
    val street: String?,
    val postalCode: String?,
    val buildingNumber: String?,
    val coordinate: Coordinate?,
)
