package com.riders.thelab.ui.recycler;

import android.annotation.SuppressLint;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.riders.thelab.core.utils.LabCompatibilityManager;
import com.riders.thelab.ui.base.BasePresenterImpl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
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

        getView().showLoader();

        service
                .getStorageReference(activity)
                .subscribe(
                        storageReference -> {
                            Timber.d("Auth done successfully :%s ", storageReference);

                            // Create a child reference
                            // imagesRef now points to "images"
                            StorageReference artistsRef = storageReference.child("bulk/artists.json");

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
                        },
                        throwable -> {
                            Timber.e(throwable);

                            getView().onJSONURLError();
                        });
    }


    /**
     * Fetch Firebase Storage files and load background image from REST database
     */
    @Override
    public void getFirebaseFiles() {
        Timber.d("getFirebaseFiles()");
        getView().showLoader();

        service
                .getStorageReference(activity)
                .subscribe(
                        storageReference -> {
                            Timber.d("signInAnonymously:success");

                            // Create a child reference
                            // imagesRef now points to "images"
                            StorageReference imagesRef = storageReference.child("images/artists");

                            imagesRef
                                    .listAll()
                                    .addOnSuccessListener(listResult -> {
                                        Timber.d("onSuccess()");
                                    })
                                    .addOnFailureListener(Timber::e)
                                    .addOnCompleteListener(taskResult -> {
                                        if (!taskResult.isSuccessful()) {
                                            Timber.e("error occurred. Please check logs.");
                                        } else {
                                            Timber.d("onComplete() - with size of : %d element(s)", taskResult.getResult().getItems().size());

                                            buildArtistsThumbnailsList(taskResult.getResult().getItems())
                                                    .subscribe(links -> {
                                                        Timber.d("Links : %s", links.toString());

                                                        if (taskResult.getResult().getItems().size() == links.size()) {
                                                            getView().onArtistsThumbnailsSuccessful(links);
                                                        }

                                                    }, throwable -> {
                                                        Timber.e(throwable);
                                                        getView().hideLoader();
                                                        getView().onFetchArtistsError();
                                                    });

                                        }
                                    });
                        },
                        Timber::e);
    }

    private Single<List<String>> buildArtistsThumbnailsList(List<StorageReference> storageReferences) {
        return new Single<List<String>>() {
            @SuppressLint("NewApi")
            @Override
            protected void subscribeActual(@NonNull SingleObserver<? super List<String>> observer) {
                List<String> thumbnailsLinks = new ArrayList<>();
                if (!LabCompatibilityManager.isNougat()) {
                    for (StorageReference element : storageReferences) {
                        element
                                .getDownloadUrl()
                                .addOnSuccessListener(artistThumbUrl -> {
                                    thumbnailsLinks.add(artistThumbUrl.toString());
                                })
                                .addOnFailureListener(throwable -> {
                                    Timber.e(throwable);
                                    observer.onError(throwable);
                                })
                                .addOnCompleteListener(
                                        taskResult -> {
                                            observer.onSuccess(thumbnailsLinks);
                                        });
                    }
                } else {
                    storageReferences
                            .stream()
                            .forEach(itemReference -> {
                                itemReference
                                        .getDownloadUrl()
                                        .addOnSuccessListener(artistThumbUrl -> {
                                            thumbnailsLinks.add(artistThumbUrl.toString());
                                        })
                                        .addOnFailureListener(throwable -> {
                                            Timber.e(throwable);
                                            observer.onError(throwable);
                                        })
                                        .addOnCompleteListener(
                                                task1 -> {
                                                    observer.onSuccess(thumbnailsLinks);
                                                });
                            });
                }

            }
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
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
                            getView().hideLoader();
                            getView().onFetchArtistsError();
                        });
    }

}
