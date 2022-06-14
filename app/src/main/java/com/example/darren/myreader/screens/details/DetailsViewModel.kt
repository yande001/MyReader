package com.example.darren.myreader.screens.details

import androidx.lifecycle.ViewModel
import com.example.darren.myreader.data.Resource
import com.example.darren.myreader.model.Item
import com.example.darren.myreader.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: BookRepository
): ViewModel()
{
    suspend fun getBookInfo(bookId: String): Resource<Item>{
        return repository.getBookInfo(bookId)
    }
}