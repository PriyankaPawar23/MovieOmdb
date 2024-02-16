package com.priyanka.movieomdb.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.priyanka.movieomdb.data.repository.DBRepository

class DBViewModelFactory(
    private val repository: DBRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        try {
            val constructor = modelClass.getDeclaredConstructor(DBRepository::class.java)
            return constructor.newInstance(repository)
        } catch (e: Exception) {
        }
        return super.create(modelClass)
    }
}