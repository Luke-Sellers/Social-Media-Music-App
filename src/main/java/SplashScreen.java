import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class SplashScreen {

    public void splash() {
        StringBuilder splash = new StringBuilder("===========================================================\n")
    .append("(         __________________________                      )\n")
    .append(")        ||                        ||                     (\n")
    .append("(        ||                        ||        (  ) )       )\n")
    .append(")        ||     Musir Analyzec     ||         )( (        (\n")
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
        System.out.println("Enter folder path: ");

        File folder = new File(scnr.nextLine());
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Error: Folder not found");
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
        secondMenu();
        String s = scnr.nextLine();
        if (s.equals("1")) {
            ArrayList<String> filenames = new ArrayList<>();
            ArrayList<File> filelist = new ArrayList<>();
            for (File file : filelist) {
            if (file.isFile() && file.getName().endsWith(".csv")) {
                    filenames.add(file.getName());
                }
            }
            Collections.sort(filenames);
            for (String file : filenames) {
                System.out.println((filenames.indexOf(file) +1) +  " - " + file);
            }
            System.out.print("Select a file number: ");
            int i = filenames.size();
            int j = scnr.nextInt();
            if (j > i || j < i) {
                second(scnr, folder);
            }
            File f = filelist.get(j + 1);
            action(scnr, f);
        } else if (s.equals("2")) {
            initialize();
        }
    }

    public void actionMenu() {
        StringBuilder actionmenu = new StringBuilder("1 - Song Stats\n")
            .append("2 - User Similarity\n")
            .append("3 - User Prediction\n")
            .append("4 - User Recommendation\n")
            .append("5 - Return\n")
            .append("\n")
            .append("Select an option: ");
        System.out.print(actionmenu);
    }

    public void action(Scanner scnr, File f) {
        try {
        actionMenu();
        String s = scnr.nextLine();
        ReadFile read = new ReadFile();
        read.readFile(f);
        OutputCSV out = new OutputCSV();
        switch (s) {
            case "1":
                String output1 = scnr.nextLine();
                System.out.print("Enter output path: ");
                out.createCSV(output1, read);
                System.out.println("Output written to: " + output1);
                break;

            case "2":
                String output2 = scnr.nextLine();
                System.out.print("Enter output path: ");
                NormalizeScore nsU = new NormalizeScore();
                nsU.NormalizeUserScore(read);
                EuclideanDistances euU = new EuclideanDistances();
                out.similarityCSV(output2, nsU.usernodes, euU.SimilarityScore(nsU.usernodes));
                System.out.println("Output written to: \n" + output2);
                break;

            case "3":
                String output3 = scnr.nextLine();
                System.out.print("Enter output path: ");
                NormalizeScore nsP = new NormalizeScore();
                nsP.NormalizeUserScore(read);
                EuclideanDistances euP = new EuclideanDistances();
                predictRatings pr = new predictRatings();
                out.predictedCSV(output3, pr.predictMissing(read, euP.SimilarityScore(nsP.usernodes)));
                System.out.println("Output written to: \n" + output3);
                break;

            case "4":
                String output4 = scnr.nextLine();
                System.out.print("Enter output path: ");
                Collections.sort(read.usernodes, new Comparator<Node>() {
                    @Override
                    public int compare(Node n1, Node n2) {
                        return n1.name.compareTo(n2.name);
                    }
                });
                for (Node n : read.usernodes) {
                    System.out.println((read.usernodes.indexOf(n) + 1) + " - " + n.name);
                }
                System.out.print("Enter selections (e.g. 2,5,7): ");
                String[] selectedarr = scnr.nextLine().split(",");
                List<String> selected = new ArrayList<>();
                for (String g : selectedarr) {
                    selected.add(g);
                }
                KmeanCluster kc = new KmeanCluster();
                out.recommendedCSV(output4, kc.getKmeanCluster(read, selected));
                System.out.println("Output written to: \n" + output4);
                break;
        }
    } catch (Exception e) {
        System.out.println("Error: ");
        action(scnr, f);
    }
    }

    public void initialize() {
        try {
            firstMenu();
            Scanner scnr = new Scanner(System.in);
            String s = "";
            if (scnr.hasNextLine()) {
                s = scnr.nextLine();
            }
            if (s.equals("1")) {
                first(s, scnr);
            } else if (s.equals("2")) {
                System.out.println("Exiting...");
                System.exit(0);
            } else {
                System.out.println("Error: Invalid option");
                initialize();
            }
            scnr.close();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(0);
        } 
    }
}
