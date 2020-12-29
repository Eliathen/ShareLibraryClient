package com.szymanski.sharelibrary.mock

import com.szymanski.sharelibrary.core.api.model.request.*
import com.szymanski.sharelibrary.core.utils.BookStatus
import com.szymanski.sharelibrary.core.utils.ExchangeStatus
import com.szymanski.sharelibrary.features.book.domain.model.Book
import com.szymanski.sharelibrary.features.exchange.domain.model.Exchange
import com.szymanski.sharelibrary.features.user.domain.model.Coordinate
import com.szymanski.sharelibrary.features.user.domain.model.Login
import com.szymanski.sharelibrary.features.user.domain.model.Token
import com.szymanski.sharelibrary.features.user.domain.model.User
import org.jetbrains.annotations.TestOnly

@TestOnly
fun CoordinatesRequest.Companion.mock() = CoordinatesRequest(
    id = 1,
    latitude = 50.123456,
    longitude = 25.123456
)

@TestOnly
fun UserRequest.Companion.mock() = UserRequest(
    coordinates = CoordinatesRequest.Companion.mock(),
    email = "poczta@pocztowska.pl",
    name = "John",
    surname = "Dee",
    username = "johndee",
    password = charArrayOf('z', 'a', 'q', '1', '@', 'W', 'S', 'X')
)

@TestOnly
fun Coordinate.Companion.mock() = Coordinate(
    id = 1,
    latitude = 50.123456,
    longitude = 25.123456
)

@TestOnly
fun User.Companion.mock() = User(
    id = 1,
    coordinates = Coordinate.Companion.mock(),
    email = "poczta@pocztowska.pl",
    name = "John",
    surname = "Dee",
    username = "johndee",
    password = charArrayOf('z', 'a', 'q', '1', '@', 'W', 'S', 'X')
)

@TestOnly
fun Token.Companion.mock() = Token(
    accessToken = "accessToken",
    tokenType = "Bearer"
)

@TestOnly
fun Login.Companion.mock() = Login(
    id = 1,
    userNameOrEmail = "johndee",
    token = Token.Companion.mock(),
    password = charArrayOf('z', 'a', 'q', '1', '@', 'W', 'S', 'X')
)

@TestOnly
fun Book.Companion.mock() = Book(
    id = 1L,
    title = "title",
    authors = listOf(),
    cover = byteArrayOf(),
    status = BookStatus.AT_OWNER,
    atUser = User.Companion.mock(),
    categories = listOf()
)

@TestOnly
fun AssignBookRequest.Companion.mock() = AssignBookRequest(
    userId = 1,
    bookId = 1
)

@TestOnly
fun WithdrawBookRequest.Companion.mock() = WithdrawBookRequest(
    userId = 1,
    bookId = 1
)

@TestOnly
fun Exchange.Companion.mock() = Exchange(
    book = Book.Companion.mock(),
    coordinates = Coordinate.Companion.mock(),
    deposit = 10.0,
    distance = 99.0,
    exchangeStatus = ExchangeStatus.STARTED,
    id = 1L,
    user = User.Companion.mock()
)

@TestOnly
fun SearchRequest.Companion.mock() = SearchRequest(
    latitude = 50.12345,
    longitude = 25.12345,
    radius = 1.0,
    categories = listOf(),
    query = "query"
)