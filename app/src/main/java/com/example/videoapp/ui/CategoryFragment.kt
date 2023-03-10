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
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.videoapp.adapter.LoadMoreAdapter
import com.example.videoapp.adapter.MoviesAdapter
import com.example.videoapp.databinding.FragmentCategoryBinding
import com.example.videoapp.viewmodel.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CategoryFragment : Fragment() {

    private lateinit var binding: FragmentCategoryBinding

    @Inject
    lateinit var moviesAdapter: MoviesAdapter

    private val viewModel: MoviesViewModel by viewModels()

    private val args: CategoryFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoryNumber: String = convertCategoryToNumber(args.movieCategory)

        binding.apply {
            lifecycleScope.launchWhenCreated {
                viewModel.moviesCategoryList(categoryNumber).collect {
                    moviesAdapter.submitData(it)
                }
            }

            moviesAdapter.setOnItemClickListener {
                val direction = CategoryFragmentDirections
                    .actionCategoryFragmentToMovieDetailsFragment(it.id)
                findNavController().navigate(direction)
            }

            lifecycleScope.launchWhenCreated {
                moviesAdapter.loadStateFlow.collect{
                    val state = it.refresh
                    prgBarMovies.isVisible = state is LoadState.Loading
                }
            }

            rlMovies.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = moviesAdapter
            }

            rlMovies.adapter=moviesAdapter.withLoadStateFooter(
                LoadMoreAdapter{
                    moviesAdapter.retry()
                }
            )
        }
    }

    private fun convertCategoryToNumber(category: String): String {
        when(category){
            "Action" -> return "28"
            "Adventure" -> return "12"
            "Animation" -> return "16"
            "Comedy" -> return "35"
            "Crime" -> return "80"
            "Documentary" -> return "99"
            "Drama" -> return "18"
            "Family" -> return "10751"
            "Fantasy" -> return "14"
            "Western" -> return "37"
            "History" -> return "36"
            "Horror" -> return "27"
            "Music" -> return "10402"
            "Mystery" -> return "9648"
            "Romance" -> return "10749"
            "Science" -> return "878"
            "TV Movie" -> return "10770"
            "Thriller" -> return "53"
            " War" -> return "10752"
        }
        return ""
    }
}