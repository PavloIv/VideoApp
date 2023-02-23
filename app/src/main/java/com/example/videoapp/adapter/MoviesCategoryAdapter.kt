package com.example.videoapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.videoapp.R
import com.example.videoapp.databinding.CategoryItemBinding
import com.example.videoapp.response.MovieCategoryResponse
import com.example.videoapp.ui.CategoryFragment
import com.example.videoapp.viewmodel.MoviesViewModel
import javax.inject.Inject

class MoviesCategoryAdapter(private val collect: List<MovieCategoryResponse>): RecyclerView.Adapter<MoviesCategoryAdapter.CategoryViewHolder>() {

    @Inject
    lateinit var movieSimpleItemAdapter: MovieSimpleItemAdapter

    private lateinit var context: Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MoviesCategoryAdapter.CategoryViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_item,parent,false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoviesCategoryAdapter.CategoryViewHolder, position: Int) {
        holder.binding.apply {
            val collect = collect[position]
            tvGenreMovie.text =collect.title

            rvMovieChild.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = movieSimpleItemAdapter
            }

            rvMovieChild.adapter=movieSimpleItemAdapter.withLoadStateFooter(
                LoadMoreAdapter{
                    movieSimpleItemAdapter.retry()
                }
            )
        }
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    inner class CategoryViewHolder(itemView : View): RecyclerView.ViewHolder(itemView) {
        val binding = CategoryItemBinding.bind(itemView)
    }
}
