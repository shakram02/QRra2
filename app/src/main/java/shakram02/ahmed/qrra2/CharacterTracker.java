package shakram02.ahmed.qrra2;

import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;

/**
 * Created by ahmed on 3/18/18.
 */
class CharacterTracker extends Tracker<Barcode> {
    private final TextToSpeech tts;

    CharacterTracker(TextToSpeech tts) {
        this.tts = tts;
        tts.speak("Text to say aloud", TextToSpeech.QUEUE_ADD, null);
    }

    @Override
    public void onDone() {
        super.onDone();
    }

    @Override
    public void onNewItem(int id, Barcode item) {
        Log.i("SpellerFactory", "ID:" + id + " Raw value:" + item.rawValue);
        super.onNewItem(id, item);
    }

    @Override
    public void onUpdate(Detector.Detections<Barcode> detections, Barcode item) {
        super.onUpdate(detections, item);
    }

    @Override
    public void onMissing(Detector.Detections<Barcode> detections) {
        super.onMissing(detections);
    }
}
