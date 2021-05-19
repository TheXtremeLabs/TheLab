package com.riders.thelab.ui.recycler;

import com.riders.thelab.data.remote.dto.artist.Artist;

import java.util.List;

public interface RecyclerViewContract {

    interface View extends BaseView {

        void onPause();

        void onResume();

        void showLoader();

        void hideLoader();

        void onJSONURLFetched(String url);

        void onJSONURLError();

        void onArtistsThumbnailsSuccessful(List<String> artistThumbnails);

        void onArtistsThumbnailsError();

        void onFetchArtistsSuccessful(List<Artist> listOfArtists);

        void onFetchArtistsError();
    }

    interface Presenter {

        void getFirebaseJSONURL();

        void getFirebaseFiles();

        void fetchArtists(String urlPath);
    }
}
