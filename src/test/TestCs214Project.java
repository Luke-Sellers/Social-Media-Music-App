import org.junit.jupiter.api.Test;
//import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TestCs214Project {

    @Test
    public void testCase() {
        assertTrue(true);
    }

    @Test
    public void testReadFile() {
        ReadFile rf = new ReadFile();
        File f = new File("/s/chopin/n/under/lukesell/pa5-Luke-Sellers/database/files/file1.csv");
        rf.readFile(f);
        assertFalse(rf.usernodes.isEmpty());
        assertFalse(rf.songnodes.isEmpty());
        assertFalse(rf.usernames.isEmpty());
        assertFalse(rf.songnames.isEmpty());
    }

    @Test
    public void testgetMean() {
        getMean gm = new getMean();
        ArrayList<Node> nodes = new ArrayList<>();
        Node n1 = new Node();
        n1.name = "Song1";
        n1.ratings.add(4.0);
        n1.ratings.add(5.0);
        n1.ratings.add(Double.NaN);
        nodes.add(n1);

        Node n2 = new Node();
        n2.name = "Song2";
        n2.ratings.add(3.0);
        n2.ratings.add(4.0);
        n2.ratings.add(5.0);
        nodes.add(n2);

        ArrayList<Double> means = gm.mean(nodes);
        assertEquals(4.5, means.get(0));
        assertEquals(4.0, means.get(1));
    }

    @Test
    public void testgetSD() {
        getSD gs = new getSD();
        ArrayList<Node> nodes = new ArrayList<>();
        Node n1 = new Node();
        n1.name = "Song1";
        n1.ratings.add(3.0);
        n1.ratings.add(2.0);
        n1.ratings.add(3.0);
        n1.ratings.add(5.0);
        n1.ratings.add(Double.NaN);
        nodes.add(n1);

        Node n2 = new Node();
        n2.name = "Song2";
        n2.ratings.add(5.0);
        n2.ratings.add(4.0);
        n2.ratings.add(2.0);
        n2.ratings.add(5.0);
        nodes.add(n2);

        ArrayList<Double> STDs = gs.getStd(nodes);
        assertEquals(1.0897247358851685, STDs.get(0));
        assertEquals(1.224744871391589, STDs.get(1));
    }

    @Test
    public void testNormalizeUserScore() {
        NormalizeScore ns = new NormalizeScore();
        ReadFile rf = new ReadFile();
        File f = new File("/s/chopin/n/under/lukesell/pa5-Luke-Sellers/database/files/file1.csv");
        rf.readFile(f);
        ns.NormalizeUserScore(rf);
        assertFalse(ns.usernodes.isEmpty());
    }

    @Test
    public void testKmeansCluster() {
        ReadFile read = new ReadFile();
        read.readFile(new File("/s/chopin/n/under/lukesell/pa5-Luke-Sellers/database/files/file3.csv"));
        List<String> selected = new ArrayList<>();
        selected.add("song3");
        selected.add("song5");
        selected.add("song6");
        KmeanCluster kc = new KmeanCluster();
        ArrayList<String[]> km = kc.getKmeanCluster(read, selected);
        assertFalse(km.isEmpty());
    }

    @Test
    public void Cs214Project() {
        String[] args = {"/s/chopin/n/under/lukesell/pa5-Luke-Sellers/database/files/file1.csv", "output.csv", "-u"};
        ReadFile read = new ReadFile();
        read.readFile(new File(args[0]));
        try {
            Cs214Project.main(args);
            assertTrue(true);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testOutputCSV() {
        OutputCSV oc = new OutputCSV();
        ReadFile rf = new ReadFile();
        File f = new File("/s/chopin/n/under/lukesell/pa5-Luke-Sellers/database/files/file1.csv");
        rf.readFile(f);
        try {
            oc.createCSV("output.csv", rf);
            assertTrue(new File("output.csv").exists());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testSimilarityCSV() {
        ReadFile read = new ReadFile();
        OutputCSV out = new OutputCSV();
        read.readFile(new File("/s/chopin/n/under/lukesell/pa5-Luke-Sellers/database/files/file1.csv"));
        NormalizeScore ns = new NormalizeScore();
        ns.NormalizeUserScore(read);
        EuclideanDistances eu = new EuclideanDistances();
        try {
            out.similarityCSV("similarity.csv", ns.usernodes, eu.SimilarityScore(ns.usernodes));
            assertTrue(new File("similarity.csv").exists());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}