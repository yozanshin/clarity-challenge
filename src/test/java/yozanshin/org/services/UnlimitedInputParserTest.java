package yozanshin.org.services;

import lombok.extern.java.Log;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import yozanshin.org.config.ApplicationConstants;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.Random;

@Log
public class UnlimitedInputParserTest {

    private final String logsFolder;
    private final String unlimitedFilePath;

    public UnlimitedInputParserTest() throws Exception {
        logsFolder = Paths.get(ClassLoader.getSystemResource("test-folder").toURI()).toAbsolutePath().toString();
        unlimitedFilePath = Paths.get(ClassLoader.getSystemResource("test-unlimited-log-file.txt").toURI())
                .toAbsolutePath().toString();
    }


    private UnlimitedInputParser fileParser;

    @Before
    public void setup() {

        fileParser = new UnlimitedInputParser();
    }

    @Test
    @Ignore //As it returns nothing, it should be implemented with reflection and mocks
    public void testParseFilesFromFolder() {

        fileParser.generateLogsReportFromPreviousLogFiles(logsFolder, "Host1");
    }

    @Test
    @Ignore //As it runs indefinitely, it should be implemented with mocks
    public void testParseUnlimitedFile() {

        appendNewRowsToLogFile();

        fileParser.generateLogReportEveryHourFromLogFile(unlimitedFilePath, "Host1");
    }

    private void appendNewRowsToLogFile() {

        new Thread(() -> {

            while (true) {

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(unlimitedFilePath, true))) {

                    writer.append(buildNewRow());
                    writer.flush();

                    Thread.sleep(2000L);

                } catch (Exception ex) {

                    log.severe("Unable to write into " + unlimitedFilePath);
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    private String buildNewRow() {

        Random random = new Random();

        return String.join(
                ApplicationConstants.ROW_ITEMS_SEPARATOR,
                String.valueOf(System.currentTimeMillis()),
                "Host" + random.nextInt(10),
                "Host" + random.nextInt(10) +System.lineSeparator()
        );
    }
}