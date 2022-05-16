package com.example.tmdb.ui.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import com.example.tmdb.Config
import com.example.tmdb.R
import com.example.tmdb.model.MovieDesc
import com.example.tmdb.model.Result
import kotlinx.android.synthetic.main.activity_details.*

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private val viewModel by viewModels<DetailsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        intent?.getIntExtra(EXTRAS_MOVIE_ID, 0)?.let { id ->
            viewModel.getMovieDetail(id)
            subscribeUi()
        } ?: showError("Unknown Movie")
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun subscribeUi() {
        viewModel.movie.observe(this, Observer { result ->

            when (result.status) {
                Result.Status.SUCCESS -> {
                    result.data?.let {
                        updateUi(it)
                    }
                    loading.visibility = View.GONE
                }

                Result.Status.ERROR -> {
                    result.message?.let {
                        showError(it)
                    }
                    loading.visibility = View.GONE
                }

                Result.Status.LOADING -> {
                    loading.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun showError(msg: String) {
        Snackbar.make(vParent, msg, Snackbar.LENGTH_INDEFINITE).setAction("DISMISS") {
        }.show()
    }

    @SuppressLint("SetTextI18n")
    private fun updateUi(movie: MovieDesc) {
        title = movie.title
        tvTitle.text = movie.title
        tvDescription.text = movie.overview
        Glide.with(this).load(Config.IMAGE_URL + movie.poster_path)
            .apply(
                RequestOptions().override(400, 400).centerInside()
                    .placeholder(R.drawable.placehoder)
            ).into(ivCover)
        tvVote.text = movie.vote_average
        tvCount.text = movie.vote_count
        tvDate.text = movie.release_date
        val genreNames = mutableListOf<String>()
        movie.genres.map {
            genreNames.add(it.name)
        }
        tvGenre.text = genreNames.joinToString(separator = ", ")
        
        genreTitle.text = "Genre:"
        votingAverageTitle.text = "Voting Average:"
        votingCountTitle.text = "Vote Count:"
        releaseDateTitle.text = "Release Date:"
    }

    companion object {
        const val EXTRAS_MOVIE_ID = "movie_id"
    }
}