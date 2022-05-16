package com.example.tmdb.ui.listing

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.tmdb.Config
import com.example.tmdb.R
import com.example.tmdb.model.Movie
import com.example.tmdb.ui.details.DetailsActivity
import kotlinx.android.synthetic.main.list_item_movie.view.*
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList

class MoviesAdapter(private val context: Context, private val list: ArrayList<Movie>) :
    RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    class MovieViewHolder(private val context: Context, itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(movie: Movie) {
            itemView.setOnClickListener {
                val intent = Intent(context, DetailsActivity::class.java)
                intent.putExtra(DetailsActivity.EXTRAS_MOVIE_ID, movie.id)
                context.startActivity(intent)
            }
            itemView.tvTitle.text = movie.title
            Glide.with(context).load(Config.IMAGE_URL + movie.poster_path)
                .apply(
                    RequestOptions().override(400, 400).centerInside()
                        .placeholder(R.drawable.placehoder)
                ).into(itemView.ivPoster)
            itemView.tvYear.text = movie.release_date
            val calendar: Calendar = Calendar.getInstance()
            val year: Int = calendar.get(Calendar.YEAR)
            if(year == splitString(movie.release_date)){
                itemView.tvYear.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark))
            }else{
                itemView.tvYear.setTextColor(ContextCompat.getColor(context, android.R.color.white))
            }
        }

        private fun splitString(releaseDate: String?): Int {
            val delim = "-"
            val arr = Pattern.compile(delim).split(releaseDate)
            return arr[0].toInt()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_movie, parent, false)
        return MovieViewHolder(context, view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(list[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<Movie>) {
        list.clear()
        val sortedList = newList.sortedBy { movie -> movie.popularity }
        list.addAll(sortedList)
        notifyDataSetChanged()
    }

}