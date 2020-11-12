package com.szymanski.sharelibrary.features.users.registration.presentation.model

import com.szymanski.sharelibrary.features.users.domain.model.Address

data class AddressDisplayable(
    val country: String?,
    val city: String?,
    val street: String?,
    val postalCode: String?,
    val buildingNumber: String?,
    val coordinate: CoordinateDisplayable?,
) {

    constructor(address: Address) : this(
        country = address.country,
        city = address.city,
        street = address.street,
        postalCode = address.postalCode,
        buildingNumber = address.buildingNumber,
        coordinate = address.coordinate?.let { CoordinateDisplayable(it) }
    )

    fun toAddress() = Address(
        country = this.country,
        city = this.city,
        street = this.street,
        postalCode = this.postalCode,
        buildingNumber = this.buildingNumber,
        coordinate = coordinate?.let { coordinate.toCoordinate() }
    )


}
