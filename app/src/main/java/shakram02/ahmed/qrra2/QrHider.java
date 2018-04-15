package shakram02.ahmed.qrra2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

public class QrHider {
    private Paint paint;
    private static String HIDER_TAG = "QrHider";

    QrHider() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setColor(Color.WHITE);
    }

    public void hide(RectF boundingBox, Bitmap qrcode) {
        Log.d(HIDER_TAG, String.format("Hiding: [%s, %s]", boundingBox.centerX(), boundingBox.centerY()));
        Canvas c = new Canvas(qrcode);
        c.drawRect(boundingBox, paint);
    }
}
