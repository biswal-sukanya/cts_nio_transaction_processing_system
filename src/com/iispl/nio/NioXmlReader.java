package com.iispl.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import com.iispl.exception.InvalidInputFileException;

public class NioXmlReader {
	

	    private static final int BUFFER_SIZE = 1024;

	    public String readXml(Path filePath) throws IOException, InvalidInputFileException {

	        validateFile(filePath);

	        StringBuilder xmlContent = new StringBuilder();

	        try (FileChannel fileChannel = FileChannel.open(filePath, StandardOpenOption.READ)) {

	            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

	            while (fileChannel.read(buffer) != -1) {

	                buffer.flip();

	                xmlContent.append(StandardCharsets.UTF_8.decode(buffer));

	                buffer.clear();
	            }
	        }

	        return xmlContent.toString();
	    }

	    private static void validateFile(Path filePath) throws InvalidInputFileException, IOException {

	        if (filePath == null) {
	            throw new InvalidInputFileException();
	        }

	        if (!Files.exists(filePath)) {
	            throw new InvalidInputFileException();
	        }

	        if (!Files.isRegularFile(filePath)) {
	            throw new InvalidInputFileException();
	        }

	        if (!Files.isReadable(filePath)) {
	            throw new InvalidInputFileException();
	        }

	        if (!filePath.toString().toLowerCase().endsWith(".xml")) {
	            throw new InvalidInputFileException();
	        }

	        if (Files.size(filePath) == 0) {
	            throw new InvalidInputFileException();
	        }
	    }

}
