import java.util.*;

public class predictRatings {

    @SuppressWarnings({"PMD.CognitiveComplexity", "PMD.CyclomaticComplexity"})
    public ArrayList<String[]> predictMissing(ReadFile rf, ArrayList<ArrayList<Double>> sim2) throws CustomException {
        try {
        getSD gsd = new getSD();
        getMean gm = new getMean();

        ArrayList<Node> users = rf.usernodes;
        ArrayList<String> usernames = new ArrayList<>();
        for (Node n : users) usernames.add(n.name);

        ArrayList<Double> stds = gsd.getStd(users);
        ArrayList<Double> means = gm.mean(users);

        Map<String, Double> userToMean = new HashMap<>();
        Map<String, Double> userToStd = new HashMap<>();
        for (int i = 0; i < users.size(); i++) {
            userToMean.put(users.get(i).name, means.get(i));
            userToStd.put(users.get(i).name, stds.get(i));
        }

        ArrayList<String> allSongs = new ArrayList<>(new TreeSet<>(rf.songnames));
        Map<String, Integer> songIndexMap = new HashMap<>();
        for (int i = 0; i < allSongs.size(); i++) {
            songIndexMap.put(allSongs.get(i), i);
        }

        ArrayList<String[]> predictedRatings = new ArrayList<>();
        boolean hasMissing = false;

        for (String song : allSongs) {
            int globalSongIdx = songIndexMap.get(song);
            for (int userIdx = 0; userIdx < users.size(); userIdx++) {
                Node user = users.get(userIdx);
                Double originalRating = null;
                if (globalSongIdx < user.ratings.size()) {
                    originalRating = user.ratings.get(globalSongIdx);
                }
                if (originalRating != null && !Double.isNaN(originalRating)) {
                    continue;
                }

                hasMissing = true;

                List<Integer> similarityOrder = new ArrayList<>();
                for (int i = 0; i < users.size(); i++) similarityOrder.add(i);
                final int currentUserIdx = userIdx;
                similarityOrder.sort(Comparator.comparingDouble(i -> sim2.get(currentUserIdx).get(i)));

                double normalizedRating = Double.NaN;
                for (int otherIdx : similarityOrder) {
                    if (otherIdx == userIdx) continue;
                    double sim = sim2.get(userIdx).get(otherIdx);
                    if (Double.isNaN(sim)) continue;

                    Node other = users.get(otherIdx);
                    if (globalSongIdx >= other.normalscore.size()) continue;

                    double otherNorm = other.normalscore.get(globalSongIdx);
                    if (!Double.isNaN(otherNorm)) {
                        normalizedRating = otherNorm;
                        break;
                    }
                }

                double predicted;
                if (Double.isNaN(normalizedRating)) {
                    predicted = Double.NaN;
                } else {
                    double std = userToStd.get(user.name);
                    double mean = userToMean.get(user.name);
                    predicted = normalizedRating * std + mean;
                    predicted = Math.round(predicted);
                    if (predicted > 5) predicted = 5;
                    if (predicted < 1) predicted = 1;
                }
                predictedRatings.add(new String[]{song, user.name, Double.isNaN(predicted) ? "NaN" : String.valueOf((int) predicted)});
            }
        }

        if (!hasMissing) {
            throw new CustomException("No predictions to be made");
        }

        predictedRatings.sort((a, b) -> {
            int cmp = a[0].compareTo(b[0]);
            if (cmp != 0) return cmp;
            return a[1].compareTo(b[1]);
        });
        return predictedRatings;
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(0);
        }
        return null;
    }
}