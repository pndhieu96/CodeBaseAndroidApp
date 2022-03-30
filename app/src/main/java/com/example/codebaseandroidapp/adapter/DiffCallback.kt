package com.example.codebaseandroidapp.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.codebaseandroidapp.model.Movie
import com.example.codebaseandroidapp.model.MoviesWithGenre

class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}

class MoviesWithGenreDiffCallback : DiffUtil.ItemCallback<MoviesWithGenre>() {
    override fun areItemsTheSame(oldItem: MoviesWithGenre, newItem: MoviesWithGenre): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MoviesWithGenre, newItem: MoviesWithGenre): Boolean {
        return oldItem.movies ==  newItem.movies
    }
}