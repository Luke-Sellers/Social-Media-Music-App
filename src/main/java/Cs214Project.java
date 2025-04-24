import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Cs214Project {

    public static void main(String[] args) {
        try {
            validateArgs(args);
            File input = new File(args[0]);

            ReadFile read = new ReadFile();
            read.readFile(input);

            processOperation(args, read);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(0);
        }
    }

    @SuppressWarnings({"PMD.CognitiveComplexity", "PMD.CyclomaticComplexity"})
    private static void validateArgs(String[] args) throws CustomException {
        if (args.length < 2) {
            throw new CustomException("Incorrect number of arguments provided");
        }
        if (!args[0].endsWith(".csv")) {
            throw new CustomException("Incorrect file format");
        }

        String option = args.length >= 3 ? args[2] : "";
        boolean validOption = option.isEmpty() || option.equals("-u") || option.equals("-p") || option.equals("-r") || option.equals("-i");
        if (!validOption) {
            throw new CustomException("unsupported argument '" + option + "'");
        }

        if (option.equals("-r") && args.length == 3) {
            throw new CustomException("Error: must select at lease one song for recommendations");
        }

        if (!new File(args[0]).exists()) {
            throw new CustomException("File not found");
        }
    }

    @SuppressWarnings({"PMD.CognitiveComplexity", "PMD.CyclomaticComplexity"})
    private static void processOperation(String[] args, ReadFile read) throws Exception {
        OutputCSV out = new OutputCSV();

        if (args.length == 2) {
            out.createCSV(args[1], read);
            return;
        }

        String option = args[2];
        switch (option) {
            case "-u":
                NormalizeScore nsU = new NormalizeScore();
                nsU.NormalizeUserScore(read);
                EuclideanDistances euU = new EuclideanDistances();
                out.similarityCSV(args[1], nsU.usernodes, euU.SimilarityScore(nsU.usernodes));
                break;

            case "-p":
                NormalizeScore nsP = new NormalizeScore();
                nsP.NormalizeUserScore(read);
                EuclideanDistances euP = new EuclideanDistances();
                predictRatings pr = new predictRatings();
                out.predictedCSV(args[1], pr.predictMissing(read, euP.SimilarityScore(nsP.usernodes)));
                break;

            case "-r":
                List<String> selected = new ArrayList<>();
                for (int i = 3; i < args.length; i++) {
                    selected.add(args[i]);
                }
                KmeanCluster kc = new KmeanCluster();
                out.recommendedCSV(args[1], kc.getKmeanCluster(read, selected));
                break;
            
            case "-i":
                SplashScreen splash = new SplashScreen();
                splash.splash();
                splash.initialize();
                break;

            default:
                throw new CustomException("unsupported argument '" + option + "'");
        }
    }
}