package bg.sofia.uni.fmi.mjt.stylechecker.line;

public class LineFactory {
    private static final String IMPORT = "import";
    private static final String PACKAGE = "package";

    public Line makeLine(String line) {
        line = line.trim();

        if (line.startsWith(IMPORT)) {
            return new ImportLine(line);
        }

        if (line.startsWith(PACKAGE)) {
            return new PackageLine(line);
        }

        return new RegularLine(line);
    }

}
