package shakram02.ahmed.qrra2;

import android.graphics.Rect;
import android.util.Log;

import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;

/**
 * Created by ahmed on 3/18/18.
 */
class StringTracker extends Tracker<Barcode> {

    private final String value;
    private final Rect location;

    StringTracker(String value, Rect location) {
        this.value = value;
        this.location = location;
    }

    public Rect getLocation() {
        return location;
    }

    public String getValue() {
        return value;
    }

    @Override
    public void onNewItem(int id, Barcode item) {
        Log.i("QrSpeller", "Id:" + id);
        super.onNewItem(id, item);
    }
}
