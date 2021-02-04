package com.riders.thelab.ui.customtoast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.riders.thelab.R;
import com.riders.thelab.core.views.TheLabToast;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class CustomToastActivity extends AppCompatActivity {

    Context context;

    @BindView(R.id.display_toast_btn)
    MaterialButton displayToastButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window w = getWindow();
        w.setStatusBarColor(Color.TRANSPARENT);

        setContentView(R.layout.activity_custom_toast);

        context = this;

        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.activity_title_custom_toast));
    }

    @OnClick(R.id.display_toast_btn)
    public void onDisplayToastButtonClicked() {

        displayCustomToastUsingClass();

//        displayCustomToastGoogleDeveloperBasicImplementation();

    }

    private void displayCustomToastUsingClass() {

        TheLabToast theLabToast = new TheLabToast(context);

        theLabToast.setText("testing a new text");

        int random = new Random().nextInt(TheLabToast.ToastTypeEnum.values().length);
        Timber.d("random : %s", random);
/*
        theLabToast.setType(TheLabToast.ToastTypeEnum.values()[random]);
        theLabToast.show();*/

        new TheLabToast.Builder(context)
                .setText("hello")
                .setType(TheLabToast.ToastTypeEnum.values()[random])
                .show();
    }

    private void displayCustomToastGoogleDeveloperBasicImplementation() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(
                R.layout.custom_toast_layout,
                (ViewGroup) findViewById(R.id.custom_toast_container));

        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText("This is a custom toast");

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 250);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();

    }
}
