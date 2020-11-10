package com.szymanski.sharelibrary.features.users.domain.model

data class Address(
    val country: String?,
    val city: String?,
    val street: String?,
    val buildingNumber: String?,
    val coordinate: Coordinate?,
)
