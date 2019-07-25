package yozanshin.org;

import lombok.extern.java.Log;
import yozanshin.org.config.ApplicationConstants;
import yozanshin.org.services.FileParser;
import yozanshin.org.services.UnlimitedInputParser;
import yozanshin.org.utils.ClarityChallengeValidator;

@Log
public class ClarityChallengeMain {

    public static void main(String[] args) {

        int action;

        try{
            action = Integer.valueOf(args[0]);
        } catch (IndexOutOfBoundsException | NumberFormatException ex) {
            throw new IllegalArgumentException("Invalid action parameter, it must be an integer number");
        }

        switch (action) {

            case ApplicationConstants.FILE_PARSE_ACTION:
                parseFile(args);
                break;
            case ApplicationConstants.FOLDER_PARSE_ACTION:
                parseFolder(args);
                break;
            case ApplicationConstants.UNLIMITED_FILE_PARSE_ACTION:
                parseUnlimitedFile(args);
                break;
            default:
                throw new IllegalArgumentException("Unrecognized action");
        }
    }

    private static void parseFile(final String[] args) {

        ClarityChallengeValidator.validateArgumentsNumber(args, 5);

        final String filePath = args[1];
        ClarityChallengeValidator.validateFile(filePath);

        final String host = args[2];
        ClarityChallengeValidator.validateHostname(host);

        final long initDateTime = Long.valueOf(args[3]);
        ClarityChallengeValidator.validateTimestamp(initDateTime);
        final long endDatetime = Long.valueOf(args[4]);
        ClarityChallengeValidator.validateTimestamp(endDatetime);

        new FileParser().getConnectedHostsInFileByHOstAndPeriod(filePath, host, initDateTime, endDatetime);
    }

    private static void parseFolder(final String[] args) {

        ClarityChallengeValidator.validateArgumentsNumber(args, 3);

        final String folderPath = args[1];
        ClarityChallengeValidator.validateFolder(folderPath);

        final String host = args[2];
        ClarityChallengeValidator.validateHostname(host);

        new UnlimitedInputParser().generateLogsReportFromPreviousLogFiles(folderPath, host);
    }

    private static void parseUnlimitedFile(final String[] args) {

        ClarityChallengeValidator.validateArgumentsNumber(args, 3);

        final String filePath = args[1];
        ClarityChallengeValidator.validateFile(filePath);

        final String host = args[2];
        ClarityChallengeValidator.validateHostname(host);

        new UnlimitedInputParser().generateLogReportEveryHourFromLogFile(filePath, host);
    }
}
