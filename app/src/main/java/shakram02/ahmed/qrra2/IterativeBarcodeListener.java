package shakram02.ahmed.qrra2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.YuvImage;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class IterativeBarcodeListener extends FrameDataListener implements Detector.Processor<Barcode> {
    private BarcodeDetector detector;
    private static String DETECTOR_TAG = "IterativeDetector";
    private static QrHider qrHider = new QrHider();

    IterativeBarcodeListener(Context context) {
        detector = new BarcodeDetector.Builder(context).build();
        // Detector.Processor interface is implemented because of the following line
        detector.setProcessor(this);    // TODO this is extremely stupid, remove the implementation of that interface
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

        Log.i(DETECTOR_TAG, String.format("Found %s detections: %s",
                detections.getDetectedItems().size(), sb.toString()));
    }

    public boolean isOperational() {
        return detector.isOperational();
    }

    @Override
    public synchronized void receiveFrameData(FrameData frameData) {
        SparseArray<Barcode> result = new SparseArray<>();
        SparseArray<Barcode> barcodeSparseArray;

        Bitmap bitmap = bitmapFromData(frameData);

        Frame frame;
        do {
            // Create a new frame, detect codes in it, hide the detected codes,
            // repeat to detect the remaining codes

            frame = new Frame.Builder().setBitmap(bitmap)
                    .setId(frameData.getId())
                    .setTimestampMillis(frameData.getTimestamp())
                    .build();

            barcodeSparseArray = this.detector.detect(frame);

            if (barcodeSparseArray.size() == 0) {
                break;
            }

            for (int i = 0; i < barcodeSparseArray.size(); i++) {
                Barcode barcode = barcodeSparseArray.valueAt(i);
                result.append(barcodeSparseArray.keyAt(i), barcode);

                qrHider.hide(new RectF(barcode.getBoundingBox()), bitmap);
            }
        } while (barcodeSparseArray.size() > 0);

        // Done
        this.receiveDetections(new Detector.Detections<>(result,
                frame.getMetadata(), detector.isOperational()));
    }


    @Override
    public void release() {

    }

    /**
     * Creates a mutable bitmap from FrameData
     *
     * @param frameData {@link FrameData} from {@link com.google.android.gms.vision.CameraSource}
     * @return Immutable {@link Bitmap} object
     */
    private static Bitmap bitmapFromData(FrameData frameData) {
        ByteBuffer sourceBytes = frameData.getFrameData();
        YuvImage yuvimage = new YuvImage(sourceBytes.array(), ImageFormat.NV21, frameData.getWidth(), frameData.getHeight(), null);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        yuvimage.compressToJpeg(new Rect(0, 0, frameData.getWidth(), frameData.getHeight()), 100, byteArrayOutputStream); // Where 100 is the quality of the generated jpeg
        byte[] jpegArray = byteArrayOutputStream.toByteArray();

        Bitmap bitmap = BitmapFactory.decodeByteArray(jpegArray, 0, jpegArray.length);
        return bitmap.copy(Bitmap.Config.ARGB_8888, true);
    }

}
