import java.util.ArrayList;

public class getSD {

    public ArrayList<Double> getStd(ArrayList<Node> nodes) {
        ArrayList<Double> std = new ArrayList<>();
        getMean m = new getMean();
        ArrayList<Double> means = m.mean(nodes);

        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            double mean = means.get(i);
            double sumSq = 0;
            int count = 0;
            for (double d : node.ratings) {
                if (!Double.isNaN(d)) {
                    sumSq += Math.pow(d - mean, 2);
                    count++;
                }
            }
            std.add(count == 0 ? Double.NaN : Math.sqrt(sumSq / count));
        }
        return std;
    }
}
