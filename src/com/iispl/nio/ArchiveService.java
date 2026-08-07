package com.iispl.nio;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class ArchiveService {

    public void moveToArchive(Path processingFile,
                              Path archiveFolder)
            throws IOException {

        Files.createDirectories(archiveFolder);

        Files.move(processingFile,
                archiveFolder.resolve(processingFile.getFileName()),
                StandardCopyOption.REPLACE_EXISTING);

        System.out.println("File moved to Archive.");
    }

    public void moveToRejected(Path processingFile,
                               Path rejectedFolder)
            throws IOException {

        Files.createDirectories(rejectedFolder);

        Files.move(processingFile,
                rejectedFolder.resolve(processingFile.getFileName()),
                StandardCopyOption.REPLACE_EXISTING);

        System.out.println("File moved to Rejected.");
    }

}