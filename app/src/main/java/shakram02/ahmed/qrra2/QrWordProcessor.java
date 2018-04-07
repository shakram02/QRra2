package shakram02.ahmed.qrra2;

import android.content.Context;
import android.content.res.Configuration;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.Comparator;
import java.util.Locale;
import java.util.PriorityQueue;

/**
 * Created by ahmed on 3/4/18.
 */

public class QrSpellerFactory implements
        MultiProcessor.Factory<Barcode>, TextToSpeech.OnInitListener {

    private TextToSpeech tts;

    private static final String ACTIVITY_TAG = "QrSpeller";
    private PriorityQueue<Barcode> scannedBarcodes;

    public QrSpellerFactory(Context context, String ttsEngineName) {
        tts = new TextToSpeech(context, this, ttsEngineName);
        tts.setLanguage(Locale.US);
        scannedBarcodes = new PriorityQueue<>(20, new QrSorter(context.getResources().getConfiguration().orientation));

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

    public Tracker<Barcode> create(Barcode barcode) {

        // TODO extract barcode value and location,
        // we shouldn't read in reverse order
        // maybe we need to pause before finding a space character

        // TODO if we want to track word location, we should return
        // a better tracker
        // We might want to join rectangles of many tracker also to track
        // a whole word

//        if (tts.isSpeaking()) {
//            tts.stop();
//        }


        if (!barcode.rawValue.equals(".")) {
            tts.speak(barcode.rawValue, TextToSpeech.QUEUE_ADD, null);
        }
        Log.i(ACTIVITY_TAG, String.format("[%s]", barcode.rawValue) +
                " At:" + String.format("%s,%s", barcode.getBoundingBox().centerX(), barcode.getBoundingBox().centerY()));

        scannedBarcodes.add(barcode);
        onWordProcess(barcode.rawValue);

        return new ScannedString(barcode.rawValue, barcode.getBoundingBox());
    }


    private void onWordProcess(String scannedValue) {
        if (!scannedValue.equals(".")) {
            return;
        }

        StringBuilder accumulated = new StringBuilder();

        while (!scannedBarcodes.isEmpty()) {
            Barcode barcode = scannedBarcodes.poll();

            Log.i(ACTIVITY_TAG, String.format("X:%s Y:%s Val:%s",
                    barcode.getBoundingBox().centerX(),
                    barcode.getBoundingBox().centerY(),
                    barcode.rawValue));

            if (barcode.rawValue.equals(".")) {
                continue;
            }

            accumulated.append(barcode.rawValue);
        }
        tts.speak(accumulated.toString(), TextToSpeech.QUEUE_ADD, null);
        Log.i(ACTIVITY_TAG, "Spoke " + accumulated.toString());
    }

    @Override
    public void onInit(int status) {
        if (status != TextToSpeech.SUCCESS) {
            Log.e(ACTIVITY_TAG, String.format("Initialization failed (status: %s).", status));
        } else {
            Log.e(ACTIVITY_TAG, String.format("Initialization success (status: %s).", status));
        }
    }

    class QrSorter implements Comparator<Barcode> {
        private boolean isLandscape;

        public QrSorter(int orientation) {
            setOrientation(orientation);
        }

        public void setOrientation(int orientation) {
            isLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE;
        }

        @Override
        public int compare(Barcode o1, Barcode o2) {
            if (isLandscape) {
                int y1 = o1.getBoundingBox().centerY();
                int y2 = o2.getBoundingBox().centerY();
                return Integer.compare(y2, y1);
            } else {
                int x1 = o1.getBoundingBox().centerX();
                int x2 = o2.getBoundingBox().centerX();
                return Integer.compare(x2, x1);

            }
        }
    }
}
