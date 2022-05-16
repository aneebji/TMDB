package com.example.tmdb.ui.details

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.tmdb.data.MovieRepository
import com.example.tmdb.model.MovieDesc
import com.example.tmdb.model.Result
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart


class DetailsViewModel @ViewModelInject constructor(private val movieRepository: MovieRepository) : ViewModel() {

    private var _id = MutableLiveData<Int>()
    private val _movie: LiveData<Result<MovieDesc>> = _id.distinctUntilChanged().switchMap {
        liveData {
            movieRepository.fetchMovie(it).onStart {
                emit(Result.loading())
            }.collect {
                emit(it)
            }
        }
    }
    val movie = _movie

    fun getMovieDetail(id: Int) {
        _id.value = id
    }
}