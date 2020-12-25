package com.riders.thelab.ui.mainactivity.fragment.time;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.riders.thelab.R;

import org.jetbrains.annotations.NotNull;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;
import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class TimeFragment extends Fragment {

    @BindView(R.id.tv_time)
    TextView tvTime;

    @BindView(R.id.tv_date)
    TextView tvDate;

    Unbinder unbinder;

    private Thread mThread;


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
        if (null != mThread && !mThread.isInterrupted())
            mThread.interrupt();
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
