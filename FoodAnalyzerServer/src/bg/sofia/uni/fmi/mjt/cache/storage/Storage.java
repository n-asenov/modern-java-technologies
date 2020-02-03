package bg.sofia.uni.fmi.mjt.cache.storage;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public abstract class Storage  {
    private ObjectInputStream input;
    private ObjectOutputStream output;
    
    public Storage(InputStream input, OutputStream output) throws IOException {
        try {
            this.output = new ObjectOutputStream(output);
            this.input = new ObjectInputStream(input);
        } catch (IOException e) {
            throw new IOException("Failed to create cache storage");
        }
    }
    
    public ObjectInputStream getInput() {
        return input;
    }

    public void saveObjectData(Object object) throws IOException {
        output.writeObject(object);
    }
    
    public void close() throws IOException {
        if (input != null) {
            input.close();
        }
        
        if (output != null) {
            output.close();
        }
    }
    
}
