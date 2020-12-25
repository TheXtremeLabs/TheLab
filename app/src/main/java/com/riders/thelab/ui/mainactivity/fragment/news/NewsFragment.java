package com.riders.thelab.ui.mainactivity.fragment.news;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.riders.thelab.R;
import com.riders.thelab.data.local.model.App;
import com.riders.thelab.ui.mainactivity.MainActivityAppClickListener;
import com.riders.thelab.utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.AndroidSupportInjection;
import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class NewsFragment extends Fragment {

    private Context context;

    @BindView(R.id.rv_News)
    RecyclerView rvNews;

    Unbinder unbinder;

    private List<App> recentApps;
    private String[] recentAppsNames = new String[]{"Recycler", "Biometric", "Widget"};

    /**
     * passing data between fragments
     */
    private MainActivityAppClickListener listener;

    @Inject
    public NewsFragment() {
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        AndroidSupportInjection.inject(this);

        if (context instanceof MainActivityAppClickListener) {
            listener = (MainActivityAppClickListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement MainActivityAppClickListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        Timber.d("onViewCreated()");

        recentApps = new ArrayList<>();

        // Setup last 3 features added
        for (App element : Constants.getInstance().getActivityList()) {
            for (String item : recentAppsNames) {
                if (element.getTitle().contains(item))
                    recentApps.add(element);
            }
        }

        Timber.e("Recent apps");
        for (App app : recentApps) {
            Timber.e(app.toString());
        }

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.d("onResume()");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rvNews.setLayoutManager(linearLayoutManager);
        NewsAdapter adapter = new NewsAdapter(context, recentApps, listener);
        rvNews.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        Timber.d("onDestroyView()");
        if (null != unbinder)
            unbinder.unbind();
        super.onDestroyView();
    }
}
