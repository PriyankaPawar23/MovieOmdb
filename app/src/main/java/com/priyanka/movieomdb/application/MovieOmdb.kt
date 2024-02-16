package com.booklet.app.application

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import com.priyanka.movieomdb.data.db.AppDatabase
import com.priyanka.movieomdb.data.network.RetrofitService
import com.priyanka.movieomdb.data.repository.DBRepository
import com.priyanka.movieomdb.data.repository.MainRepository
import com.priyanka.movieomdb.data.viewmodel.DBViewModelFactory
import com.priyanka.movieomdb.data.viewmodel.MainViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class MovieOmdb : Application(), KodeinAware, LifecycleObserver {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@MovieOmdb))

        bind() from singleton { RetrofitService.getInstance(instance()) }
        bind() from singleton { AppDatabase.getInstance(instance()) }
        bind() from singleton { MainRepository(instance()) }
        bind() from provider { MainViewModelFactory(instance()) }

        bind() from singleton { DBRepository(instance()) }
        bind() from provider { DBViewModelFactory(instance()) }
    }
    override fun onCreate() {
        super.onCreate()
    }
}