package bg.sofia.uni.fmi.mjt.stylechecker.line;

import java.util.Collection;
import java.util.EnumSet;

import bg.sofia.uni.fmi.mjt.stylechecker.Warning;

public abstract class Line implements Checkable {
    private static final char SEMICOLON = ';';

    private final String line;

    public Line(String line) {
        this.line = line;
    }
    
    public String getLine() {
        return line;
    }

    public boolean hasOnlyOneStatement() {
        int semicolonIndex = line.indexOf(SEMICOLON);

        while (semicolonIndex != -1) {
            int nextSemicolonIndex = line.indexOf(SEMICOLON, semicolonIndex + 1);

            if (nextSemicolonIndex != -1 && nextSemicolonIndex != semicolonIndex + 1) {
                return false;
            }

            semicolonIndex = nextSemicolonIndex;
        }

        return true;
    }

    @Override
    public Collection<Warning> getWarnings() {
        EnumSet<Warning> warnings = EnumSet.noneOf(Warning.class);

        if (!hasOnlyOneStatement()) {
            warnings.add(Warning.STATEMENTS_IN_LINE);
        }

        return warnings;
    }
}
