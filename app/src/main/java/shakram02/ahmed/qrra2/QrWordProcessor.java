package shakram02.ahmed.qrra2;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.Locale;

/**
 * Created by ahmed on 3/4/18.
 */

public class QrWordProcessor implements TextToSpeech.OnInitListener, Detector.Processor<Barcode> {

    private TextToSpeech tts;

    private static final String QR_SPELLER_TAG = "QrSpeller";


    public QrWordProcessor(Context context, String ttsEngineName) {
        tts = new TextToSpeech(context, this, ttsEngineName);
        tts.setLanguage(Locale.US);


//        Locale[] locales = Locale.getAvailableLocales();
//        List<Locale> localeList = new ArrayList<>();
//        for (Locale locale : locales) {
//            locale.lang
//            int res = tts.isLanguageAvailable(locale);
//            if (res == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
//                localeList.add(locale);
//            }
//        }

    }


    @Override
    public void receiveDetections(Detector.Detections<Barcode> detections) {
        if (detections.getDetectedItems().size() == 0) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        SparseArray<Barcode> detected = detections.getDetectedItems();
        for (int i = 0; i < detected.size(); i++) {
            sb.append(detected.valueAt(i).rawValue);
            sb.append(" - ");
        }

        Log.i(QR_SPELLER_TAG, String.format("Found %s detections: %s",
                detections.getDetectedItems().size(), sb.toString()));
    }

//    public void onWord(String word) {
//
//        // TODO extract barcode value and location,
//        // we shouldn't read in reverse order
//        // maybe we need to pause before finding a space character
//
//        // TODO if we want to track word location, we should return
//        // a better tracker
//        // We might want to join rectangles of many tracker also to track
//        // a whole word
//
////        if (tts.isSpeaking()) {
////            tts.stop();
////        }
//
//
//        if (!barcode.rawValue.equals(".")) {
//            tts.speak(barcode.rawValue, TextToSpeech.QUEUE_ADD, null);
//        }
//        Log.i(QR_SPELLER_TAG, String.format("[%s]", barcode.rawValue) +
//                " At:" + String.format("%s,%s", barcode.getBoundingBox().centerX(), barcode.getBoundingBox().centerY()));
//
//        scannedBarcodes.add(barcode);
//        onWordProcess(barcode.rawValue);
//
//        return new ScannedString(barcode.rawValue, barcode.getBoundingBox());
//    }


//    private void onWordProcess(String scannedValue) {
//        if (!scannedValue.equals(".")) {
//            return;
//        }
//
//        StringBuilder accumulated = new StringBuilder();
//
//
//        tts.speak(accumulated.toString(), TextToSpeech.QUEUE_ADD, null);
//        Log.i(QR_SPELLER_TAG, "Spoke " + accumulated.toString());
//    }

    @Override
    public void onInit(int status) {
        if (status != TextToSpeech.SUCCESS) {
            Log.e(QR_SPELLER_TAG, String.format("Initialization failed (status: %s).", status));
        } else {
            Log.e(QR_SPELLER_TAG, String.format("Initialization success (status: %s).", status));
        }
    }

    @Override
    public void release() {
        // void
    }
}
