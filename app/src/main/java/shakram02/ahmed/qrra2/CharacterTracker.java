package shakram02.ahmed.qrra2;

import android.graphics.Rect;
import android.speech.tts.TextToSpeech;

import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;

/**
 * Created by ahmed on 3/18/18.
 */
class CharacterTracker extends Tracker<Barcode> {
    private final TextToSpeech tts;

    CharacterTracker(String value, Rect location, TextToSpeech tts) {
        this.tts = tts;
        tts.speak("Text to say aloud", TextToSpeech.QUEUE_ADD, null);
    }

}
