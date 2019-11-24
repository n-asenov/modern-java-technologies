package bg.sofia.uni.fmi.mjt.stylechecker.line;

import java.util.Collection;

import bg.sofia.uni.fmi.mjt.stylechecker.Warning;

public interface Checkable {
    Collection<Warning> getWarnings();
}
