package bg.sofia.uni.fmi.mjt.stylechecker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;

import bg.sofia.uni.fmi.mjt.stylechecker.line.Line;
import bg.sofia.uni.fmi.mjt.stylechecker.line.LineFactory;

public class StyleChecker {
    private LineFactory factory;

    public StyleChecker() {
        factory = new LineFactory();
    }

    public void checkStyle(Reader source, Writer output) {
        try (BufferedReader reader = new BufferedReader(source);
                BufferedWriter writer = new BufferedWriter(output)) {
            String line = reader.readLine();

            while (line != null) {
                Line currentLine = factory.makeLine(line);

                printWarnings(currentLine.getWarnings(), writer);

                writer.write(line);
                
                line = reader.readLine();

                if (line != null) {
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printWarnings(Collection<Warning> warnings, Writer output) throws IOException {
        for (Warning warning : warnings) {
            output.write(warning.getMessage());
            output.write(System.lineSeparator());
        }
    }
}