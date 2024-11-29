import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Scanner {
    private static final Set<String> keywords = new HashSet<>(Arrays.asList(
        "and", "class", "else", "false", "for", "fun", "if", "nil", "or",
        "print", "return", "super", "this", "true", "var", "while"
    ));

    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;
    private boolean hasErrors = false;

    public Scanner(String source) {
        this.source = source;
    }

    public List<Token> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }
        tokens.add(new Token("EOF", "", "null", line));
        return tokens;
    }

    private void scanToken() {
        char c = advance();
        switch (c) {
            case '(' -> addToken("LEFT_PAREN", "(");
            case ')' -> addToken("RIGHT_PAREN", ")");
            case '{' -> addToken("LEFT_BRACE", "{");
            case '}' -> addToken("RIGHT_BRACE", "}");
            case ',' -> addToken("COMMA", ",");
            case '.' -> addToken("DOT", ".");
            case '-' -> addToken("MINUS", "-");
            case '+' -> addToken("PLUS", "+");
            case ';' -> addToken("SEMICOLON", ";");
            case '*' -> addToken("STAR", "*");
            case '!' -> {
                if (match('=')) {
                    addToken("BANG_EQUAL", "!=");
                } else {
                    addToken("BANG", "!");
                }
            }
            case '=' -> {
                if (match('=')) {
                    addToken("EQUAL_EQUAL", "==");
                } else {
                    addToken("EQUAL", "=");
                }
            }
            case '<' -> {
                if (match('=')) {
                    addToken("LESS_EQUAL", "<=");
                } else {
                    addToken("LESS", "<");
                }
            }
            case '>' -> {
                if (match('=')) {
                    addToken("GREATER_EQUAL", ">=");
                } else {
                    addToken("GREATER", ">");
                }
            }
            case '/' -> {
                if (match('/')) {
                    // A comment goes until the end of the line.
                    while (peek() != '\n' && !isAtEnd()) advance();
                } else {
                    addToken("SLASH", "/");
                }
            }
            case ' ', '\r', '\t' -> {} // Ignore whitespace
            case '\n' -> line++;
            case '"' -> string();
            default -> {
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    error("Unexpected character: " + c);
                }
            }
        }
    }

    private void string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++;
            advance();
        }

        if (isAtEnd()) {
            error("Unterminated string.");
            return;
        }

        // The closing ".
        advance();

        // Trim the surrounding quotes.
        String value = source.substring(start + 1, current - 1);
        addToken("STRING", '"' + value + '"', value);
    }

    private void number() {
        while (isDigit(peek())) advance();

        // Look for a fractional part.
        if (peek() == '.' && isDigit(peekNext())) {
            // Consume the "."
            advance();

            while (isDigit(peek())) advance();
        }

        String number = source.substring(start, current);
        String value = formatNumber(number);
        addToken("NUMBER", number, value);
    }

    private String formatNumber(String number) {
        int dotIndex = number.indexOf('.');
        if (dotIndex == -1) {
            return number + ".0";
        } else {
            String fractionalPart = NumberUtils.removeTrailingZeroes(number.substring(dotIndex));
            return number.substring(0, dotIndex) + (fractionalPart.isEmpty() ? ".0" : fractionalPart);
        }
    }

    private void identifier() {
        while (isAlphaNumeric(peek())) advance();

        String text = source.substring(start, current);
        String type = keywords.contains(text) ? text.toUpperCase() : "IDENTIFIER";
        addToken(type, text);
    }

    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;

        current++;
        return true;
    }

    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
               (c >= 'A' && c <= 'Z') ||
                c == '_';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private char advance() {
        return source.charAt(current++);
    }

    private void addToken(String type, String lexeme) {
        addToken(type, lexeme, "null");
    }

    private void addToken(String type, String lexeme, String literal) {
        tokens.add(new Token(type, lexeme, literal, line));
    }

    private void error(String message) {
        System.err.println("[line " + line + "] Error: " + message);
        hasErrors = true;
    }

    public boolean hasErrors() {
        return hasErrors;
    }
} 