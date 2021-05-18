package com.riders.thelab.ui.mainactivity.fragment.time;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.StorageReference;
import com.riders.thelab.core.utils.LabCompatibilityManager;
import com.riders.thelab.databinding.FragmentTimeBinding;

import org.jetbrains.annotations.NotNull;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.Random;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class TimeFragment extends Fragment {

    FragmentTimeBinding viewBinding;

    private Thread mThread;

    private final CompositeDisposable compositeDisposable;

    @Inject
    LabService service;


    public TimeFragment() {
        compositeDisposable = new CompositeDisposable();
    }

    public static TimeFragment newInstance() {
        return new TimeFragment();
    }


    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        AndroidSupportInjection.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewBinding = FragmentTimeBinding.inflate(inflater, container, false);
        return viewBinding.getRoot();
    }

    @Override
    public void onPause() {
        if (null != mThread && !mThread.isInterrupted()) {
            mThread.interrupt();
            mThread = null;
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mThread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        requireActivity().runOnUiThread(() -> {
                            LocalTime localTime = LocalTime.now();
                            viewBinding.tvTime.setText(
                                    localTime.format(DateTimeFormatter.ofPattern("HH:mm")));
                        });
                    }
                } catch (InterruptedException e) {
                    Timber.e(e);
                }
            }
        };
        mThread.start();

        LocalDate localDate = LocalDate.now();
        viewBinding.tvDate.setText(localDate.format(DateTimeFormatter.ofPattern("EEE, d MMM yyyy")));

        if (!LabCompatibilityManager.isTablet(requireActivity()))
            getFirebaseFiles();
    }


    /**
     * Fetch Firebase Storage files and load background image from REST database
     */
    public void getFirebaseFiles() {
        Timber.d("getFirebaseFiles()");

        Disposable disposable =
                service.getStorageReference(requireActivity())
                        .subscribe(storageReference -> {
                            // Create a child reference
                            // imagesRef now points to "images"
                            StorageReference imagesRef = storageReference.child("images/dark_theme");

                            imagesRef.list(5)
                                    .addOnSuccessListener(listResult -> {
                                        Timber.d("onSuccess()");

                                        int max = listResult.getItems().size();

                                        // Get random int
                                        int iRandom = new Random().nextInt(max);

                                        // Get item url using random int
                                        StorageReference item =
                                                listResult.getItems().get(iRandom);

                                        // Make rest call
                                        item
                                                .getDownloadUrl()
                                                .addOnSuccessListener(uri ->

                                                        // Display image
                                                        Glide.with(requireActivity())
                                                                .load(uri.toString())
                                                                .into(viewBinding.ivTimeBackground)
                                                );
                                    })
                                    .addOnFailureListener(Timber::e)
                                    .addOnCompleteListener(task1 ->
                                            Timber.d(
                                                    "onComplete() - %d ",
                                                    task1.getResult().getItems().size()));
                        }, Timber::e);

        compositeDisposable.add(disposable);
    }

    @Override
    public void onDestroyView() {
        Timber.d("onDestroyView()");

        if (null != mThread) {
            mThread.interrupt();
            mThread = null;
        }

        compositeDisposable.clear();

        super.onDestroyView();
        viewBinding = null;
    }
}
