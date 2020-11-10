package com.szymanski.sharelibrary.core.api.model.request

import com.szymanski.sharelibrary.features.users.domain.model.Address

data class AddressRequest(
    val buildingNumber: String?,
    val city: String?,
    val country: String?,
    val street: String?,
) {
    constructor(address: Address) : this(
        buildingNumber = address.buildingNumber,
        city = address.city,
        country = address.country,
        street = address.street
    )
}