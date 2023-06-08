package com.riders.thelab.data.local.bean

import com.riders.thelab.data.local.model.Movie

enum class MovieEnum(val title: String, val genre: String, val year: String, val url: String) {

    MAD_MAX(
        "Mad Max: Fury Road",
        "Action & Adventure",
        "2015",
        "https://confluences.hypotheses.org/files/2023/01/mad-max-affiche-2.jpg"
    ),
    INSIDE_OUT(
        "Inside Out",
        "Animation, Kids & Family",
        "2015",
        "http://marvelll.fr/wp-content/uploads/2015/06/vice-versa-inside-out-film-2015-affiche-france.jpg"
    ),
    STAR_WARS(
        "Star Wars: Episode VII - The Force Awakens",
        "Action",
        "2015",
        "https://cdn.shopify.com/s/files/1/1057/4964/products/Star-Wars-The-Force-Awakens-Vintage-Movie-Poster-Original-1-Sheet-27x41-7308_2000x.progressive.jpg?v=1666911067"
    ),
    SHAUN_THE_SHEEP(
        "Shaun the Sheep",
        "Animation",
        "2015",
        "http://marvelll.fr/wp-content/uploads/2015/04/Shaun-le-Mouton-le-Film-Affiche.jpg"
    ),
    THE_MARTIAN(
        "The Martian",
        "Science Fiction & Fantasy",
        "2015",
        "http://fr.web.img3.acsta.net/pictures/15/09/08/15/20/305329.jpg"
    ),
    MI_ROGUE_NATION(
        "Mission: Impossible Rogue Nation",
        "Action",
        "2015",
        "https://fusion.molotov.tv/arts/i/446x588/Ch8SHQoUv-wmMqUTaFFU-z8fiyvejhkSwR0SA2pwZxgB/jpg"
    ),
    UP("Up", "Animation", "2009", "http://www.walle.free.fr/up/affiche_fr_big.jpg"),
    STAR_TREK(
        "Star Trek",
        "Science Fiction",
        "2009",
        "http://www.avoir-alire.com/IMG/jpg/starr_grd.jpg"
    ),
    LEGO(
        "The LEGO Movie",
        "Animation",
        "2014",
        "https://m.media-amazon.com/images/I/71rH-TN3BnL._AC_SY445_.jpg"
    ),
    IRON_MAN(
        "Iron Man",
        "Action & Adventure",
        "2008",
        "http://www.chroniquedisney.fr/imgFiliale/marvel/2008-ironman1-1-big.jpg"
    ),
    ALIENS(
        "Aliens",
        "Science Fiction",
        "1986",
        "http://images.fan-de-cinema.com/affiches/large/5e/41239.jpg"
    ),
    CHICKEN_RUN(
        "Chicken Run",
        "Animation",
        "2000",
        "http://images.fan-de-cinema.com/affiches/large/51/21474.jpg"
    ),
    BACK_TO_FUTURE(
        "Back to the Future",
        "Science Fiction",
        "1985",
        "https://www.filmsfantastiques.com/wp-content/uploads/2014/04/Retour-vers-le-futur-poster-703x1024.jpg"
    ),
    RAIDERS_OF_THE_LOST_ARK(
        "Raiders of the Lost Ark",
        "Action & Adventure",
        "1981",
        "http://www.goldposter.com/wp-content/uploads/2015/04/Raiders-of-the-Lost-Ark_poster_goldposter_com_44.jpg"
    ),
    GOLD_FINGER(
        "Goldfinger",
        "Action & Adventure",
        "1965",
        "http://images.commeaucinema.com/news/560_123.jpg"
    ),
    GUARDIANS_OF_THE_GALAXY(
        "Guardians of the Galaxy",
        "Science Fiction & Fantasy",
        "2014",
        "http://img.filmsactu.net/datas/films/l/e/les-gardiens-de-la-galaxie/xl/les-gardiens-de-la-galaxie-affiche-53b28a79becd7.jpg"
    );

    fun toMovie(): Movie = Movie(this)

    companion object {
        fun getMovies(): List<Movie> = values().map { Movie(it) }
    }
}