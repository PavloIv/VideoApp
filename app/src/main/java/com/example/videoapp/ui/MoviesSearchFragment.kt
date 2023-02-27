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
import com.example.videoapp.databinding.FragmentMoviesSearchBinding
import com.example.videoapp.viewmodel.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MoviesSearchFragment : Fragment() {

    private lateinit var binding: FragmentMoviesSearchBinding

    @Inject
    lateinit var moviesAdapter: MoviesAdapter

    private val viewModel: MoviesViewModel by viewModels()

    private val args: MoviesSearchFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoviesSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            val movieName: String = args.movieName
            if (movieName.isNotEmpty()) {
                lifecycleScope.launchWhenCreated {
                    viewModel.movieSearchList(movieName).collect {
                        moviesAdapter.submitData(it)
                    }
                }
            }

            moviesAdapter.setOnItemClickListener {
                val direction =
                    MoviesSearchFragmentDirections.actionMoviesSearchFragmentToMovieDetailsFragment(
                        it.id
                    )
                findNavController().navigate(direction)
            }

            lifecycleScope.launchWhenCreated {
                moviesAdapter.loadStateFlow.collect {
                    val state = it.refresh
                    prgBarMovies.isVisible = state is LoadState.Loading
                }
            }

            rlMovies.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = moviesAdapter
            }

            rlMovies.adapter = moviesAdapter.withLoadStateFooter(
                LoadMoreAdapter {
                    moviesAdapter.retry()
                }
            )
        }
    }
}