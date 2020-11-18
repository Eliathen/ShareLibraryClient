package com.szymanski.sharelibrary.features.book.di

import com.szymanski.sharelibrary.features.book.data.BookRepositoryImpl
import com.szymanski.sharelibrary.features.book.domain.BookRepository
import com.szymanski.sharelibrary.features.book.domain.usecase.GetUsersBookUseCase
import com.szymanski.sharelibrary.features.book.domain.usecase.SaveBookUseCase
import com.szymanski.sharelibrary.features.book.domain.usecase.SearchBookUseCase
import com.szymanski.sharelibrary.features.book.navigation.BookNavigator
import com.szymanski.sharelibrary.features.book.navigation.BookNavigatorImpl
import com.szymanski.sharelibrary.features.book.presentation.all.BooksViewModel
import com.szymanski.sharelibrary.features.book.presentation.save.SaveBookViewModel
import com.szymanski.sharelibrary.features.book.presentation.searchbook.SearchBookAdapter
import com.szymanski.sharelibrary.features.book.presentation.searchbook.SearchBookViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val bookModule = module {

    viewModel {
        BooksViewModel(get(), get(), get(), get(), get())
    }
    factory<BookRepository> {
        BookRepositoryImpl(get(), get(), get())
    }

    factory { SaveBookUseCase(get(), get()) }

    factory<BookNavigator> {
        BookNavigatorImpl(get())
    }
    factory {
        GetUsersBookUseCase(get())
    }
    viewModel {
        SearchBookViewModel(get(), get(), get())
    }
    factory {
        SearchBookAdapter()
    }
    factory {
        SearchBookUseCase(get())
    }
    viewModel {
        SaveBookViewModel(get(), get(), get())
    }
}