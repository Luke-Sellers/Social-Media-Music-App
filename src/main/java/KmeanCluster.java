import java.util.*;
import java.util.stream.Collectors;

public class KmeanCluster {

    @SuppressWarnings({"PMD.CognitiveComplexity", "PMD.CyclomaticComplexity"})
    public ArrayList<String[]> fillaverages(ReadFile readfirst, ArrayList<String[]> pm) {
        getMean gm = new getMean();
        ArrayList<Double> means = gm.mean(readfirst.usernodes);
        ArrayList<Double> songmeans = gm.mean(readfirst.songnodes);
        for (String[] row : pm) {
            if (row[2].equals("NaN")) {
                Double userMean = 0.0;
                int numUserRatings = 0;
                Double songMean = 0.0;
                Double numSongRatings = 0.0;
                for (Node n: readfirst.usernodes) {
                    if (n.name.equals(row[1])) {
                        for (Double i: n.ratings) {
                            if (!i.isNaN()) {
                                numUserRatings += 1;
                            }
                        }
                        userMean = means.get(readfirst.usernodes.indexOf(n));
                    }
                }
                for (Node m: readfirst.songnodes) {
                    if (m.name.equals(row[0])) {
                        for (Double j: m.ratings) {
                            if (!j.isNaN()) {
                                numSongRatings += 1;
                            }
                        }
                        songMean = songmeans.get(readfirst.songnames.indexOf(row[0]));
                        }
                }
                
                long unpredicted = Math.round((songMean * numSongRatings + userMean * numUserRatings) / (numSongRatings + numUserRatings));
                row[2] = Long.toString(unpredicted);
            }
        }
        return pm;
    }

    @SuppressWarnings({"PMD.CognitiveComplexity", "PMD.CyclomaticComplexity"})
    public void fillSongScores(ReadFile readfirst) {
        TreeSet<String> sortedUniqueSongs = new TreeSet<>(readfirst.songnames);
        readfirst.songnames = new ArrayList<>(sortedUniqueSongs);
        readfirst.songnodes.sort(Comparator.comparing(node -> node.name));

        try {
        for (Node n : readfirst.songnodes) {
            n.ratings = new ArrayList<>();
        }

        for (int i = 0; i < readfirst.usernodes.size(); i++) {
            Node user = readfirst.usernodes.get(i);
            for (int j = 0; j < readfirst.songnodes.size(); j++) {
                readfirst.songnodes.get(j).ratings.add(user.ratings.get(j));
            }
        }
    } catch (Exception e) {
        System.err.println("Error: " + e.getMessage());
        System.exit(0);
    }
}
    @SuppressWarnings({"PMD.CognitiveComplexity", "PMD.CyclomaticComplexity"})
    public void alignRatings(ReadFile readfirst) {
        TreeSet<String> allsongset = new TreeSet<>();
            for (Node node : readfirst.songnodes) {
                allsongset.addAll(node.songs);
            }
            ArrayList<String> globalsongs = new ArrayList<>(allsongset);

            for (int i = 0; i < readfirst.songnodes.size(); i++) {

                Node node = readfirst.songnodes.get(i);
                ArrayList<Double> alignedRatings = new ArrayList<>();

                for (String song : globalsongs) {
                    int idx = node.songs.indexOf(song);
                    if (idx != -1) {
                        double rating = node.ratings.get(idx);
                        alignedRatings.add(rating);
                    } else {
                        alignedRatings.add(Double.NaN);
                    }
                }
                node.ratings = alignedRatings;
            }
    }

    @SuppressWarnings({"PMD.CognitiveComplexity", "PMD.CyclomaticComplexity"})
    public void addAndRemove(ReadFile readfirst, ArrayList<String[]> pm) {
        ArrayList<Node> toRemove = new ArrayList<Node>();
            ArrayList<Node> filteredNodes = new ArrayList<>();
            for (Node n : readfirst.usernodes) {
                int i = 0;
                for (Double j : n.ratings) {
                    if (!j.isNaN()) {
                        i++;
                    }
                }
                if (i == 1) {
                    toRemove.add(n);
                }
            }
            for (Node n : readfirst.usernodes) {
                for (String[] row : pm) {
                    if (n.name.equals(row[1])) {
                        for (String s : n.songs) {     
                            if (s.equals(row[0])) {
                                n.ratings.set(n.songs.indexOf(s), Double.parseDouble(row[2]));
                            }
                        }
                    }   
                }
            }
            for (Node n : readfirst.usernodes) {
                if (!toRemove.contains(n)) {
                    filteredNodes.add(n);
                }
            }
            readfirst.usernodes = filteredNodes;
    }

    @SuppressWarnings({"PMD.CognitiveComplexity", "PMD.CyclomaticComplexity"})
    public ArrayList<String[]> getKmeanCluster(ReadFile readfirst, List<String> selectedSongNames) {
        try {
            NormalizeScore ns = new NormalizeScore();
            ns.NormalizeUserScore(readfirst);
    
            EuclideanDistances eu = new EuclideanDistances();
            predictRatings pr = new predictRatings();
            ArrayList<String[]> pm = pr.predictMissing(readfirst, eu.SimilarityScore(ns.usernodes));
    
            fillaverages(readfirst, pm);
            addAndRemove(readfirst, pm);
            alignRatings(readfirst);
            fillSongScores(readfirst);
            ns.NormalizeSongScore(readfirst);
    
            int k = selectedSongNames.size();
    
            if (readfirst.songnodes.size() < k + 1) {
                System.err.println("Error: insufficient songs for clustering.");
                System.exit(0);
            }
            Set<String> duplicateCheck = new HashSet<>(selectedSongNames);
            if (duplicateCheck.size() != selectedSongNames.size()) {
                System.err.println("Error: duplicate song selections");
                System.exit(0);
            }

            Set<String> seedSet = new HashSet<>(selectedSongNames);

            ArrayList<ArrayList<Node>> clusters = new ArrayList<>();
            ArrayList<ArrayList<Double>> centroids = new ArrayList<>();
            for (int i = 0; i < k; i++) clusters.add(new ArrayList<>());
    
            for (String sel : selectedSongNames) {
                Optional<Node> opt = readfirst.songnodes.stream().filter(s -> s.name.equals(sel)).findFirst();
                if (opt.isEmpty()) {
                    System.err.println("Error: selected song '" + sel + "' not found.");
                    System.exit(0);
                }
                centroids.add(new ArrayList<>(opt.get().normalscore));
            }

            int dim = readfirst.usernodes.size();

            for (int it = 0; it < 10; it++) {
                for (ArrayList<Node> c : clusters) c.clear();

                for (Node song : readfirst.songnodes) {
                    double best = Double.MAX_VALUE; 
                    int bestIdx = 0;
                    for (int c = 0; c < k; c++) {
                        double dist = 0.0;
                        List<Double> cent = centroids.get(c), vec = song.normalscore;
                        for (int d = 0; d < vec.size(); d++) {
                            double a = vec.get(d), b = cent.get(d);
                            if (!Double.isNaN(a) && !Double.isNaN(b)) {
                                double diff = a - b; dist += diff * diff;
                            }
                        }
                        if (dist < best) { best = dist; bestIdx = c; }
                    }
                    clusters.get(bestIdx).add(song);
                }

                for (int c = 0; c < k; c++) {
                    ArrayList<Double> mean = new ArrayList<>(dim);
                    for (int d = 0; d < dim; d++) {
                        double sum = 0; int cnt = 0;
                        for (Node s : clusters.get(c)) {
                            double v = s.normalscore.get(d);
                            if (!Double.isNaN(v)) { sum += v; cnt++; }
                        }
                        mean.add(cnt > 0 ? sum / cnt : centroids.get(c).get(d));
                    }
                    centroids.set(c, mean);
                }
            }

            Map<String, List<Node>> songToCluster = new HashMap<>();
            for (int c = 0; c < k; c++) songToCluster.put(selectedSongNames.get(c), clusters.get(c));

            ArrayList<String> sortedSongs = new ArrayList<>(songToCluster.keySet());
            Collections.sort(sortedSongs);

            ArrayList<String[]> out = new ArrayList<>();

            for (String seed : sortedSongs) {
                List<String> recNames = songToCluster.get(seed).stream()
                        .map(n -> n.name)
                        .filter(name -> !seedSet.contains(name))
                        .sorted()
                        .collect(Collectors.toList());
                for (String rec : recNames) out.add(new String[]{seed, rec});
            }

            if (out.isEmpty()) {
                System.err.println("Error: all clusters empty or only contain original songs.");
                System.exit(0);
            }
            return out;

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(0);
            return new ArrayList<>();
        }
    }
}
