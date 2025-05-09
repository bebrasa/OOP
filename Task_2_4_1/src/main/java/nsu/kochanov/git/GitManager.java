package nsu.kochanov.git;


import java.io.File;

public class GitManager {
    public static void cloneOrPull(String repoUrl, String targetDir) {
        try {
            File dir = new File(targetDir);
            ProcessBuilder builder;
            if (dir.exists()) {
                builder = new ProcessBuilder("git", "-C", targetDir, "pull");
            } else {
                builder = new ProcessBuilder("git", "clone", repoUrl, targetDir);
            }
            builder.inheritIO().start().waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
