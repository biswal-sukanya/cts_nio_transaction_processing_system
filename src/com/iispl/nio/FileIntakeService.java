package com.iispl.nio;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

import com.iispl.exception.InvalidFileNameException;
import com.iispl.exception.InvalidInputFileException;

public class FileIntakeService {
	private final Path incoming;
	private final Path processing;
	
	public FileIntakeService()throws IOException{
		incoming=Paths.get("data","incoming");
		processing=Paths.get("data","processing");
		
		Files.createDirectories(incoming);
		Files.createDirectories(processing);
	}
	
	public Path getFileForProcessing() throws IOException, InvalidFileNameException,InvalidInputFileException{
		try(DirectoryStream<Path> stream=Files.newDirectoryStream(incoming,"*.xml")){
			for(Path file:stream) {
				BasicFileAttributes attribute=Files.readAttributes(file,BasicFileAttributes.class);
				
				if(!attribute.isRegularFile()) {
					throw new InvalidInputFileException();
				}
				if(attribute.size()==0) {
					throw new InvalidInputFileException();
				}
				
				if(!isValidFileName(file.getFileName().toString())) {
					throw new InvalidFileNameException();
				}
				
				Path target=processing.resolve(file.getFileName());
				
				Files.move(file, target, StandardCopyOption.REPLACE_EXISTING);
				return target;
				
			}
			
		}
		return null;
	}
	
	private boolean isValidFileName(String fileName) {

        return fileName.matches(
                "TXN_[A-Za-z0-9]+_\\d{8}_\\d+\\.xml"
        );
    }
	
	
	
}
