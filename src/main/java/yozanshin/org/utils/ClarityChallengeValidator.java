package yozanshin.org.utils;

import java.io.File;

public class ClarityChallengeValidator {

    public static void validateHostname(final String hostname) {

        if (hostname == null || hostname.isEmpty()) {
            throw new IllegalArgumentException("Invalid hostname: " +hostname);
        }
    }

    public static void validateTimestamp(final long timestamp) {

        if (timestamp < 1) {
            throw new IllegalArgumentException("Invalid timestamp: " +timestamp);
        }
    }

    public static void validateFile(final String filePath) {

        if (!(new File(filePath).exists())) {
            throw new IllegalArgumentException("Invalid file: " +filePath);
        }
    }

    public static void validateFolder(final String folderPath) {

        File folder = new File(folderPath);

        if (!(folder.exists() && folder.isDirectory())) {
            throw new IllegalArgumentException("Invalid folder: " +folderPath);
        }
    }

    public static void validateArgumentsNumber(final String[] args, final int requiredNumber) {

        if (args.length != requiredNumber) {

            throw new IllegalArgumentException("Invalid app parameters number, it should be " +requiredNumber);
        }
    }
}
