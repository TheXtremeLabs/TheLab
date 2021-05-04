package com.riders.thelab.ui.recycler;

import com.riders.thelab.data.remote.dto.Artist;
import com.riders.thelab.ui.base.BaseView;

import java.util.List;

public interface RecyclerViewContract {

    interface View extends BaseView {

        void onPause();

        void onResume();

        void showLoader();

        void hideLoader();

        void onJSONURLFetched(String url);

        void onJSONURLError();

        void onFetchArtistsSuccessful(List<Artist> listOfArtists);

        void onFetchArtistsError();
    }

    interface Presenter {

        void getFirebaseJSONURL();

        void fetchArtists(String urlPath);
    }
}
