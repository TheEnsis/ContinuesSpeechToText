package com.example.nut.speechtyping.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Outline;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.nut.speechtyping.R;
import com.example.nut.speechtyping.sql.DatabaseManager;
import com.example.nut.speechtyping.util.PermissionHandler;
import com.inthecheesefactory.thecheeselibrary.manager.Contextor;

import java.util.ArrayList;


/**
 * Created by nuuneoi on 11/16/2014.
 */
@SuppressWarnings("unused")
public class FirstTabFragment extends Fragment implements View.OnClickListener, RecognitionListener {

    private EditText edMsg;
    private ImageView ivRecord;
    private ImageView ivStop;
    private ProgressBar progressBar;
    private String savingMessage;
    private String myLanguage = "th";
    private DatabaseManager dbManager;

    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private String LOG_TAG = "STT";
    private AudioManager audioManager;

    private boolean isCountDownOn;
    private boolean isPause = true;

    public void setMyLanguage(String myLanguage) {
        this.myLanguage = myLanguage;
    }

    public FirstTabFragment() {
        super();
    }

    public static FirstTabFragment newInstance() {
        FirstTabFragment fragment = new FirstTabFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab_first, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        // Init Fragment level's variable(s) here
        dbManager = new DatabaseManager();
        dbManager.open();

        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, myLanguage);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getContext().getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 5000);

        speech = SpeechRecognizer.createSpeechRecognizer(getContext());
        speech.setRecognitionListener(this);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initInstances(View rootView, Bundle savedInstanceState) {
        edMsg = (EditText) rootView.findViewById(R.id.edMessage);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        ivRecord = (ImageView) rootView.findViewById(R.id.ivRec);
        ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                // Or read size directly from the view's width/height
                int size = getResources().getDimensionPixelSize(R.dimen.fab_size);
                outline.setOval(0, 0, size, size);
            }
        };
        ivRecord.setOutlineProvider(viewOutlineProvider);
        ivRecord.setOnClickListener(this);

        ivStop = (ImageView) rootView.findViewById(R.id.ivStop);
        ivStop.setOnClickListener(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        if (speech != null) {
            speech.cancel();
            //Log.i(LOG_TAG, "onPause");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        //Log.i(LOG_TAG, "onStart");
    }

    @Override
    public void onStop() {
        super.onStop();

        if (speech != null) {
            speech.destroy();
            //Log.i(LOG_TAG, "onStop");

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
    }

    private void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore Instance State here
    }

    @Override
    public void onClick(View v) {
        if (PermissionHandler.checkPermission(getActivity(), PermissionHandler.RECORD_AUDIO)) {
            savingMessage = edMsg.getText().toString();

            if (v == ivStop && !savingMessage.isEmpty()) {

                MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext())
                        .title("Set Title")
                        .inputRangeRes(2, 15, R.color.colorPrimary)
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input("untitle", null, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                showToast(input.toString() + " saved");

                                dbManager.insertData(input.toString(), savingMessage);

                                edMsg.setText("");
                                ivRecord.setImageResource(R.drawable.ic_mic_white_48dp);

                                isPause = true;
                                isCountDownOn = false;
                                NoSpeechCountDown.cancel();
                            }
                        })
                        .positiveColorRes(R.color.colorPrimary)
                        .buttonRippleColorRes(R.color.colorPrimary);
                MaterialDialog dialog = builder.build();
                dialog.show();

                progressBar.setIndeterminate(false);
                speech.cancel();
                turnBeepOn();
            }
            if (v == ivRecord) {
                turnBeepOff();

                if (isPause) {
                    ivRecord.setImageResource(R.drawable.ic_pause_white_48dp);

                    progressBar.setIndeterminate(true);
                    speech.startListening(recognizerIntent);
                    isPause = false;
                    //Log.i(LOG_TAG, "record click");
                } else if (!isPause) {
                    ivRecord.setImageResource(R.drawable.ic_mic_white_48dp);
                    progressBar.setIndeterminate(false);

                    speech.cancel();
                    isPause = true;
                    isCountDownOn = false;
                    NoSpeechCountDown.cancel();
                    //Log.i(LOG_TAG, "pause click");
                }
            }
        } else {
            PermissionHandler.askForPermission(PermissionHandler.RECORD_AUDIO, getActivity());
        }
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.i(LOG_TAG, "onReadyForSpeech");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            isCountDownOn = true;
            NoSpeechCountDown.start();

        }
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.i(LOG_TAG, "onBeginningOfSpeech");
        if (isCountDownOn) {
            isCountDownOn = false;
            NoSpeechCountDown.cancel();
        }
    }

    @Override
    public void onEndOfSpeech() {
        Log.i(LOG_TAG, "onEndOfSpeech");
    }

    @Override
    public void onResults(Bundle results) {
        Log.i(LOG_TAG, "onResults");
        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

        edMsg.append(matches.get(0) + " ");
        //toggleButton.setChecked(true);
        speech.startListening(recognizerIntent);

    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        //Log.i(LOG_TAG, "onPartialResults");
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        //Log.i(LOG_TAG, "onEvent");
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);

        float quiet_max = 25f;
        float medium_max = 65f;

        if (rmsdB < quiet_max) {
            //Log.i(LOG_TAG, "Quiet" + rmsdB);
            // quiet
        } else if (rmsdB >= quiet_max && rmsdB < medium_max) {
            //Log.i(LOG_TAG, "Medium" + rmsdB);
            // medium
        } else {
            //Log.i(LOG_TAG, "Loud" + rmsdB);
            // loud
        }
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        //Log.i(LOG_TAG, "onBufferReceived: " + buffer);
    }

    @Override
    public void onError(int error) {
        if (error == SpeechRecognizer.ERROR_NO_MATCH) {
            speech.startListening(recognizerIntent);
        } else if (error == SpeechRecognizer.ERROR_SPEECH_TIMEOUT) {
            edMsg.append(" ");
            speech.startListening(recognizerIntent);
        }

        if (isCountDownOn) {
            isCountDownOn = false;
            NoSpeechCountDown.cancel();
            speech.startListening(recognizerIntent);
        }
        //String errorMessage = getErrorText(error);
        //Log.d(LOG_TAG, "FAILED " + errorMessage);

    }

//    public String getErrorText(int errorCode) {
//        String message;
//        switch (errorCode) {
//            case SpeechRecognizer.ERROR_AUDIO:
//                message = "Audio recording error";
//                break;
//            case SpeechRecognizer.ERROR_CLIENT:
//                message = "Client side error";
//                break;
//            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
//                message = "Insufficient permissions";
//                break;
//            case SpeechRecognizer.ERROR_NETWORK:
//                message = "Network error";
//                break;
//            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
//                message = "Network timeout";
//                break;
//            case SpeechRecognizer.ERROR_NO_MATCH:
//                message = "No match";
//                break;
//            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
//                message = "RecognitionService busy";
//                break;
//            case SpeechRecognizer.ERROR_SERVER:
//                message = "error from server";
//                break;
//            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
//                message = "No speech input";
//                break;
//            default:
//                message = "Didn't understand, please try again.";
//                break;
//        }
//        return message;
//    }

    private void turnBeepOff() {
        audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_MUTE, 0);
        audioManager.adjustStreamVolume(AudioManager.STREAM_ALARM, AudioManager.ADJUST_MUTE, 0);
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
        audioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_MUTE, 0);
        audioManager.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_MUTE, 0);
    }

    private void turnBeepOn() {
        audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_UNMUTE, 0);
        audioManager.adjustStreamVolume(AudioManager.STREAM_ALARM, AudioManager.ADJUST_UNMUTE, 0);
        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE, 0);
        audioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_UNMUTE, 0);
        audioManager.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_UNMUTE, 0);
    }

    protected CountDownTimer NoSpeechCountDown = new CountDownTimer(5000, 5000) {

        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            isCountDownOn = false;
            speech.cancel();
            speech.startListening(recognizerIntent);
        }
    };

    private void showToast(String text) {
        Toast.makeText(Contextor.getInstance().getContext(),
                text,
                Toast.LENGTH_SHORT).show();
    }
}
