package com.priyanka.movieomdb

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.priyanka.movieomdb.constant.ApiConstant
import com.priyanka.movieomdb.data.response.MovieDetailsResponse
import com.priyanka.movieomdb.data.viewmodel.DBViewModel
import com.priyanka.movieomdb.data.viewmodel.DBViewModelFactory
import com.priyanka.movieomdb.data.viewmodel.MainViewModel
import com.priyanka.movieomdb.data.viewmodel.MainViewModelFactory
import com.priyanka.movieomdb.databinding.ActivityMovieDetailsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class MovieDetailsActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()
    private lateinit var binding: ActivityMovieDetailsBinding

    private val factory: MainViewModelFactory by instance()
    private lateinit var viewModel: MainViewModel
    private val dbFactory: DBViewModelFactory by instance()
    private lateinit var dbViewModel: DBViewModel

    var dialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        dbViewModel = ViewModelProvider(this, dbFactory)[DBViewModel::class.java]


        if (intent != null) {
            val id = intent.getStringExtra("id")

            if (id != null) {
                if (id.isNotEmpty()) {
                    dialog = showProgressDialog()
                    if (isInternetAvailable(this))
                        viewModel.getMovieDetails(ApiConstant.API_KEY, id)
                    else
                        CoroutineScope(Dispatchers.Main).launch {
                            dbViewModel.getMovieDetails(id).observe(this@MovieDetailsActivity) {
                                if (it != null) {
                                    setMovieData(it)
                                } else {
                                    dialog?.dismiss()
                                    binding.mainLayout.visibility = View.GONE
                                    toast("Please check internet connection")
                                }
                            }
                        }
                }
            }
        }

        viewModel.movieDetails.observe(this) {
            if (it != null) {
                if (it.Response.equals("True")) {
                    setMovieData(it)
                    CoroutineScope(Dispatchers.Main).launch {
                        dbViewModel.insertMovieDetails(it)
                    }
                } else {
                    dialog?.dismiss()
                }
            }
        }

        binding.backBtn.bringToFront()

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

    }

    private fun setMovieData(details: MovieDetailsResponse) {

        Glide.with(this)
            .load(details.Poster)
            .error(R.drawable.no_image_available)
            .into(binding.movieImg)

        binding.movieName.text = details.Title
        binding.genre.text = details.Genre.replace(", ", "|")
        try {
            val runtimeInMinutes = details.Runtime
                .split(" ")[0].toInt()

            val (hours, remainingMinutes) = convertMinutesToHoursAndMinutes(runtimeInMinutes)
            binding.time.text = buildString {
                append(hours)
                append(" hr ")
                append(remainingMinutes)
                append(" min")
            }
        } catch (e: Exception) {
        }


        binding.rating.text = details.imdbRating
        binding.plot.text = details.Plot
        binding.director.text = details.Director
        binding.actors.text = details.Actors
        binding.writer.text = details.Writer
        dialog?.dismiss()

    }

    private fun convertMinutesToHoursAndMinutes(minutes: Int): Pair<Int, Int> {
        val hours = minutes / 60
        val remainingMinutes = minutes % 60
        return Pair(hours, remainingMinutes)
    }
}