package com.example.codebaseandroidapp.model

data class Detail(
    val adult: Boolean,
    val backdrop_path: String,
    val belongs_to_collection: Any,
    val budget: Int,
    val genres: List<Genre>,
    val homepage: String,
    val id: Int,
    val imdb_id: String,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val production_companies: List<ProductionCompany>,
    val production_countries: List<ProductionCountry>,
    val release_date: String,
    val revenue: Int,
    val runtime: Int,
    val spoken_languages: List<SpokenLanguage>,
    val status: String,
    val tagline: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int,
    var is_love: Boolean
) {
    fun transformToMovie(): Movie {
        val movie = Movie()
        return movie.apply {
            this.adult = this@Detail.adult
            this.backdrop_path = this@Detail.backdrop_path
            this.id = this@Detail.id
            this.is_love = this@Detail.is_love
            this.original_language = this@Detail.original_language
            this.original_title = this@Detail.original_title
            this.overview = this@Detail.overview
            this.popularity = this@Detail.popularity
            this.poster_path = this@Detail.poster_path
            this.release_date = this@Detail.release_date
            this.title = this@Detail.title
            this.video = this@Detail.video
            this.vote_average = this@Detail.vote_average
            this.vote_count = this@Detail.vote_count
        }
    }
}