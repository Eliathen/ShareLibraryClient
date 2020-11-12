package com.szymanski.sharelibrary.features.books.di

import com.szymanski.sharelibrary.features.books.presentation.BooksViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val bookModule = module {

    viewModel {
        BooksViewModel(get())
    }

}