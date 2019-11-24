package bg.sofia.uni.fmi.mjt.stylechecker.line;

import java.util.Collection;

import bg.sofia.uni.fmi.mjt.stylechecker.Warning;

public abstract class LengthDependentLine extends Line {
    private static final int MAX_LENGTH = 100;

    public LengthDependentLine(String line) {
        super(line);
    }

    @Override
    public Collection<Warning> getWarnings() {
        Collection<Warning> warnings = super.getWarnings();

        if (!isUnder100Characters()) {
            warnings.add(Warning.LINE_LENGTH);
        }

        return warnings;
    }

    public boolean isUnder100Characters() {
        return super.getLine().length() <= MAX_LENGTH;
    }
}
