package com.riders.thelab.data.local.bean

import com.riders.thelab.data.local.model.Movie

enum class MovieEnum(
    val category: MovieCategoryEnum,
    val title: String,
    val genre: String,
    val year: String,
    val url: String,
    val type: String,
    var overview: String,
    var duration: Long,
    var rating: Double
) {

    MAD_MAX(
        MovieCategoryEnum.UPCOMING,
        "Mad Max: Fury Road",
        "Action & Adventure",
        "2015",
        "https://confluences.hypotheses.org/files/2023/01/mad-max-affiche-2.jpg",
        "action",
        "",
        15123665156144,
        3.2
    ),
    INSIDE_OUT(
        MovieCategoryEnum.UPCOMING,
        "Inside Out",
        "Animation, Kids & Family",
        "2015",
        "http://marvelll.fr/wp-content/uploads/2015/06/vice-versa-inside-out-film-2015-affiche-france.jpg",
        "action",
        "",
        15123665156144,
        3.2
    ),
    STAR_WARS(
        MovieCategoryEnum.POPULAR,
        "Star Wars: Episode VII - The Force Awakens",
        "Action",
        "2015",
        "https://cdn.shopify.com/s/files/1/1057/4964/products/Star-Wars-The-Force-Awakens-Vintage-Movie-Poster-Original-1-Sheet-27x41-7308_2000x.progressive.jpg?v=1666911067",
        "action",
        "",
        15123665156144,
        3.2
    ),
    SHAUN_THE_SHEEP(
        MovieCategoryEnum.POPULAR,
        "Shaun the Sheep",
        "Animation",
        "2015",
        "http://marvelll.fr/wp-content/uploads/2015/04/Shaun-le-Mouton-le-Film-Affiche.jpg",
        "action",
        "",
        15123665156144,
        3.2
    ),
    THE_MARTIAN(
        MovieCategoryEnum.POPULAR,
        "The Martian",
        "Science Fiction & Fantasy",
        "2015",
        "http://fr.web.img3.acsta.net/pictures/15/09/08/15/20/305329.jpg",
        "action",
        "",
        15123665156144,
        3.2
    ),
    MI_ROGUE_NATION(
        MovieCategoryEnum.UPCOMING,
        "Mission: Impossible Rogue Nation",
        "Action",
        "2015",
        "https://fusion.molotov.tv/arts/i/446x588/Ch8SHQoUv-wmMqUTaFFU-z8fiyvejhkSwR0SA2pwZxgB/jpg",
        "action",
        "",
        15123665156144,
        3.2
    ),
    UP(
        MovieCategoryEnum.POPULAR,
        "Up",
        "Animation",
        "2009",
        "http://www.walle.free.fr/up/affiche_fr_big.jpg",
        "action",
        "",
        15123665156144,
        3.2
    ),
    STAR_TREK(
        MovieCategoryEnum.POPULAR,
        "Star Trek",
        "Science Fiction",
        "2009",
        "http://www.avoir-alire.com/IMG/jpg/starr_grd.jpg",
        "action",
        "",
        15123665156144,
        3.2
    ),
    LEGO(
        MovieCategoryEnum.POPULAR,
        "The LEGO Movie",
        "Animation",
        "2014",
        "https://m.media-amazon.com/images/I/71rH-TN3BnL._AC_SY445_.jpg",
        "action",
        "",
        15123665156144,
        3.2
    ),
    IRON_MAN(
        MovieCategoryEnum.TRENDING,
        "Iron Man",
        "Action & Adventure",
        "2008",
        "http://www.chroniquedisney.fr/imgFiliale/marvel/2008-ironman1-1-big.jpg",
        "action",
        "",
        15123665156144,
        3.2
    ),
    ALIENS(
        MovieCategoryEnum.POPULAR,
        "Aliens",
        "Science Fiction",
        "1986",
        "http://images.fan-de-cinema.com/affiches/large/5e/41239.jpg",
        "action",
        "",
        15123665156144,
        3.2
    ),
    CHICKEN_RUN(
        MovieCategoryEnum.POPULAR,
        "Chicken Run",
        "Animation",
        "2000",
        "http://images.fan-de-cinema.com/affiches/large/51/21474.jpg",
        "action",
        "",
        15123665156144,
        3.2
    ),
    BACK_TO_FUTURE(
        MovieCategoryEnum.POPULAR,
        "Back to the Future",
        "Science Fiction",
        "1985",
        "https://www.filmsfantastiques.com/wp-content/uploads/2014/04/Retour-vers-le-futur-poster-703x1024.jpg",
        "action",
        "",
        15123665156144,
        3.2
    ),
    RAIDERS_OF_THE_LOST_ARK(

        MovieCategoryEnum.POPULAR,
        "Raiders of the Lost Ark",
        "Action & Adventure",
        "1981",
        "http://www.goldposter.com/wp-content/uploads/2015/04/Raiders-of-the-Lost-Ark_poster_goldposter_com_44.jpg",
        "action",
        "",
        15123665156144,
        3.2
    ),
    GOLD_FINGER(
        MovieCategoryEnum.POPULAR,
        "Goldfinger",
        "Action & Adventure",
        "1965",
        "http://images.commeaucinema.com/news/560_123.jpg",
        "action",
        "",
        15123665156144,
        3.2
    ),
    GUARDIANS_OF_THE_GALAXY(
        MovieCategoryEnum.TRENDING,
        "Guardians of the Galaxy",
        "Science Fiction & Fantasy",
        "2014",
        "http://img.filmsactu.net/datas/films/l/e/les-gardiens-de-la-galaxie/xl/les-gardiens-de-la-galaxie-affiche-53b28a79becd7.jpg",
        "action",
        "",
        15123665156144,
        3.2
    );

    fun toMovie(): Movie = Movie(this)

    companion object {
        fun getMovies(): List<Movie> = values().map { Movie(it) }
    }
}