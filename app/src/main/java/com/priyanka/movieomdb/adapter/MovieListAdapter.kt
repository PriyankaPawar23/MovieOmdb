package com.priyanka.movieomdb.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.priyanka.movieomdb.MainActivity
import com.priyanka.movieomdb.MovieDetailsActivity
import com.priyanka.movieomdb.R
import com.priyanka.movieomdb.data.response.Search
import com.priyanka.movieomdb.databinding.MovieItemBinding

class MovieListAdapter(
    private val context: MainActivity,
) : RecyclerView.Adapter<MovieListAdapter.ViewHolder>() {
    var movies = mutableListOf<Search>()
    fun setMovieList(movies: List<Search>) {
        this.movies.addAll(movies)
        notifyDataSetChanged()
    }

    fun clearData() {
        this.movies.clear()
        notifyDataSetChanged()
    }

    // create new views
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val movie = movies[position]

        Glide.with(context)
            .load(movie.Poster)
            .error(R.drawable.no_image_available)
            .into(holder.viewBinding.movieImg)

        holder.viewBinding.movieTitle.text = movie.Title
        holder.viewBinding.movieSubtitle.text = movie.Year

        holder.viewBinding.mainLayout.setOnClickListener {
            val intent = Intent(context, MovieDetailsActivity::class.java)
            intent.putExtra("id", movie.imdbID)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                context,
                holder.viewBinding.movieImg,
                "img_transition"
            )
            context.startActivity(intent, options.toBundle())
        }
    }


    // return the number of the items in the list
    override fun getItemCount(): Int {
        return movies.size
    }

    class ViewHolder(val viewBinding: MovieItemBinding) :
        RecyclerView.ViewHolder(viewBinding.root)
}

