package org.daniel.control;

import org.daniel.exception.EventStoreBuilderException;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Instant;
import java.time.ZoneOffset;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.File;

import com.google.gson.JsonObject;
import com.google.gson.Gson;


public class EventBuilder implements EventStore {
	private String storageDirectory;

	public EventBuilder(String storageDirectory) {
		this.storageDirectory = storageDirectory;
	}

	private void createFolder(String folderPath) throws EventStoreBuilderException {
		File folder = new File(folderPath);
		if (!folder.exists()) {
			boolean folderCreated = folder.mkdirs();
			if (folderCreated) {
				System.out.println("Folder created successfully: " + folder.getAbsolutePath());
			} else {
				throw new EventStoreBuilderException("Could not create the folder: " + folder.getAbsolutePath(), null);
			}
		} else {
			System.out.println("Folder already exists: " + folder.getAbsolutePath());
		}
	}

	private String formatDate(String timestamp) {
		Instant instant = Instant.parse(timestamp);
		ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneOffset.UTC);
		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		return zonedDateTime.format(outputFormatter);
	}

	private void writeDataToFile(String filePath, String data) throws EventStoreBuilderException {
		try (FileWriter fileWriter = new FileWriter(filePath, true);
			 BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
			bufferedWriter.write(data);
			bufferedWriter.newLine();
			System.out.println("Data written successfully to: " + filePath);
		} catch (IOException e) {
			throw new EventStoreBuilderException("Error writing to file: " + e.getMessage(), e);
		}
	}

	@Override
	public void save(String data) throws EventStoreBuilderException {
		Gson gson = new Gson();
		JsonObject eventData = gson.fromJson(data, JsonObject.class);
		String source = eventData.get("source").getAsString();
		String timestamp = eventData.get("timestamp").getAsString();
		String formattedTimestamp = formatDate(timestamp);
		String folderPath = storageDirectory + "/eventStorage/prediction.Weather/" + source;
		createFolder(folderPath);
		String filePath = folderPath + "/" + formattedTimestamp + ".events";
		writeDataToFile(filePath, data);
	}

}


