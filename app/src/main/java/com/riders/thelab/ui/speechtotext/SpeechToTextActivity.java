package com.riders.thelab.ui.speechtotext;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.content.ContextCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.riders.thelab.R;
import com.riders.thelab.ui.base.SimpleActivity;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class SpeechToTextActivity extends SimpleActivity
        implements RecognitionListener {

    // TAG & Context
    private Context context;

    // Views
    @BindView(R.id.speech_input_textView)
    TextView inputTextView;

    @BindView(R.id.speech_button)
    AppCompatImageButton speechButton;

    @BindView(R.id.recording_textView)
    TextView recordingPlaceholderTextView;

    @BindView(R.id.eq_imageView)
    ImageView eqImageView;

    // Speech
    SpeechRecognizer speech;
    Intent recognizerIntent;
    String message;
    float currentFloatDB = 0;


    ///////////////////////////////////////////
    //
    //  Override methods
    //
    ///////////////////////////////////////////
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_to_text);

        context = this;
        ButterKnife.bind(this);

        // Check permission
        Dexter.withContext(context)
                .withPermission(Manifest.permission.RECORD_AUDIO)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                        Timber.d("permission granted");

                        // Check if speech to text is available on device


                        // Init Speech To Text Variables
                        initSpeechToText();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                        Timber.d("permission denied");
                        SpeechToTextActivity.this.finish();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .check();
    }

    @Override
    protected void onDestroy() {
        if (speech != null)
            speech.stopListening();
        super.onDestroy();
    }
    ///////////////////////////////////////////
    //
    //  Override methods
    //
    ///////////////////////////////////////////


    ///////////////////////////////////////////
    //
    //  Butterknife methods
    //
    ///////////////////////////////////////////
    @OnClick(R.id.speech_button)
    public void onSpeechButtonClicked() {
        Timber.e("onSpeechClicked()");

        if (!inputTextView.getText().toString().isEmpty())
            inputTextView.setText("");

        if (!isRecordingViewVisible()) {
            // Display Recording view
            showRecordingView();
            speechButton.setColorFilter(ContextCompat.getColor(context, R.color.teal_700));

            showEq();
        }


        Timber.i("start listening ");
        speech.startListening(recognizerIntent);
    }
    ///////////////////////////////////////////
    //
    //  Butterknife methods
    //
    ///////////////////////////////////////////


    ///////////////////////////////////////////
    //
    //  Class methods
    //
    ///////////////////////////////////////////

    /**
     * Init speech to text variables in order to use it
     */
    private void initSpeechToText() {

        speech = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);

        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());

        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
    }


    public boolean isRecordingViewVisible() {
        if (View.VISIBLE == recordingPlaceholderTextView.getVisibility())
            return true;
        else if (View.INVISIBLE == recordingPlaceholderTextView.getVisibility())
            return false;

        return false;
    }


    public void showRecordingView() {
        recordingPlaceholderTextView.setVisibility(View.VISIBLE);
    }


    public void hideRecordingView() {
        recordingPlaceholderTextView.setVisibility(View.INVISIBLE);
    }


    public void showEq() {
        eqImageView.setVisibility(View.VISIBLE);
    }


    public void hideEq() {
        eqImageView.setVisibility(View.INVISIBLE);
    }
    ///////////////////////////////////////////
    //
    //  Class methods
    //
    ///////////////////////////////////////////


    ///////////////////////////////////////////
    //
    //  Listeners methods
    //
    ///////////////////////////////////////////
    @Override
    public void onReadyForSpeech(Bundle params) {
        Timber.e("onReadyForSpeech()");
    }

    @Override
    public void onBeginningOfSpeech() {
        Timber.i("onBeginningOfSpeech()");

    }

    @Override
    public void onRmsChanged(float rmsdB) {

        //Check if eqView is visible
        if (eqImageView.getVisibility() == View.VISIBLE) {

            // Variables
            int min = -50;
            int toZero = 0;
            int max = 50;
            Random random = new Random();

            // Compare current dB value with the next one in order to set random value (negative/positive)
            if (currentFloatDB > rmsdB) {
                // Random negative
                int negRandom = random.nextInt(toZero - min) + toZero;
                eqImageView.getLayoutParams().height = eqImageView.getLayoutParams().height - negRandom;

            } else if (currentFloatDB < rmsdB) {
                // Random positive
                int posRandom = random.nextInt(max + toZero) + toZero;
                eqImageView.getLayoutParams().height = eqImageView.getLayoutParams().height + posRandom;
            }

            // eqImageView.getLayoutParams().height = eqImageView.getLayoutParams().height + random;
            eqImageView.requestLayout();

            // Store current dB value
            currentFloatDB = rmsdB;

        }
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Timber.d("onBufferReceived() : %s", buffer);
    }

    @Override
    public void onEndOfSpeech() {
        Timber.d("onEndOfSpeech()");
    }

    @Override
    public void onError(int error) {
        Timber.e("FAILED %s", error);

        switch (error) {

            case SpeechRecognizer.ERROR_AUDIO:
                message = getString(R.string.error_audio_error);
                break;

            case SpeechRecognizer.ERROR_CLIENT:
                message = getString(R.string.error_client);
                break;

            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = getString(R.string.error_permission);
                break;

            case SpeechRecognizer.ERROR_NETWORK:
                message = getString(R.string.error_network);
                break;

            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = getString(R.string.error_timeout);
                break;

            case SpeechRecognizer.ERROR_NO_MATCH:
                message = getString(R.string.error_no_match);
                break;

            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = getString(R.string.error_busy);
                break;

            case SpeechRecognizer.ERROR_SERVER:
                message = getString(R.string.error_server);
                break;

            default:
                message = getString(R.string.error_understand);
                break;
        }

        Timber.e(message);

        if (isRecordingViewVisible()) {
            // Hide Recording view
            hideRecordingView();
            speechButton.setColorFilter(ContextCompat.getColor(context, R.color.white));
            hideEq();
        }

        inputTextView.setText(message);
    }

    @Override
    public void onResults(Bundle results) {
        Timber.e("onResults()");

        String result = "";

        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        for (String element : matches)
            Timber.d(element);

        Timber.d("User said : %s", matches.get(0));

        if (isRecordingViewVisible()) {
            // Hide Recording view
            hideRecordingView();
            speechButton.setColorFilter(ContextCompat.getColor(context, R.color.white));
            hideEq();
        }

        result = matches.get(0);
        inputTextView.setText(result);
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        Timber.i("onPartialResults()");
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        Timber.i("onEvent()");
    }
}
