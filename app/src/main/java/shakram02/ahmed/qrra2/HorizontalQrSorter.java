package shakram02.ahmed.qrra2;

import java.util.Comparator;

import kotlin.Pair;

public class QrSorter implements Comparator<Pair<Double, Double>> {
    @Override
    public int compare(Pair<Double, Double> o1, Pair<Double, Double> o2) {
        return Double.compare(o2.getFirst(), o1.getFirst());
    }

    public void setOrientation() {
        throw new UnsupportedOperationException();
    }
}
