package com.riders.thelab.ui.recycler;

import android.annotation.SuppressLint;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.riders.thelab.data.remote.LabService;
import com.riders.thelab.ui.base.BasePresenterImpl;

import javax.inject.Inject;

import timber.log.Timber;


@SuppressLint("CheckResult")
public class RecyclerViewPresenter extends BasePresenterImpl<RecyclerViewView>
        implements RecyclerViewContract.Presenter {

    @Inject
    RecyclerViewActivity activity;

    @Inject
    LabService service;

    private FirebaseAuth mAuth;
    private FirebaseStorage storage;


    @Inject
    RecyclerViewPresenter() {
    }

    @Override
    public void getFirebaseJSONURL() {

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mAuth
                .signInAnonymously()
                .addOnCompleteListener(
                        activity,
                        task -> {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Timber.d("signInAnonymously:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                String bucketName = "gs://the-lab-3920e.appspot.com";

                                storage = FirebaseStorage.getInstance(bucketName);
                                // Create a storage reference from our app
                                StorageReference storageRef = storage.getReference();

                                // Create a child reference
                                // imagesRef now points to "images"
                                StorageReference artistsRef = storageRef.child("bulk/artists.json");

                                artistsRef
                                        .getDownloadUrl()
                                        .addOnCompleteListener(artistTask -> {
                                            Timber.d("result : %s", artistTask.getResult().toString());

                                            String result = artistTask.getResult().toString();
                                            String url = "";

                                            try {
                                                url = result.replace("%3D", "?");

                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                            }

                                            getView().onJSONURLFetched(url);
                                        });
                            } else {
                                // If sign in fails, display a message to the user.
                                Timber.w("signInAnonymously:failure %s", task.getException());
                                Toast.makeText(
                                        activity,
                                        "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                                getView().onJSONURLError();
                            }
                        });

    }

    @Override
    public void fetchArtists(String urlPath) {
        Timber.d("fetchArtists()");
        service
                .getArtists(urlPath)
                .subscribe(
                        artists -> {
                            getView().onFetchArtistsSuccessful(artists);
                        }, throwable -> {
                            Timber.e(throwable);
                            getView().onFetchArtistsError();
                        });
    }

}
