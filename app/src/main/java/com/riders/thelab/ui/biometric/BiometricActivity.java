package com.riders.thelab.ui.biometric;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.riders.thelab.R;
import com.riders.thelab.core.utils.LabCompatibilityManager;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.infinum.goldfinger.Goldfinger;
import co.infinum.goldfinger.crypto.CipherCrypter;
import co.infinum.goldfinger.crypto.CipherFactory;
import co.infinum.goldfinger.crypto.impl.AesCipherFactory;
import co.infinum.goldfinger.crypto.impl.Base64CipherCrypter;
import co.infinum.goldfinger.rx.RxGoldfinger;
import io.reactivex.observers.DisposableObserver;
import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class BiometricActivity extends AppCompatActivity {

    @BindView(R.id.finger_print_btn)
    ImageButton fingerPrintButton;
    Goldfinger goldFinger;
    Goldfinger.PromptParams params;
    // RX approach
    RxGoldfinger rxGoldFinger;
    Goldfinger.PromptParams rxParams;
    private Context context;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biometric);

        init();

        if (LabCompatibilityManager.isMarshmallow()) {
            initGoldFinger();
        }

        // Check if fingerprint hardware is available
        if (checkFingerPrintAvailability()) {
            Timber.d("Fingerprint hardware ok");

        } else {
            Timber.e("The device doesn't have finger print hardware");
        }

        // Authenticate
        if (goldFinger.canAuthenticate()) {
            /* Authenticate */
            Timber.d("Authenticate");

            authenticate();
        } else {
            Timber.e("Cannot authenticate");
        }
    }

    private void init() {

        context = this;

        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.activity_title_biometric));
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initGoldFinger() {

        // fingerprint instantiation
        goldFinger = new Goldfinger.Builder(context).build();

        // fingerprint prompt instantiation
        params = new Goldfinger.PromptParams.Builder(this)
                .title("Title")
                .negativeButtonText("Cancel")
                .description("Description")
                .subtitle("Subtitle")
                .build();


        CipherFactory factory = new AesCipherFactory(context);
        CipherCrypter crypter = new Base64CipherCrypter();

        rxGoldFinger = new RxGoldfinger.Builder(context)
                .logEnabled(true)
                .cipherFactory(factory)
                .cipherCrypter(crypter)
                .build();
    }

    private boolean checkFingerPrintAvailability() {
        return goldFinger.hasFingerprintHardware();
    }

    private void authenticate() {
        Timber.d("authenticate()");
        goldFinger
                .authenticate(params, new Goldfinger.Callback() {
                    @Override
                    public void onError(@NonNull Exception e) {
                        /* Critical error happened */
                        Timber.e(e);
                    }

                    @Override
                    public void onResult(@NonNull Goldfinger.Result result) {
                        /* Result received */

                        Timber.d(
                                "Result :\n" +
                                        "type : " + result.type() + "\n" +
                                        "value : " + result.value() + "\n" +
                                        "reason : " + result.reason() + "\n" +
                                        "message : " + result.message() + "\n" +
                                        "");
                    }
                });

    }

    private void authenticateWithRX() {
        Timber.d("authenticateWithRX()");
        rxGoldFinger
                .authenticate(params)
                .subscribe(new DisposableObserver<Goldfinger.Result>() {

                    @Override
                    public void onComplete() {
                        /* Fingerprint authentication is finished */
                        Timber.d("Authentication complete");
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        /* Critical error happened */
                        Timber.e(e);
                    }

                    @Override
                    public void onNext(Goldfinger.@NotNull Result result) {
                        /* Result received */

                        Timber.d(
                                "Result :\n" +
                                        "type : " + result.type() + "\n" +
                                        "value : " + result.value() + "\n" +
                                        "reason : " + result.reason() + "\n" +
                                        "message : " + result.message() + "\n" +
                                        "");
                    }
                });
    }

    @OnClick(R.id.finger_print_btn)
    public void onFingerPrintClicked() {
        Timber.d("on fingerprint button clicked");
        authenticateWithRX();
    }
}
