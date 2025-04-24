import java.util.ArrayList;

public class getMean {
    public ArrayList<Double> mean(ArrayList<Node> nodes) {
        ArrayList<Double> songmean = new ArrayList<>();
        for (Node n : nodes) {
            double sum = 0;
            int count = 0;
            for (Double d : n.ratings) {
                if (!Double.isNaN(d)) {
                    sum += d;
                    count++;
                }
            }
            songmean.add(count == 0 ? Double.NaN : sum / count);
        }
        return songmean;
    }
}
