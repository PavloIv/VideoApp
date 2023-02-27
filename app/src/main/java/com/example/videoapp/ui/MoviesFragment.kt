package com.example.videoapp.ui

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.videoapp.adapter.LoadMoreAdapter
import com.example.videoapp.adapter.MoviesAdapter
import com.example.videoapp.databinding.FragmentMoviesBinding
import com.example.videoapp.viewmodel.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MoviesFragment : Fragment() {

    private lateinit var binding: FragmentMoviesBinding

    @Inject
    lateinit var moviesAdapter: MoviesAdapter

    private val viewModel: MoviesViewModel by viewModels()

    private val category = arrayOf("Category name","Action", "History", "Adventure","Horror","Animation","Music","Comedy"
        ,"Mystery","Crime","Romance","Documentary","Science","Drama","TV Movie","Family","Thriller"
        ,"Fantasy","War","Western")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMoviesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinner = binding.categorySpinner
        ArrayAdapter(
            requireContext(),
            R.layout.simple_spinner_item
            ,category
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        binding.apply {

            lifecycleScope.launchWhenCreated {
                viewModel.moviesList.collect {
                    moviesAdapter.submitData(it)
                }
            }

            moviesAdapter.setOnItemClickListener {
                val direction = MoviesFragmentDirections.actionMoviesFragmentToMovieDetailsFragment(it.id)
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

        binding.categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position > 0) {
                    val direction =
                        MoviesFragmentDirections
                            .actionMoviesFragmentToCategoryFragment(category[position])
                    findNavController().navigate(direction)
                    binding.categorySpinner.setSelection(0)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.btnSearch.setOnClickListener {
            val searchMovieName: String = binding.movieName.text.toString()
            val direction = MoviesFragmentDirections.actionMoviesFragmentToMoviesSearchFragment(searchMovieName)
            findNavController().navigate(direction)
        }
    }
}