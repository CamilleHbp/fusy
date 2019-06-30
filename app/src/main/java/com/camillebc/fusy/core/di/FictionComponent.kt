package com.camillebc.fusy.core.di

import com.camillebc.fusy.MainActivity
import com.camillebc.fusy.bookshelf.view.BookshelfFragment
import com.camillebc.fusy.core.di.modules.*
import com.camillebc.fusy.core.model.FictionViewModel
import com.camillebc.fusy.searchengine.SearchableActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        FictionDatabaseModule::class,
        FictionRepositoryModule::class,
        HardwareStatusModule::class
    ]
)
interface FictionComponent {
    fun inject(fictionViewModel: FictionViewModel)
    fun inject(mainActivity: MainActivity)
    fun inject(searchableActivity: SearchableActivity)
}