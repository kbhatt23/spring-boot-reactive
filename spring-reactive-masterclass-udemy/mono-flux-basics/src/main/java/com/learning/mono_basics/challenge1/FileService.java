package com.learning.mono_basics.challenge1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import com.learning.mono_basics.utils.ThreadUtils;

public class FileService {

	private final String filePath;
	
	private  String data;

	public FileService(String filePath) {
		this.filePath = filePath;
	}
	
	public FileService(String filePath, String data) {
		this.data = data;
		this.filePath = filePath;
	}

	// read all line of file and share the data in single string
	public String readFile() throws IOException {
		System.out.println("readFile called for path " + filePath);

		// time consuming task
		ThreadUtils.sleep(2000);

		return Files.lines(Paths.get(filePath)).collect(Collectors.joining(","));
	}

	// good choice for runnable -> no input no output no exception
	public void writeFile() {
		System.out.println("writeFile called for path " + filePath);

		// time consuming task
		ThreadUtils.sleep(2000);
		
		try {
			Files.write(Paths.get(filePath), data.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void deleteFile() {
		System.out.println("deleteFile called for path " + filePath);

		// time consuming task
		ThreadUtils.sleep(2000);
		
		try {
			Files.deleteIfExists(Paths.get(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
