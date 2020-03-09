package bg.sofia.uni.fmi.mjt.cache.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;

public abstract class Storage {
	private static final String NEW_LINE = System.lineSeparator();

	protected static final Gson GSON = new Gson();

	private File storage;

	public Storage(String storageName) throws IOException {
		storage = new File(storageName);
		try {
			storage.createNewFile();
		} catch (IOException e) {
			throw new IOException("Could not create food storage file", e);
		}
	}

	public File getStorage() {
		return storage;
	}

	public void saveObjectData(Object object) throws IOException {
		try (FileWriter writer = new FileWriter(storage, true)) {
			writer.write(GSON.toJson(object));
			writer.write(NEW_LINE);
			writer.flush();
		}
	}

}
