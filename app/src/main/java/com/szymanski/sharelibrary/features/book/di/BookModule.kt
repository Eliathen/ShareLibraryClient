package com.szymanski.sharelibrary.features.book.di

import com.szymanski.sharelibrary.features.book.data.BookRepositoryImpl
import com.szymanski.sharelibrary.features.book.domain.BookRepository
import com.szymanski.sharelibrary.features.book.domain.usecase.SaveBookUseCase
import com.szymanski.sharelibrary.features.book.presentation.BooksViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val bookModule = module {

    viewModel {
        BooksViewModel(get(), get())
    }
    factory<BookRepository> {
        BookRepositoryImpl(get(), get())
    }

    factory { SaveBookUseCase(get()) }

}