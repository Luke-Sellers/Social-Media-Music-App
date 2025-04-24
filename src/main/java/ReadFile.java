import java.util.*;
import java.io.*;

public class ReadFile {

    ArrayList<String> songnames = new ArrayList<String>();

    ArrayList<Integer> songocc = new ArrayList<Integer>();

    ArrayList<String> usernames = new ArrayList<String>();

    ArrayList<Integer> userocc = new ArrayList<Integer>();
    
    ArrayList<Node> songnodes = new ArrayList<Node>();

    ArrayList<Node> usernodes = new ArrayList<Node>();

    @SuppressWarnings({"PMD.CognitiveComplexity", "PMD.CyclomaticComplexity"})
    public void readFile(File f) {

        try {

            Scanner scnr = new Scanner(f);

            if (!scnr.hasNextLine()) {
                scnr.close();
                throw new CustomException("Input file empty");
            }


            while (scnr.hasNextLine()) {
                String[] fileline = scnr.nextLine().split(",");

                if (fileline.length != 3 ) {
                    scnr.close();
                    throw new CustomException("Incorrect number of fields in CSV");
                }
                if (fileline[2].contains(".")) {
                        scnr.close();
                        throw new CustomException("Rating not an int");
                }
                if (Integer.parseInt(fileline[2]) > 5 || Integer.parseInt(fileline[2]) < 1) {
                    scnr.close();
                    throw new CustomException("Rating not in range");
                }

                if (this.songnames.isEmpty() && this.songocc.isEmpty() && this.songnodes.isEmpty()) {
                    this.songnames.add(fileline[0]);
                    this.songocc.add(1);
                    Node n = new Node();
                    n.ratings.add(Double.parseDouble(fileline[2]));
                    n.name = fileline[0];
                    n.songs.add(fileline[1]);
                    this.songnodes.add(n);
                } else {
                    for (String s : this.songnames) {
                    if (s.equals(fileline[0])) {
                        int j = this.songnames.indexOf(s);
                        int y = this.songocc.get(j).intValue() + 1;
                        this.songocc.set(j, y);
                        Node n = this.songnodes.get(this.songnames.indexOf(s));
                        n.ratings.add(Double.parseDouble(fileline[2]));
                        n.name = fileline[0];
                        n.songs.add(fileline[1]);
                        this.songnodes.set(this.songnames.indexOf(s), n);
                    }
                }
                }            

                if (!this.songnames.contains(fileline[0])) {
                    this.songnames.add(fileline[0]);
                    this.songocc.add(1);
                    Node n = new Node();
                    n.ratings.add(Double.parseDouble(fileline[2]));
                    n.name = fileline[0];
                    n.songs.add(fileline[1]);
                    this.songnodes.add(n);
                }

                //songs above users below

                if (this.usernames.isEmpty() && this.userocc.isEmpty()) {
                    this.usernames.add(fileline[1]);
                    this.userocc.add(0);
                    Node n = new Node();
                    n.name = fileline[1];
                    n.songs.add(fileline[0]);
                    n.ratings.add(Double.parseDouble(fileline[2]));
                    usernodes.add(n);
                } else {
                    for (String s : this.usernames) {
                    if (s.equals(fileline[1])) {
                        int j = this.usernames.indexOf(s);
                        int y = this.userocc.get(j).intValue() + 1;
                        this.userocc.set(j, y);
                        Node n = usernodes.get(j);
                        n.songs.add(fileline[0]);
                        n.ratings.add(Double.parseDouble(fileline[2]));
                        usernodes.set(j, n);
                    }
                }
            }

                if (!this.usernames.contains(fileline[1])) {
                    usernames.add(fileline[1]);
                    this.userocc.add(1);
                    Node n = new Node();
                    n.name = fileline[1];
                    n.songs.add(fileline[0]);
                    n.ratings.add(Double.parseDouble(fileline[2]));
                    usernodes.add(n);
                }

                    }
            scnr.close();
        } catch (Exception e) {
            System.err.println("Error " + e.getMessage());
            System.exit(0);
        }
    }
}