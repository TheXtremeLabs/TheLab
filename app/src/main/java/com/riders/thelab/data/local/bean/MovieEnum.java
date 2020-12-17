package com.riders.thelab.data.local.bean;

import com.riders.thelab.data.local.model.Movie;

import java.util.ArrayList;
import java.util.List;

public enum MovieEnum {

    MAD_MAX("Mad Max: Fury Road", "Action & Adventure", "2015", "http://www.lyricis.fr/wp-content/uploads/2015/04/Mad-Max-Fury-Road-Affiche-Finale-US.jpg"),
    INSIDE_OUT("Inside Out", "Animation, Kids & Family", "2015", "http://marvelll.fr/wp-content/uploads/2015/06/vice-versa-inside-out-film-2015-affiche-france.jpg"),
    STAR_WARS("Star Wars: Episode VII - The Force Awakens", "Action", "2015", "http://s2.lemde.fr/image/2015/10/19/534x0/4792408_6_5a54_2015-10-19-9644df6-23620-irycre_2fc3ea95cb36078aadd9728153e377ea.jpg"),
    SHAUN_THE_SHEEP("Shaun the Sheep", "Animation", "2015", "http://marvelll.fr/wp-content/uploads/2015/04/Shaun-le-Mouton-le-Film-Affiche.jpg"),
    THE_MARTIAN("The Martian", "Science Fiction & Fantasy", "2015", "http://fr.web.img3.acsta.net/pictures/15/09/08/15/20/305329.jpg"),
    STAR_TREK("Mission: Impossible Rogue Nation", "Action", "2015", "http://www.avoir-alire.com/IMG/jpg/starr_grd.jpg"),
    UP("Up", "Animation", "2009", "http://www.walle.free.fr/up/affiche_fr_big.jpg"),
    MI_ROGUE_NATION("Star Trek", "Science Fiction", "2009", "http://www.addictomovie.com/wp-content/uploads/2015/06/mi5_affiche_11.jpg"),
    LEGO("The LEGO Movie", "Animation", "2014", "http://www.lyricis.fr/wp-content/uploads/2013/11/The-LEGO-Movie-Affiche-USA-2.jpg"),
    IRON_MAN("Iron Man", "Action & Adventure", "2008", "http://www.chroniquedisney.fr/imgFiliale/marvel/2008-ironman1-1-big.jpg"),
    ALIENS("Aliens", "Science Fiction", "1986", "http://images.fan-de-cinema.com/affiches/large/5e/41239.jpg"),
    CHICKEN_RUN("Chicken Run", "Animation", "2000", "http://images.fan-de-cinema.com/affiches/large/51/21474.jpg"),
    BACK_TO_FUTURE("Back to the Future", "Science Fiction", "1985", "http://www.spasm.ca/wp-content/uploads/2015/05/Affiche-BackToTheFuture.jpg"),
    RAIDERS_OF_THE_LOST_ARK("Raiders of the Lost Ark", "Action & Adventure", "1981", "http://www.goldposter.com/wp-content/uploads/2015/04/Raiders-of-the-Lost-Ark_poster_goldposter_com_44.jpg"),
    GOLD_FINGER("Goldfinger", "Action & Adventure", "1965", "http://images.commeaucinema.com/news/560_123.jpg"),
    GUARDIANS_OF_THE_GALAXY("Guardians of the Galaxy", "Science Fiction & Fantasy", "2014", "http://img.filmsactu.net/datas/films/l/e/les-gardiens-de-la-galaxie/xl/les-gardiens-de-la-galaxie-affiche-53b28a79becd7.jpg");


    private final String title;
    private final String genre;
    private final String year;
    private final String url;

    MovieEnum(String title, String genre, String year, String url) {
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public String getYear() {
        return year;
    }

    public String getUrl() {
        return url;
    }

    public static List<Movie> getMovies() {
        List<Movie> list = new ArrayList<>();

        for (MovieEnum item : values()) {
            list.add(new Movie(item));
        }

        return list;
    }
}
