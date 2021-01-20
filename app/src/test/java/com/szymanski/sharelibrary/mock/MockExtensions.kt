package com.szymanski.sharelibrary.mock

import com.szymanski.sharelibrary.core.api.model.request.*
import com.szymanski.sharelibrary.core.api.model.response.*
import com.szymanski.sharelibrary.core.utils.BookCondition
import com.szymanski.sharelibrary.core.utils.BookStatus
import com.szymanski.sharelibrary.core.utils.ExchangeStatus
import com.szymanski.sharelibrary.features.book.domain.model.Book
import com.szymanski.sharelibrary.features.book.domain.model.Language
import com.szymanski.sharelibrary.features.exchange.domain.model.Exchange
import com.szymanski.sharelibrary.features.home.domain.model.ExchangeDetails
import com.szymanski.sharelibrary.features.user.domain.model.Coordinate
import com.szymanski.sharelibrary.features.user.domain.model.Login
import com.szymanski.sharelibrary.features.user.domain.model.Token
import com.szymanski.sharelibrary.features.user.domain.model.User
import org.jetbrains.annotations.TestOnly
import java.time.LocalDateTime

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
    categories = listOf(),
    language = Language.Companion.mock(),
    condition = BookCondition.NEW
)

@TestOnly
fun Language.Companion.mock() = Language(
    id = 1,
    name = "English"
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
    query = "query",
    languageId = 1,
    conditions = listOf(BookCondition.NEW.ordinal)
)

@TestOnly
fun CategoryResponse.Companion.mock() = CategoryResponse(
    id = 1,
    name = "Fantasy"
)

@TestOnly
fun CoordinatesResponse.Companion.mock() = CoordinatesResponse(
    id = 1L,
    latitude = 50.12345,
    longitude = 25.12345
)

@TestOnly
fun UserResponse.Companion.mock() = UserResponse(
    id = 1,
    email = "poczta@pocztowska.pl",
    name = "John",
    surname = "Dee",
    username = "johndee",
    coordinatesResponse = CoordinatesResponse.Companion.mock(),
    books = listOf()
)

@TestOnly
fun ChatRoomResponse.Companion.mock() = ChatRoomResponse(
    id = 1L,
    sender = UserResponse.mock(),
    recipient = UserResponse.mock()
)

@TestOnly
fun MessageResponse.Companion.mock() = MessageResponse(
    id = 1L,
    chatRoomResponse = ChatRoomResponse.mock(),
    sender = UserResponse.mock(),
    recipient = UserResponse.mock(),
    content = "Content",
    LocalDateTime.now().toString()
)

@TestOnly
fun LanguageResponse.Companion.mock() = LanguageResponse(
    id = 1,
    name = "English"
)

@TestOnly
fun UserWithoutBooks.Companion.mock() = UserWithoutBooks(
    id = 1,
    email = "poczta@pocztowska.pl",
    name = "John",
    surname = "Dee",
    username = "johndee",
    coordinates = CoordinatesResponse.Companion.mock(),
)

@TestOnly
fun BookResponse.Companion.mock() = BookResponse(
    id = 1L,
    title = "title",
    authors = listOf(),
    status = BookStatus.AT_OWNER,
    atUser = UserWithoutBooks.Companion.mock(),
    categories = listOf(),
    language = LanguageResponse.Companion.mock(),
    condition = BookCondition.NEW
)

@TestOnly
fun ExchangeResponse.Companion.mock() = ExchangeResponse(
    book = BookResponse.Companion.mock(),
    coordinates = CoordinatesResponse.mock(),
    deposit = 10.0,
    distance = 99.0,
    exchangeStatus = ExchangeStatus.STARTED,
    id = 1L,
    user = UserResponse.mock()
)

@TestOnly
fun RequirementResponse.Companion.mock() = RequirementResponse(
    id = 1L,
    exchange = ExchangeResponse.mock(),
    user = UserResponse.mock(),
    createTime = LocalDateTime.now().toString(),
    isActual = true
)

@TestOnly
fun CreateRequirementRequest.Companion.mock() = CreateRequirementRequest(
    exchangeId = 1L,
    userId = 1L
)

@TestOnly
fun RegisterRequest.Companion.mock() = RegisterRequest(
    coordinates = CoordinatesRequest.mock(),
    email = "poczta@pocztowska.pl",
    name = "John",
    surname = "Dee",
    username = "johndee",
    password = charArrayOf('z', 'a', 'q', '1', '@', 'W', 'S', 'X')
)

@TestOnly
fun RegisterResponse.Companion.mock() = RegisterResponse(
    coordinatesResponse = CoordinatesResponse.mock(),
    email = "poczta@pocztowska.pl",
    name = "John",
    surname = "Dee",
    username = "johndee",
    id = 1L
)

@TestOnly
fun TokenResponse.Companion.mock() = TokenResponse(
    accessToken = "AccessToken",
    tokenType = "Bearer"
)

@TestOnly
fun LoginRequest.Companion.mock() = LoginRequest(
    userNameOrEmail = "johndee",
    password = charArrayOf('z', 'a', 'q', '1', '@', 'W', 'S', 'X')
)

@TestOnly
fun LoginResponse.Companion.mock() = LoginResponse(
    id = 1L,
    userName = "johndee",
    response = TokenResponse.mock()
)

@TestOnly
fun SaveExchangeRequest.Companion.mock() = SaveExchangeRequest(
    Exchange.mock()
)

@TestOnly
fun ExchangeDetails.Companion.mock() = ExchangeDetails(
    book = Book.mock(),
    coordinates = Coordinate.mock(),
    deposit = 50.0,
    distance = 50.0,
    exchangeStatus = ExchangeStatus.DURING,
    id = 1,
    user = User.mock(),
    withUser = User.mock(),
    forBook = Book.mock()
)

@TestOnly
fun ExchangeWithDetailsResponse.Companion.mock() =
    com.szymanski.sharelibrary.core.api.model.response.ExchangeWithDetailsResponse(
        book = BookResponse.mock(),
        coordinates = CoordinatesResponse.mock(),
        deposit = 50.0,
        distance = 50.0,
        exchangeStatus = ExchangeStatus.DURING,
        id = 1,
        user = UserResponse.mock(),
        withUser = UserResponse.mock(),
        forBook = BookResponse.mock()
    )