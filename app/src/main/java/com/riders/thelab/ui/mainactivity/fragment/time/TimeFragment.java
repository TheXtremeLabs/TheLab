package com.riders.thelab.ui.mainactivity.fragment.time;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.riders.thelab.R;
import com.riders.thelab.core.utils.LabCompatibilityManager;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class TimeFragment extends Fragment {

    @BindView(R.id.iv_time_background)
    ShapeableImageView ivBackground;

    @BindView(R.id.tv_time)
    MaterialTextView tvTime;

    @BindView(R.id.tv_date)
    MaterialTextView tvDate;

    Unbinder unbinder;

    private Thread mThread;

    private FirebaseAuth mAuth;
    private FirebaseStorage storage;


    public static TimeFragment newInstance() {
        return new TimeFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_time, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
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
                        getActivity().runOnUiThread(() -> {
                            LocalTime localTime = LocalTime.now();
                            tvTime.setText(localTime.format(DateTimeFormatter.ofPattern("HH:mm")));
                        });
                    }
                } catch (InterruptedException e) {
                    Timber.e(e);
                }
            }
        };
        mThread.start();

        LocalDate localDate = LocalDate.now();
        tvDate.setText(localDate.format(DateTimeFormatter.ofPattern("EEE, d MMM yyyy")));

        if (!LabCompatibilityManager.isTablet(requireActivity()))
            getFirebaseFiles();
        else {
            tvTime.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white));
            tvDate.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white));
        }
    }


    /**
     * Fetch Firebase Storage files and load background image from REST database
     */
    public void getFirebaseFiles() {
        Timber.d("getFirebaseFiles()");

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        mAuth
                .signInAnonymously()
                .addOnCompleteListener(
                        requireActivity(),
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
                                StorageReference imagesRef = storageRef.child("images/dark_theme");

                                imagesRef.list(5)
                                        .addOnSuccessListener(listResult -> {
                                            Timber.d("onSuccess()");

                                            int max = listResult.getItems().size();
                                            Random random = new Random();

                                            // Get random int
                                            int iRandom = random.nextInt(max);

                                            // Get item url using random int
                                            StorageReference item = listResult.getItems().get(iRandom);

                                            // Make rest call
                                            item.getDownloadUrl().addOnSuccessListener(uri -> {

                                                // Display image
                                                Glide.with(requireActivity())
                                                        .load(uri.toString())
                                                        .into(ivBackground);
                                            });
                                        })
                                        .addOnFailureListener(Timber::e)
                                        .addOnCompleteListener(task1 -> Timber.d("onComplete() - %d ", task1.getResult().getItems().size()));

                            } else {
                                // If sign in fails, display a message to the user.
                                Timber.w("signInAnonymously:failure %s", task.getException().toString());
                                Toast.makeText(
                                        getActivity(),
                                        "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

    }

    @Override
    public void onDestroyView() {
        Timber.d("onDestroyView()");
        if (null != unbinder)
            unbinder.unbind();

        if (null != mThread) {
            mThread.interrupt();
            mThread = null;
        }
        super.onDestroyView();
    }
}
