package com.szymanski.sharelibrary.features.book.di

import com.szymanski.sharelibrary.features.book.data.BookRepositoryImpl
import com.szymanski.sharelibrary.features.book.data.CategoryRepositoryImpl
import com.szymanski.sharelibrary.features.book.domain.BookRepository
import com.szymanski.sharelibrary.features.book.domain.CategoryRepository
import com.szymanski.sharelibrary.features.book.domain.usecase.*
import com.szymanski.sharelibrary.features.book.navigation.BookNavigator
import com.szymanski.sharelibrary.features.book.navigation.BookNavigatorImpl
import com.szymanski.sharelibrary.features.book.presentation.all.BooksAdapter
import com.szymanski.sharelibrary.features.book.presentation.all.BooksViewModel
import com.szymanski.sharelibrary.features.book.presentation.details.BookDetailsViewModel
import com.szymanski.sharelibrary.features.book.presentation.otheruserbook.OtherUserBooksAdapter
import com.szymanski.sharelibrary.features.book.presentation.otheruserbook.OtherUserBooksViewModel
import com.szymanski.sharelibrary.features.book.presentation.save.AddAuthorAdapter
import com.szymanski.sharelibrary.features.book.presentation.save.SaveBookViewModel
import com.szymanski.sharelibrary.features.book.presentation.searchbook.SearchBookAdapter
import com.szymanski.sharelibrary.features.book.presentation.searchbook.SearchBookViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val bookModule = module {

    //viewModels
    viewModel { BooksViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { SearchBookViewModel(get(), get(), get(), get(), get(), get(), get()) }
    viewModel { SaveBookViewModel(get(), get(), get(), get()) }
    viewModel { OtherUserBooksViewModel(get(), get(), get(), get(), get(), get(), get()) }
    viewModel { BookDetailsViewModel(get(), get(), get(), get(), get()) }

    //repository
    factory<BookRepository> { BookRepositoryImpl(get(), get(), get()) }
    factory<CategoryRepository> { CategoryRepositoryImpl(get(), get()) }

    //navigation
    factory<BookNavigator> { BookNavigatorImpl(get()) }

    //useCases
    factory { SaveBookUseCase(get(), get()) }
    factory { GetUsersBookUseCase(get()) }
    factory { SearchBookUseCase(get()) }
    factory { GetCoverUseCase(get()) }
    factory { AssignBookToUserUseCase(get()) }
    factory { GetCategoriesUseCase(get()) }

    //Adapters
    factory { SearchBookAdapter() }
    factory { AddAuthorAdapter() }
    factory { BooksAdapter() }
    factory { OtherUserBooksAdapter() }
}