package shakram02.ahmed.qrra2;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.Locale;

/**
 * Created by ahmed on 3/4/18.
 */

public class QrSpellerFactory implements MultiProcessor.Factory<Barcode>, TextToSpeech.OnInitListener {
    TextToSpeech tts;
    private static final String ESPEAK_ENGINE = "com.redzoc.ramees.tts.espeak";
    private static final String ACTIVITY_TAG = "QrSpeller";

    public QrSpellerFactory(Context context) {
        tts = new TextToSpeech(context, this);
        tts.setLanguage(Locale.US);

        tts = new TextToSpeech(context, this, ESPEAK_ENGINE);
    }

    @Override
    public Tracker<Barcode> create(Barcode barcode) {
        // TODO extract barcode value and location,
        // we shouldn't read in reverse order
        // maybe we need to pause before finding a space character

        // TODO if we want to track word location, we should return
        // a better tracker
        // We might want to join rectangles of many tracker also to track
        // a whole word

        tts.speak(barcode.rawValue, TextToSpeech.QUEUE_ADD, null);
        return new Tracker<>();
    }


    @Override
    public void onInit(int status) {
        if (status != TextToSpeech.SUCCESS) {
            Log.e(ACTIVITY_TAG, "Initialization failed (status: $status).");
        } else {
            Log.i(ACTIVITY_TAG, "Initialization success (status: $status).");
        }
    }
}
