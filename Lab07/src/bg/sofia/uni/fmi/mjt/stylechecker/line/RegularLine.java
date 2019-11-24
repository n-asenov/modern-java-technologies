package bg.sofia.uni.fmi.mjt.stylechecker.line;

import java.util.Collection;

import bg.sofia.uni.fmi.mjt.stylechecker.Warning;

public class RegularLine extends LengthDependentLine {
    private static final String OPENING_BRACKET = "{";
    
    public RegularLine(String line) {
        super(line);
    }

    @Override
    public Collection<Warning> getWarnings() {
        Collection<Warning> warnings = super.getWarnings();
        
        if (hasOnlyOpeningBracket()) {
            warnings.add(Warning.OPENING_BRACKET);
        }
        
        return warnings;
    }
    
    public boolean hasOnlyOpeningBracket() {
        return super.getLine().equals(OPENING_BRACKET);
    }

}
