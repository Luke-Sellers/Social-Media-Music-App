import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

public class NormalizeScore {

    ArrayList<Node> usernodes;
    ArrayList<Double> standardmean;
    ArrayList<Double> standardstd;

    ArrayList<Node> songnodes;
    ArrayList<Double> songmean;
    ArrayList<Double> songstd;

    public static boolean checksame(ArrayList<Double> list) {
        if (list == null || list.isEmpty() || list.size() == 1) {
            return true;
        }
        double first = list.get(0);
        for (double e : list) {
            if (e != first) {
                return false;
            }
        }
        return true;
    }

    public void NormalizeUserScore(ReadFile r) {
        try {
            this.usernodes = r.usernodes;

            Iterator<Node> it = this.usernodes.iterator();
            List<Node> toRemove = new ArrayList<>();

            while (it.hasNext()) {
                Node a = it.next();
                if (checksame(a.ratings)) {
                    toRemove.add(a);
                    continue;
                }
            }
            for (Node n : toRemove) {
                String str = n.name;
                r.songnames.remove(str);
            }
            this.usernodes.removeAll(toRemove);

            getMean m = new getMean();
            ArrayList<Double> means = m.mean(usernodes);
            getSD s = new getSD();
            ArrayList<Double> StandardDeviations = s.getStd(usernodes);

            TreeSet<String> allsongset = new TreeSet<>();
            for (Node node : usernodes) {
                allsongset.addAll(node.songs);
            }
            ArrayList<String> globalsongs = new ArrayList<>(allsongset);

            for (int i = 0; i < usernodes.size(); i++) {
                Node node = usernodes.get(i);
                double mean = means.get(i);
                double std = StandardDeviations.get(i);

                ArrayList<Double> alignedRatings = new ArrayList<>();
                ArrayList<Double> alignedNormals = new ArrayList<>();

                for (String song : globalsongs) {
                    int idx = node.songs.indexOf(song);
                    if (idx != -1) {
                        double rating = node.ratings.get(idx);
                        alignedRatings.add(rating);
                        alignedNormals.add((rating - mean) / std);
                    } else {
                        alignedRatings.add(Double.NaN);
                        alignedNormals.add(Double.NaN);
                    }
                }
                node.songs = globalsongs;
                node.ratings = alignedRatings;
                node.normalscore = alignedNormals;
            }

        } catch (Exception e) {
            System.err.println("Error " + e.getMessage());
            System.exit(0);
        }
    }



    //User scores above song scores below



    public void NormalizeSongScore(ReadFile r) {
        try {
            this.songnodes = r.songnodes;

            Iterator<Node> it = this.songnodes.iterator();
            List<Node> toRemove = new ArrayList<>();

            while (it.hasNext()) {
                Node a = it.next();
                if (checksame(a.ratings)) {
                    toRemove.add(a);
                    continue;
                }
            }
            for (Node n : toRemove) {
                String str = n.name;
                r.songnames.remove(str);
            }
            this.songnodes.removeAll(toRemove);

            getMean m = new getMean();
            getSD s = new getSD();
            this.songmean = m.mean(songnodes);
            this.songstd = s.getStd(songnodes);

            for (Node n : songnodes) {
                for (double d : n.ratings) {
                    n.normalscore.add((d - songmean.get(songnodes.indexOf(n))) / songstd.get(songnodes.indexOf(n)));
                }
            }

        } catch (Exception e) {
            System.err.println("Error " + e.getMessage());
            System.exit(0);
        }
    }
}