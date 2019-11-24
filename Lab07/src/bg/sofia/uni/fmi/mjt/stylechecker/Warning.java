package bg.sofia.uni.fmi.mjt.stylechecker;

public enum Warning {
    IMPORT_STATEMENT("// FIXME Wildcards are not allowed in import statements"),
    STATEMENTS_IN_LINE("// FIXME Only one statement per line is allowed"),
    LINE_LENGTH("// FIXME Length of line should not exceed 100 characters"),
    OPENING_BRACKET(
            "// FIXME Opening brackets should be placed on the same line as the declaration"),
    PACKAGE_STATEMENT(
            "// FIXME Package name should not contain upper-case letters or underscores");

    private final String message;

    private Warning(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
