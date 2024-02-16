package com.priyanka.movieomdb

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.priyanka.movieomdb.adapter.MovieListAdapter
import com.priyanka.movieomdb.constant.ApiConstant
import com.priyanka.movieomdb.data.response.Search
import com.priyanka.movieomdb.data.viewmodel.DBViewModel
import com.priyanka.movieomdb.data.viewmodel.DBViewModelFactory
import com.priyanka.movieomdb.data.viewmodel.MainViewModel
import com.priyanka.movieomdb.data.viewmodel.MainViewModelFactory
import com.priyanka.movieomdb.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class MainActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()

    private lateinit var binding: ActivityMainBinding

    private val factory: MainViewModelFactory by instance()
    private lateinit var viewModel: MainViewModel
    private val dbFactory: DBViewModelFactory by instance()
    private lateinit var dbViewModel: DBViewModel

    var adapter = MovieListAdapter(this)

    private var movieList: List<Search> = ArrayList()

    private var currentPage = 1

    private var searchQuery = ""

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        dbViewModel = ViewModelProvider(this, dbFactory)[DBViewModel::class.java]

        binding.recyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerview.adapter = adapter

        viewModel.movieList.observe(this) {
            Log.e("searchResult.", "${it}")

            if (it != null) {
                if (it.Response == "True") {
                    if (it.Search.isNotEmpty()) {
                        Log.e("searchResult", "${it.Search.size}")
                        binding.progressBar.visibility = View.GONE
                        adapter.setMovieList(it.Search)

                        CoroutineScope(Dispatchers.Main).launch {

                            for (movie in it.Search) {
                                dbViewModel.insertMovies(movie)
                                Log.e("moviesss", "${movie.imdbID}")
                            }
                        }

                    }
                } else {
                    binding.progressBar.visibility = View.GONE
                    toast("no movies found")
                }

            }
        }

        viewModel.errorMessage.observe(this) {
            binding.progressBar.visibility = View.GONE
            toast("$it")
        }

        binding.nestedScrollView.viewTreeObserver.addOnScrollChangedListener {
            val view =
                binding.nestedScrollView.getChildAt(binding.nestedScrollView.childCount - 1) as View
            val diff: Int =
                view.bottom - (binding.nestedScrollView.height + binding.nestedScrollView
                    .scrollY)
            if (diff == 0) {
                // your pagination code
                if (isInternetAvailable(this)) {
                    binding.progressBar.visibility = View.VISIBLE
                    currentPage++
                    viewModel.getSearchMovies(ApiConstant.API_KEY, searchQuery, currentPage)
                }

            }
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // below line is to get our inflater
        val inflater = menuInflater

        // inside inflater we are inflating our menu file.
        inflater.inflate(R.menu.menu, menu)


        val searchItem = menu.findItem(R.id.actionSearch)
        val searchView = searchItem.actionView as SearchView?
        searchView!!.imeOptions = EditorInfo.IME_ACTION_DONE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                binding.progressBar.visibility = View.VISIBLE
                searchQuery = query
                if (isInternetAvailable(this@MainActivity))
                    viewModel.getSearchMovies(ApiConstant.API_KEY, query, currentPage)
                else
                    CoroutineScope(Dispatchers.Main).launch {
                        dbViewModel.getAllListByQuery(query).observe(this@MainActivity) {
                            if (it.isNotEmpty()) {
                                adapter.setMovieList(it)
                            } else {
                                toast("Please check internet connection")
                            }
                            binding.progressBar.visibility = View.GONE

                        }
                    }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                binding.progressBar.visibility = View.GONE
                currentPage = 1
                searchQuery = ""
                adapter.clearData()
                return false
            }
        })
        return true

    }
}