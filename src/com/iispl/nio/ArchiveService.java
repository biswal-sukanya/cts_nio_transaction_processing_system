package com.iispl.nio;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class ArchiveService {

    public void moveToArchive(Path processedFile,Path archiveFolder) throws IOException {

        Files.createDirectories(archiveFolder);

        Files.move(processedFile,
                archiveFolder.resolve(processedFile.getFileName()),
                StandardCopyOption.REPLACE_EXISTING);

        System.out.println("File moved to Archive.");
    }

    public void moveToRejected(Path processedFile, Path rejectedFolder) throws IOException {

        Files.createDirectories(rejectedFolder);

        Files.move(processedFile,
                rejectedFolder.resolve(processedFile.getFileName()),
                StandardCopyOption.REPLACE_EXISTING);

        System.out.println("File moved to Rejected.");
    }

}