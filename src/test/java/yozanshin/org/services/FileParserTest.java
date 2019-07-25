package yozanshin.org.services;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.nio.file.Paths;

/*
 * Copyright (c) 2019 Ryanair Ltd. All rights reserved.
 */
public class FileParserTest {

    private final String filePath;

    private FileParser fileParser;

    public FileParserTest() throws Exception {
        filePath = Paths.get(ClassLoader.getSystemResource("test-log-file.txt").toURI()).toAbsolutePath().toString();
    }

    @Before
    public void setup() {

        fileParser = new FileParser();
    }

    @Test
    @Ignore //As it returns nothing, it should be implemented with reflection and mocks
    public void testListConnectedHosts() {

        fileParser.getConnectedHostsInFileByHOstAndPeriod(filePath, "Host11", 100, 250);
    }
}