import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

@SuppressWarnings({"PMD.CognitiveComplexity", "PMD.CyclomaticComplexity"})
public class SplashScreen {

    Scanner scnr = new Scanner(System.in);

    public boolean checkCSV(String s) {
        if (s.contains(".csv")) {
            return true;
        }
        return false;
    }

    public void splash() {
        StringBuilder splash = new StringBuilder("===========================================================\n")
    .append("(         __________________________                      )\n")
    .append(")        ||                        ||                     (\n")
    .append("(        ||                        ||        (  ) )       )\n")
    .append(")        ||     Music Analyzer     ||         )( (        (\n")
    .append("(        ||          v1.0          ||        (  ) )       )\n")
    .append(")        ||                        ||       _________     (\n")
    .append("(        ||                        ||    .-'---------|    )\n")
    .append(")        *__________________________*   ( c Java 2001|    (\n")
    .append("(       / ==__oooo__==___ooooo-+ o //    `-.         |    )\n")
    .append(")      /  oooo   ______  ooooo    //       '_________'    (\n")
    .append("(     /         /_____/          /'         `-------'     )\n")
    .append(")     `-------------------------'                         (\n")
    .append("(                                                         )\n")
    .append("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n")
    .append("\n");
    System.out.print(splash);
    }

    public void firstMenu() {
        StringBuilder firstmenu = new StringBuilder("1 - Load Folder\n")
            .append("2 - Exit\n")
            .append("\n")
            .append("Select an option: ");
        System.out.print(firstmenu);
    }

    public void first(String s, Scanner scnr) {
        System.out.print("Enter folder path: ");
        File folder = new File(scnr.next());
        System.out.println("\n");
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Error: folder not found");
            System.out.println();
            initialize();
        } else {
            second(scnr, folder);
        }
    }

    public void secondMenu() {
        StringBuilder secondmenu = new StringBuilder("1 - Select File\n")
            .append("2 - Return\n")
            .append("\n")
            .append("Select an option: ");
        System.out.print(secondmenu);
    }

    public void second(Scanner scnr, File folder) {
        try {
        secondMenu();
        String s = scnr.next();
        System.out.println("\n");
        if (s.equals("1")) {
            ArrayList<String> filenames = new ArrayList<>();
            File[] filelist = folder.listFiles();
            for (File file : filelist) {
            if (file.isFile() && file.getName().endsWith(".csv")) {
                    filenames.add(file.getName());
                }
            }
            Collections.sort(filenames);
            Arrays.sort(filelist, (f1, f2) -> f1.getName().compareTo(f2.getName()));
            
            for (String file : filenames) {
                System.out.println((filenames.indexOf(file) +1) +  " - " + file);
            }
            System.out.println();
            System.out.print("Select file number: ");
            String j = scnr.next();
            int i = filenames.size();
            System.out.println("\n");
            if (Integer.parseInt(j) > i || Integer.parseInt(j) < 1) {
                throw new CustomException("Error: invalid option");
            }
            int in = Integer.parseInt(j);
            File f = filelist[in - 1];
            action(scnr, f, folder);
        } else if (s.equals("2")) {
            initialize();
        } else {
            throw new CustomException("Error: invalid option");
        }
    } catch (CustomException e) {
            System.out.println(e.getMessage());
            System.out.println();
            second(scnr, folder);
        }
    }

    public void actionMenu() {
        StringBuilder actionmenu = new StringBuilder("1 - Song Stats\n")
            .append("2 - User Similarity\n")
            .append("3 - User Prediction\n")
            .append("4 - User Recommendation\n")
            .append("5 - Return\n")
            .append("\n")
            .append("Select an option: \n");
        System.out.print(actionmenu);
    }

    public void action(Scanner scnr, File f, File folder) {
        try {

        actionMenu();
        String s = "";
        if (scnr.hasNext()) {
            s = scnr.next();
        }
        System.out.println();
        ReadFile read = new ReadFile();
        read.readFile(f);
        OutputCSV out = new OutputCSV();

        switch (s) {
            case "1":
                System.out.print("Enter output path: ");
                String output1 = scnr.next();
                System.out.println("\n");
                if (!checkCSV(output1)) {
                    System.out.println("Error: only `.csv` files are supported");
                    System.out.println();
                    action(scnr, f, folder);
                    break;
                }
                out.createCSV(output1, read);
                System.out.println("Output written to: " + output1);
                System.out.println();
                action(scnr, f, folder);
                break;

            case "2":
                System.out.print("Enter output path: ");
                String output2 = scnr.next();
                System.out.println("\n");
                if (!checkCSV(output2)) {
                    System.out.println("Error: only `.csv` files are supported");
                    System.out.println();
                    action(scnr, f, folder);
                    break;
                }
                NormalizeScore nsU = new NormalizeScore();
                nsU.NormalizeUserScore(read);
                EuclideanDistances euU = new EuclideanDistances();
                out.similarityCSV(output2, nsU.usernodes, euU.Similarity(nsU.usernodes));
                System.out.println("Output written to: " + output2);
                System.out.println();
                action(scnr, f, folder);
                break;

            case "3":
                System.out.print("Enter output path: ");
                String output3 = scnr.next();
                System.out.println("\n");
                if (!checkCSV(output3)) {
                    System.out.println("Error: only `.csv` files are supported");
                    System.out.println();
                    action(scnr, f, folder);
                    break;
                }
                NormalizeScore nsP = new NormalizeScore();
                nsP.NormalizeUserScore(read);
                EuclideanDistances euP = new EuclideanDistances();
                predictRatings pr = new predictRatings();
                out.predictedCSV(output3, pr.pm(read, euP.Similarity(nsP.usernodes)));
                System.out.println("Output written to: " + output3);
                System.out.println();
                action(scnr, f, folder);
                break;

            case "4":
                System.out.print("Enter output path: ");
                String output4 = scnr.next();
                System.out.println("\n");
                if (!checkCSV(output4)) {
                    System.out.println("Error: only `.csv` files are supported");
                    System.out.println();
                    action(scnr, f, folder);
                    break;
                }
                Collections.sort(read.songnodes, new Comparator<Node>() {
                    @Override
                    public int compare(Node n1, Node n2) {
                        return n1.name.compareTo(n2.name);
                    }
                });
                for (Node n : read.songnodes) {
                    System.out.println((read.songnodes.indexOf(n) + 1) + " - " + n.name);
                }
                System.out.println();
                System.out.print("Enter selections (e.g. 2,5,7): \n");
                String[] selectedarr = scnr.next().split(",");
                System.out.println();
                List<String> selected = new ArrayList<>();
                for (String g : selectedarr) {
                    g = "song" + g;
                    selected.add(g);
                }
                KmeanCluster kc = new KmeanCluster();
                out.recommendedCSV(output4, kc.gkm(read, selected));
                System.out.println("Output written to: " + output4);
                System.out.println();
                action(scnr, f, folder);
                break;

            case "5":
                second(scnr, folder);
                break;

            default:
                throw new CustomException("invalid option");
        }

    } catch (CustomException e) {
        System.out.println("Error: " + e.getMessage() + "\n");
        action(scnr, f, folder);
    }
    }

    public void initialize() {
        try {
            firstMenu();
            String s = "";
            if (scnr.hasNext()) {
                s = scnr.next();
            }
            System.out.println("\n");
            if (s.equals("1")) {
                first(s, scnr);
            } else if (s.equals("2")) {
                System.exit(0);
            } else {
                System.out.println("Error: invalid option");
                System.out.println();
                initialize();
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(0);
        } 
    }
}
