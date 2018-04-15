package shakram02.ahmed.qrra2.processors;

import android.graphics.Bitmap;
import android.graphics.Color;

public class Binarizer {
    /**
     * Boolean that tells me how to treat a transparent pixel (Should it be black?)
     */
    private static final boolean TRASNPARENT_IS_BLACK = false;
    /**
     * This is a point that will break the space into Black or white
     * In real words, if the distance between WHITE and BLACK is D;
     * then we should be this percent far from WHITE to be in the black region.
     * Example: If this value is 0.5, the space is equally split.
     */
    private static final double SPACE_BREAKING_POINT = 13.0 / 30.0;

    public static void binarize(Bitmap bitmap) {
        for (int i = 0; i < bitmap.getWidth(); i++) {
            for (int c = 0; c < bitmap.getHeight(); c++) {
                int pixel = bitmap.getPixel(i, c);
                if (shouldBeBlack(pixel)) {
                    bitmap.setPixel(i, c, Color.BLACK);
                } else {
                    bitmap.setPixel(i, c, Color.WHITE);
                }
            }
        }
    }

    /**
     * @param pixel the pixel that we need to decide on
     * @return boolean indicating whether this pixel should be black
     */
    private static boolean shouldBeBlack(int pixel) {
        int alpha = Color.alpha(pixel);
        int redValue = Color.red(pixel);
        int blueValue = Color.blue(pixel);
        int greenValue = Color.green(pixel);
        if (alpha == 0x00) //if this pixel is transparent let me use TRASNPARENT_IS_BLACK
            return TRASNPARENT_IS_BLACK;
        // distance from the white extreme
        double distanceFromWhite = Math.sqrt(Math.pow(0xff - redValue, 2) + Math.pow(0xff - blueValue, 2) + Math.pow(0xff - greenValue, 2));
        // distance from the black extreme //this should not be computed and might be as well a function of distanceFromWhite and the whole distance
        double distanceFromBlack = Math.sqrt(Math.pow(0x00 - redValue, 2) + Math.pow(0x00 - blueValue, 2) + Math.pow(0x00 - greenValue, 2));
        // distance between the extremes //this is a constant that should not be computed :p
        double distance = distanceFromBlack + distanceFromWhite;
        // distance between the extremes
        return ((distanceFromWhite / distance) > SPACE_BREAKING_POINT);
    }
}
