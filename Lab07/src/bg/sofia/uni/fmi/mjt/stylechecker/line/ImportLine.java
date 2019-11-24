package bg.sofia.uni.fmi.mjt.stylechecker.line;

import java.util.Collection;

import bg.sofia.uni.fmi.mjt.stylechecker.Warning;

public class ImportLine extends Line {
    private static final char WILDCARD = '*';

    public ImportLine(String line) {
        super(line);
    }

    @Override
    public Collection<Warning> getWarnings() {
        Collection<Warning> warnings = super.getWarnings();

        if (hasWildcard()) {
            warnings.add(Warning.IMPORT_STATEMENT);
        }

        return warnings;
    }

    public boolean hasWildcard() {
        return super.getLine().indexOf(WILDCARD) != -1;
    }
}
