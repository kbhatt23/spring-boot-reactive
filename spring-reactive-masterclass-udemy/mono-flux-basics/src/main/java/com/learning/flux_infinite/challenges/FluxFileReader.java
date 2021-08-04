package com.learning.flux_infinite.challenges;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.learning.mono_basics.utils.DefaultSubscriber;
import com.learning.mono_basics.utils.ThreadUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;

public class FluxFileReader {

	public static void main(String[] args) {
		String filePath = "files/names.txt";
		handleUsingStream(filePath);
		
//		/handleUsingList(filePath);
		
	}

	private static void handleUsingList(String filePath) {
		//using list is bad here
		//cleitn is blocked untill all the task is done
		//in stream we can provide some data 
		
		int line = 0;
		List<String> lines= new ArrayList<>();
		while(true) {
			String readFileLine = readFileLine(line, filePath);
			if(readFileLine == null) {
				break;
			}
			lines.add(readFileLine);
			line++;
		}
		
		System.out.println("lines recieved "+lines);
		
		
	}

	private static void handleUsingStream(String filePath) {
		readLines(filePath)
			.subscribeWith(new DefaultSubscriber<>(true, FluxFileReader.class.getSimpleName()));
	}

	private static Flux<String> readLines(String filePath) {

		// assume each line fetching is time consuming
		// so if we do realall line at once there is no back pressure
		// also client is blocked until all the data comes

		// calls in loop
		// each time one line is fetched as it is time consuming
		return Flux.generate( () -> new FileState(0, filePath)
				, FluxFileReader::fileIterate);
		
	}

	// could be using offset lines
	// line number start from 0
	private static String readFileLine(int lineNumber, String filePath) {
		System.out.println("readFileLine: reading line "+lineNumber);

		// showcasting time aking task
		ThreadUtils.sleep(1000);
		String line = null;
		try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
			line = lines.skip(lineNumber).findFirst().orElse(null);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return line;
	}

	private static FileState fileIterate(FileState fileState, SynchronousSink<String> synchronousSink) {

		String readFileLine = readFileLine(fileState.getCurrentLine(), fileState.getFilePath());
		
		
		if(readFileLine == null) {
			synchronousSink.complete();
		}else {
			synchronousSink.next(readFileLine);
		}
		fileState.incrementLine();
		
		return fileState;
	}

}

@Data
@AllArgsConstructor
class FileState{
	private int currentLine;
	
	private String filePath;
	
	public void incrementLine() {
		currentLine++;
	}
}
