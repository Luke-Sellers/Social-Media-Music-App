import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;

public class OutputCSV {

    @SuppressWarnings({"PMD.CognitiveComplexity", "PMD.CyclomaticComplexity"})
    public void createCSV(String s, ReadFile r) {
        try {
        if (!s.contains(".csv")) {
            throw new CustomException("Output file not CSV");
        }
        File myFile = new File(s);
        getMean m = new getMean();
        ArrayList<Double> g = m.mean(r.songnodes);
        getSD t = new getSD();
        ArrayList<Double> h = t.getStd(r.songnodes);
        ArrayList<String[]> grits = new ArrayList<String[]>();
        for (String str: r.songnames) {
            String[] peas = new String[4];
            peas[0] = r.songnames.get(r.songnames.indexOf(str));
            peas[1] = r.songocc.get(r.songnames.indexOf(str)).toString();
            peas[2] = g.get(r.songnames.indexOf(str)).toString();
            peas[3] = h.get(r.songnames.indexOf(str)).toString();
            grits.add(peas);
        }
        grits.sort(Comparator.comparing(arr -> arr[0]));

            PrintWriter p = new PrintWriter(myFile);
            p.println("song,number of ratings,mean,standard deviation");
            for (String[] a: grits) {
                p.print(a[0] + ",");
                p.print(a[1] + ",");
                p.print(a[2] + ",");
                p.print(a[3]);
                p.println();
            }
            p.close();
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found");
            System.exit(0);
        } catch (CustomException e) {
        System.err.println("Error " + e.getMessage());
        System.exit(0);
    }
}

@SuppressWarnings({"PMD.CognitiveComplexity", "PMD.CyclomaticComplexity"})
public void similarityCSV(String filename, ArrayList<Node> nodes, ArrayList<ArrayList<Double>> similarity) {
    try {
        if (!filename.contains(".csv")) {
            throw new CustomException("Output file not CSV");
        }
        File myFile = new File(filename);
        PrintWriter writer = new PrintWriter(myFile);
        writer.println("name1,name2,similarity");
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = i + 1; j < nodes.size(); j++) {
                double sim = similarity.get(i).get(j);
                    writer.println(nodes.get(i).name + "," + nodes.get(j).name + "," + sim);   
            }
        }
        writer.close();

    } catch (FileNotFoundException e) {
            System.err.println("Error: File not found");
            System.exit(0);
        } catch (CustomException e) {
        System.err.println("Error " + e.getMessage());
        System.exit(0);
}
}

@SuppressWarnings({"PMD.CognitiveComplexity", "PMD.CyclomaticComplexity"})
public void predictedCSV(String filename, ArrayList<String[]> predictedratings) {
    try {
        if (!filename.contains(".csv")) {
            throw new CustomException("Output file not CSV");
        }
        File f = new File(filename);
        File parentDir = f.getParentFile();
        if (parentDir != null && !parentDir.exists() && !parentDir.mkdirs()) {
            throw new FileNotFoundException("Failed to create parent directories for: " + filename);
        }
        PrintWriter w = new PrintWriter(f);
        w.println("song,user,predicted rating");
        for (String[] row : predictedratings) {
            w.println(String.join(",", row));
        }
        w.close();
    } catch (FileNotFoundException e) {
        System.err.println("Error: File not found");
        System.exit(0);
    } catch (CustomException e) {
        System.err.println("Error " + e.getMessage());
        System.exit(0);
}
}

@SuppressWarnings({"PMD.CognitiveComplexity", "PMD.CyclomaticComplexity"})
public void recommendedCSV(String filename, ArrayList<String[]> reccomendedsongs) {
    try {
        if (!filename.contains(".csv")) {
            throw new CustomException("Output file not CSV");
        }
        File f = new File(filename);
        File parentDir = f.getParentFile();
        if (parentDir != null && !parentDir.exists() && !parentDir.mkdirs()) {
            throw new FileNotFoundException("Failed to create parent directories for: " + filename);
        }
        PrintWriter w = new PrintWriter(f);
        w.println("user choice,recommendation");
        for (String[] row : reccomendedsongs) {
            w.println(String.join(",", row));
        }
        w.close();
    } catch (FileNotFoundException e) {
        System.err.println("Error: File not found");
        System.exit(0);
    } catch (CustomException e) {
        System.err.println("Error " + e.getMessage());
        System.exit(0);
}
}
}
