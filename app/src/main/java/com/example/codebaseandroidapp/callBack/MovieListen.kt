package com.example.codebaseandroidapp.callBack

import com.example.codebaseandroidapp.model.Movie

class MovieListen(val clickListener: (movie: Movie) -> (Unit)) {
    fun onCLick(movie: Movie) = clickListener(movie)
}