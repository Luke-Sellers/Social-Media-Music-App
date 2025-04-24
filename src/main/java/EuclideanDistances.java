import java.util.*;
import org.apache.commons.math4.legacy.ml.distance.EuclideanDistance;

public class EuclideanDistances {

    @SuppressWarnings({"PMD.CognitiveComplexity", "PMD.CyclomaticComplexity"})
    public ArrayList<ArrayList<Double>> SimilarityScore(ArrayList<Node> nodes) {
        ArrayList<ArrayList<Double>> similaritytable = new ArrayList<>();
        try {
        nodes.sort(Comparator.comparing(n -> n.name));

        for (int i = 0; i < nodes.size(); i++) {
            ArrayList<Double> row = new ArrayList<>(Collections.nCopies(nodes.size(), 0.0));
            similaritytable.add(row);
        }

        EuclideanDistance eu = new EuclideanDistance();
        int compatibleusers = 0;

        for (int i = 0; i < nodes.size(); i++) {
            for (int j = i + 1; j < nodes.size(); j++) {
                ArrayList<Double> validRatings1 = new ArrayList<>();
                ArrayList<Double> validRatings2 = new ArrayList<>();

                for (int k = 0; k < nodes.get(i).songs.size(); k++) {
                    String song = nodes.get(i).songs.get(k);
                    int indexInUser2 = nodes.get(j).songs.indexOf(song);

                    if (indexInUser2 != -1) {
                        double rating1 = nodes.get(i).normalscore.get(k);
                        double rating2 = nodes.get(j).normalscore.get(indexInUser2);

                        if (!Double.isNaN(rating1) && !Double.isNaN(rating2)) {
                            validRatings1.add(rating1);
                            validRatings2.add(rating2);
                        }
                    }
                }

                double distance;
                if (validRatings1.isEmpty()) {
                    distance = Double.NaN;
                } else {
                    compatibleusers++;
                    distance = eu.compute(
                            validRatings1.stream().mapToDouble(Double::doubleValue).toArray(),
                            validRatings2.stream().mapToDouble(Double::doubleValue).toArray()
                    );
                }
                similaritytable.get(i).set(j, distance);
                similaritytable.get(j).set(i, distance);
            }
        }
        if (compatibleusers < 1) {
            throw new CustomException("at least two cooperative users are required for user similarity");
        }

        return similaritytable;
    } catch (CustomException e) {
        System.err.println("Error: " + e.getMessage());
        System.exit(0);
    }
    return null;
}
}