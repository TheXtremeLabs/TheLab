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

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.riders.thelab.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class NewsFragment extends Fragment {

    private final String[] recentAppsNames = new String[]{"Chat", "Spring", "Weather"};

    // Card View 1
    @BindView(R.id.news_card_view_1)
    MaterialCardView cardView1;
    @BindView(R.id.news_image_view_1)
    ShapeableImageView imageView1;
    @BindView(R.id.news_text_view_1)
    MaterialTextView textView1;

    // Card View 1
    @BindView(R.id.news_card_view_2)
    MaterialCardView cardView2;
    @BindView(R.id.news_image_view_2)
    ShapeableImageView imageView2;
    @BindView(R.id.news_text_view_2)
    MaterialTextView textView2;

    // Card View 1
    @BindView(R.id.news_card_view_3)
    MaterialCardView cardView3;
    @BindView(R.id.news_image_view_3)
    ShapeableImageView imageView3;
    @BindView(R.id.news_text_view_3)
    MaterialTextView textView3;


    Unbinder unbinder;
    private Context context;
    private List<App> recentApps;
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

        setupCards(recentApps);
    }

    @Override
    public void onDestroyView() {
        Timber.d("onDestroyView()");
        if (null != unbinder)
            unbinder.unbind();
        super.onDestroyView();
    }

    @OnClick({R.id.news_card_view_1, R.id.news_card_view_2, R.id.news_card_view_3})
    public void onCardClicked(View view) {
        switch (view.getId()) {
            case R.id.news_card_view_1:
                listener.onAppItemCLickListener(view, recentApps.get(0), 0);
                break;

            case R.id.news_card_view_2:
                listener.onAppItemCLickListener(view, recentApps.get(1), 1);
                break;

            case R.id.news_card_view_3:
                listener.onAppItemCLickListener(view, recentApps.get(2), 2);
                break;
        }
    }

    private void setupCards(final List<App> appList) {

        for (int i = 0; i < appList.size(); i++) {
            if (i == 0) {
                bindData(imageView1, textView1, appList.get(i));
            }
            if (i == 1) {

                bindData(imageView2, textView2, appList.get(i));
            }
            if (i == 2) {

                bindData(imageView3, textView3, appList.get(i));
            }
        }
    }

    private void bindData(final ShapeableImageView imageView,
                          final MaterialTextView textView,
                          final App app) {

        if (app.getDrawableIcon() != null) {
            imageView.setBackgroundDrawable(app.getDrawableIcon());
        } else {
            imageView.setBackgroundResource(app.getIcon());
        }

        textView.setText(
                app.getName() != null
                        ? app.getName()
                        : app.getTitle());
    }
}
