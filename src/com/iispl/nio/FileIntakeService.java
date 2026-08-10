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
	private final Path rejected;
	
	public FileIntakeService()throws IOException{
		incoming=Paths.get("data","incoming");
		processing=Paths.get("data","processing");
		rejected = Paths.get("data","rejected");
		
		
		Files.createDirectories(incoming);
		Files.createDirectories(processing);
		Files.createDirectories(rejected);
	}
	//validate the file and move it to processing Folder for parsing
	public Path getFileForProcessing() throws IOException, InvalidFileNameException,InvalidInputFileException{
		try(DirectoryStream<Path> stream=Files.newDirectoryStream(incoming,"*.xml")){
			for(Path file:stream) {
				BasicFileAttributes attribute=Files.readAttributes(file,BasicFileAttributes.class);
				
				if(!attribute.isRegularFile()) {
					
					Path rejectedFile = rejected.resolve(file.getFileName());
					Files.move(file, rejectedFile,StandardCopyOption.REPLACE_EXISTING);
					
					System.out.println("Invalid Input file Moved to rejected: "+file.getFileName()+"\n");
					continue;
				}
				if(attribute.size()==0) {
					
					Path rejectedFile = rejected.resolve(file.getFileName());
					Files.move(file, rejectedFile,StandardCopyOption.REPLACE_EXISTING);
					
					System.out.println("Empty Input file Moved to rejected: "+file.getFileName()+"\n");
					continue;
				}
				
				if(!isValidFileName(file.getFileName().toString())) {
					
					Path rejectedFile = rejected.resolve(file.getFileName());
					
					Files.move(file, rejectedFile,StandardCopyOption.REPLACE_EXISTING);
					
					System.out.println("file Moved to rejected: "+file.getFileName()+" ( Invalid File Name )"+"\n");
					continue;
				}
				
				Path target=processing.resolve(file.getFileName());
				
				Files.move(file, target, StandardCopyOption.REPLACE_EXISTING);
				return target;
				
			}
			
		}
		return null;
	}
	//check if the file name is valid
	private boolean isValidFileName(String fileName) {

        return fileName.matches(
                "TXN_[A-Za-z0-9]+_\\d{8}_\\d+\\.xml"
        );
    }
	
	
	
}
