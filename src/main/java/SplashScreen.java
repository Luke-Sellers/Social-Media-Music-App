import java.io.File;
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
    }

    public static void secondMenu() {

    }

    public static void actionMenu() {

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
                System.out.println(s);
                System.out.println("Enter folder path: ");
                File folder = new File(scnr.nextLine());
                for (File file : folder.listFiles()) {
                    if (file.isFile() && file.getName().endsWith(".csv")) {
                        System.out.println(file.getAbsolutePath());
                    }
                }
                if (!folder.exists() || !folder.isDirectory()) {
                    System.out.println("Error: Invalid folder path");
                    initialize();
                } else {
                    System.out.println("Loading folder: " + folder.getAbsolutePath());
                }

            } else if (s.equals("2")) {
                System.out.println("Exiting...");
                System.exit(0);
            } else {
                System.out.println("Error: Invalid option");
                initialize();
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(0);
        } 
    }
}
