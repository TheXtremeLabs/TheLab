package com.riders.thelab.ui.mainactivity.fragment.time;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.riders.thelab.R;

import org.jetbrains.annotations.NotNull;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;
import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class TimeFragment extends Fragment {

    @BindView(R.id.iv_time_background)
    AppCompatImageView ivBackground;

    @BindView(R.id.tv_time)
    TextView tvTime;

    @BindView(R.id.tv_date)
    TextView tvDate;

    Unbinder unbinder;

    private Thread mThread;

    private FirebaseAuth mAuth;
    private FirebaseStorage storage;


    public TimeFragment() {
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        AndroidSupportInjection.inject(this);
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

        getFirebaseFiles();

    }


    public void getFirebaseFiles() {
        Timber.d("getFirebaseFiles()");

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        mAuth
                .signInAnonymously()
                .addOnCompleteListener(
                        getActivity(),
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
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
                                    StorageReference imagesRef = storageRef.child("images");

                                    imagesRef.list(5)
                                            .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                                                @Override
                                                public void onSuccess(ListResult listResult) {
                                                    Timber.d("onSuccess()");
                                                    /*for (StorageReference prefix : listResult.getPrefixes()) {
                                                        // All the prefixes under listRef.
                                                        // You may call listAll() recursively on them.
                                                    }*/



                                                    /*for (StorageReference item : listResult.getItems()) {
                                                        // All the items under listRef.

                                                        item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                            @Override
                                                            public void onSuccess(Uri uri) {
                                                                Timber.d("URL : %s", uri.toString());
                                                                //uri.getScheme() + "://" + uri.getAuthority() + "/" + uri.getPath()

                                                                //String builtURL = uri.getScheme() + "://" + uri.getAuthority() + "/" + uri.getPath();

                                                                String[] imageURL = uri.toString().split(Pattern.quote("?"));
                                                                // Build url
                                                                Timber.e("imageURL from firebase :  %s", imageURL[0]);

                                                                try {
                                                                    URL url = new URL(URLDecoder.decode(imageURL[0], "UTF-8"));
                                                                    Timber.e("Final URL :  %s", url.toString());
                                                                } catch (MalformedURLException e) {
                                                                    e.printStackTrace();
                                                                } catch (UnsupportedEncodingException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        });
                                                    }*/


                                                    int max = listResult.getItems().size();
                                                    Random random = new Random();

                                                    // Get random int
                                                    int iRandom = random.nextInt(max);

                                                    // Get item url using random int
                                                    StorageReference item = listResult.getItems().get(iRandom);

                                                    // Make rest call
                                                    item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {

                                                            // Display image
                                                            Glide.with(getActivity())
                                                                    .load(uri.toString())
                                                                    .into(ivBackground);
                                                        }
                                                    });
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Timber.e(e);
                                                }
                                            })
                                            .addOnCompleteListener(new OnCompleteListener<ListResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<ListResult> task) {
                                                    Timber.d("onComplete() - " + task.getResult().getItems().size());
                                                }
                                            });

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Timber.w("signInAnonymously:failure %s", task.getException());
                                    Toast.makeText(
                                            getActivity(),
                                            "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
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
