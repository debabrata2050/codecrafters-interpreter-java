public class Token {
    private final String type;
    private final String lexeme;
    private final String literal;
    private final int line;

    public Token(String type, String lexeme, String literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }
    public String getType() {
        return type;
    }

    public String getLexeme() {
        return lexeme;
    }

    public String getLiteral() {
        return literal;
    }

    public int getLine() {
        return line;
    }
    @Override
    public String toString() {
        return type + " " + lexeme + " " + literal;
    }
} 