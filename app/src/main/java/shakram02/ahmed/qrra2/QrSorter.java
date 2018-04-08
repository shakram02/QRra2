package shakram02.ahmed.qrra2;

import com.google.android.gms.vision.barcode.Barcode;

import java.util.Comparator;

public class QrSorter implements Comparator<Barcode> {
    @Override
    public int compare(Barcode o1, Barcode o2) {
        int x1 = o1.getBoundingBox().centerX();
        int x2 = o2.getBoundingBox().centerX();

        return Integer.compare(x1, x2);
    }
}
