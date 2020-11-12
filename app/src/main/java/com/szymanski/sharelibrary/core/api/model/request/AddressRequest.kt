package com.szymanski.sharelibrary.core.api.model.request

import com.szymanski.sharelibrary.features.users.domain.model.Address

data class AddressRequest(
    val country: String?,
    val city: String?,
    val postalCode: String?,
    val street: String?,
    val buildingNumber: String?,
) {
    constructor(address: Address) : this(
        buildingNumber = address.buildingNumber,
        city = address.city,
        country = address.country,
        postalCode = address.postalCode,
        street = address.street
    )
}