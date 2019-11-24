package bg.sofia.uni.fmi.mjt.stylechecker.line;

import java.util.Collection;

import bg.sofia.uni.fmi.mjt.stylechecker.Warning;

public class PackageLine extends LengthDependentLine {
    private static final char UNDERSCORE = '_';
    private static final String UPPERCASE_REGEX = ".*[A-Z].*";

    public PackageLine(String line) {
        super(line);
    }

    @Override
    public Collection<Warning> getWarnings() {
        Collection<Warning> warnings = super.getWarnings();

        if (hasUpperCase() || hasUnderscore()) {
            warnings.add(Warning.PACKAGE_STATEMENT);
        }

        return warnings;
    }

    public boolean hasUpperCase() {
        return super.getLine().matches(UPPERCASE_REGEX);
    }

    public boolean hasUnderscore() {
        return super.getLine().indexOf(UNDERSCORE) != -1;
    }
}
