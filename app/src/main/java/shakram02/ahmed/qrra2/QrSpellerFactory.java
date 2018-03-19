package shakram02.ahmed.qrra2;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.Locale;

/**
 * Created by ahmed on 3/4/18.
 */

public class QrSpellerFactory implements MultiProcessor.Factory<Barcode>, TextToSpeech.OnInitListener {
    TextToSpeech tts;

    public QrSpellerFactory(Context context) {
        tts = new TextToSpeech(context, this);
        tts.setLanguage(Locale.US);
    }

    @Override
    public Tracker<Barcode> create(Barcode barcode) {
        return new CharacterTracker(tts);
    }


    @Override
    public void onInit(int status) {

    }
}
