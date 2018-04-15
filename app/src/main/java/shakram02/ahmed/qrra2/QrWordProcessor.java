package shakram02.ahmed.qrra2;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.ArrayList;
import java.util.Collections;
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


    QrWordProcessor(Context context, String ttsEngineName) {
        Log.i(QR_SPELLER_TAG, "TTS engine:" + ttsEngineName);
        tts = new TextToSpeech(context, this, ttsEngineName);
        tts.setLanguage(Locale.US);
    }


    @Override
    public void receiveDetections(Detector.Detections<Barcode> detections) {
        if (detections.getDetectedItems().size() == 0) {
            return;
        }

        PriorityQueue<Pair<Double, Double>> barcodes = new PriorityQueue<>(20, new HorizontalQrSorter());
        ArrayList<Pair<Double, Double>> points = new ArrayList<>();
        HashMap<Pair<Double, Double>, String> pointMap = new HashMap<>();
        SparseArray<Barcode> detected = detections.getDetectedItems();

        for (int i = 0; i < detected.size(); i++) {
            Barcode barcode = detected.valueAt(i);

            Pair<Double, Double> center = new Pair<>((double) barcode.getBoundingBox().centerX(),
                    (double) barcode.getBoundingBox().centerY());

            pointMap.put(center, barcode.rawValue);
            points.add(center);
        }

        Collections.sort(points, new VerticalQrSorter());   // Sort the points it helps the algorithm behave better

        LineFinder lineFinder = new LineFinder(points, 17);
        StringBuilder sb = new StringBuilder();
        StringBuilder entryStringifier = new StringBuilder();
        HashMap<Pair<Double, Double>, HashSet<Pair<Double, Double>>> results = lineFinder.clusterLines();
        StringBuilder sentence = new StringBuilder();

        /// TODO sort line detections using VerticalQrSorter
        for (HashMap.Entry<Pair<Double, Double>, HashSet<Pair<Double, Double>>> entry : results.entrySet()) {
            barcodes.addAll(entry.getValue());

            while (!barcodes.isEmpty()) {
                Pair<Double, Double> linePoint = barcodes.poll();
                String val = pointMap.get(linePoint);
                entryStringifier.append(String.format("%s %s\n", linePoint, val));

                sb.append(val);
            }

            // Append well-formed words to sentence builder, otherwise discard them
            if (sb.toString().endsWith(".") && !sb.toString().equals(".")) {
                sentence.append(sb.toString());
            }

            sb = new StringBuilder();
        }

        String readSentence = sentence.toString();
        Log.i(QR_SPELLER_TAG, String.format("Found %s detections [%s lines]: \n%s \n%s\n",
                detections.getDetectedItems().size(), results.size(),
                entryStringifier.toString(), readSentence));

        speakWord(readSentence);
    }

    private void speakWord(String word) {
        tts.speak(word, TextToSpeech.QUEUE_ADD, null);
    }

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
