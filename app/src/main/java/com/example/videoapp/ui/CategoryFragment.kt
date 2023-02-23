package com.example.videoapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.videoapp.adapter.LoadMoreAdapter
import com.example.videoapp.adapter.MovieSimpleItemAdapter
import com.example.videoapp.adapter.MoviesCategoryAdapter
import com.example.videoapp.databinding.CategoryItemBinding
import com.example.videoapp.databinding.FragmentCategoryBinding
import com.example.videoapp.viewmodel.MoviesViewModel
import javax.inject.Inject

class CategoryFragment : Fragment() {

    private lateinit var binding: FragmentCategoryBinding

    private lateinit var sItemBinding: CategoryItemBinding

    @Inject
    lateinit var moviesCategoryAdapter: MoviesCategoryAdapter

    @Inject
    lateinit var movieSimpleItemAdapter: MovieSimpleItemAdapter

    private val viewModel: MoviesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sItemBinding.apply {
            lifecycleScope.launchWhenCreated {
                viewModel.moviesList.collect {
                    movieSimpleItemAdapter.submitData(it)
                }
            }

            movieSimpleItemAdapter.setOnItemClickListener {
                val direction = MoviesFragmentDirections.actionMoviesFragmentToMovieDetailsFragment(it.id)
                findNavController().navigate(direction)
            }

            lifecycleScope.launchWhenCreated {
                movieSimpleItemAdapter.loadStateFlow.collect{
                    val state = it.refresh
                    prgBarMovies.isVisible = state is LoadState.Loading
                }
            }


            rvMovieChild.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = movieSimpleItemAdapter
            }

            rvMovieChild.adapter=movieSimpleItemAdapter.withLoadStateFooter(
                LoadMoreAdapter{
                    movieSimpleItemAdapter.retry()
                }
            )
        }

//        val category= listOf(
//            MovieCategoryResponse("Popular", viewModel.moviesList)
//        )
//        binding.apply {
//            rvMain.adapter = MoviesCategoryAdapter(category)
//        }

    }

}