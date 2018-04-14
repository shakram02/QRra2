package shakram02.ahmed.qrra2;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.PriorityQueue;

import kotlin.Pair;
import linefinder.LineFinder;


/**
 * Created by ahmed on 3/4/18.
 */

public class QrWordProcessor implements TextToSpeech.OnInitListener, Detector.Processor<Barcode> {

    private TextToSpeech tts;

    private static final String QR_SPELLER_TAG = "QrSpeller";


    public QrWordProcessor(Context context, String ttsEngineName) {
        Log.i(QR_SPELLER_TAG, "TTS engine:" + ttsEngineName);
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

        PriorityQueue<Pair<Double, Double>> barcodes = new PriorityQueue<>(20, new QrSorter());
        HashMap<Pair<Double, Double>, String> pointMap = new HashMap<>();
        SparseArray<Barcode> detected = detections.getDetectedItems();

        ArrayList<Pair<Double, Double>> points = new ArrayList<>();
        for (int i = 0; i < detected.size(); i++) {
            Barcode barcode = detected.valueAt(i);
            Pair<Double, Double> center = new Pair<>((double) barcode.getBoundingBox().centerX(),
                    (double) barcode.getBoundingBox().centerY());

            pointMap.put(center, barcode.rawValue);
            points.add(center);
        }

        LineFinder lineFinder = new LineFinder(points, 10);
        StringBuilder sb = new StringBuilder();
        StringBuilder entryStringifier = new StringBuilder();
        HashMap<Pair<Double, Double>, HashSet<Pair<Double, Double>>> results = lineFinder.clusterLines();

        for (HashMap.Entry<Pair<Double, Double>, HashSet<Pair<Double, Double>>> entry : results.entrySet()) {
            barcodes.addAll(entry.getValue());

            while (!barcodes.isEmpty()) {
                Pair<Double, Double> linePoint = barcodes.poll();
                String val = pointMap.get(linePoint);
                entryStringifier.append(String.format("%s %s\n", linePoint, val));

                if (val.equals(".")) {
                    sb.append(" ");
                } else {
                    sb.append(val);
                }
            }
            barcodes.clear();
        }

        Log.i(QR_SPELLER_TAG, String.format("Found %s detections: %s \n%s\n",
                detections.getDetectedItems().size(), entryStringifier.toString(), sb.toString()));
        speakWord(sb.toString());
    }

    private void speakWord(String word) {
//        if (tts.isSpeaking()) {
//            tts.stop();
//        }

        tts.speak(word, TextToSpeech.QUEUE_ADD, null);
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
