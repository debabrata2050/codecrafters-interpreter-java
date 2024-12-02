import java.util.List;

public class Parser {
    private List<Token> tokenList;
    private int current = 0;

    public Parser(List<Token> tokenList) { 
        this.tokenList = tokenList; 
    }
    
    private boolean check(Token type) {
        if (isAtEnd()) return false;
        return peek().getType() == type.getType();
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().getType().equals("EOF");
    }
    
    private Token peek() {
        return tokenList.get(current);
    }
    
    private Token previous() {
        return tokenList.get(current - 1);
    }

    public void doParse() {
        while (!isAtEnd()) {
            Token token = advance();
            if (token.getType().equals("LEFT_PAREN")) {
                parseGroup();
                System.out.println();
            } else if (isUnaryOperator(token)) {
                parseUnary(token);
                System.out.println();
            } else {
                System.out.println(getLiteralValue(token));
            }
        }
    }

    private boolean isUnaryOperator(Token token) {
        return token.getType().equals("BANG") || token.getType().equals("MINUS");
    }

    private void parseUnary(Token operator) {
        System.out.print("(" + getLiteralValue(operator) + " ");
        
        Token next = peek();
        if (next.getType().equals("LEFT_PAREN")) {
            advance(); // consume LEFT_PAREN
            parseGroup();
        } else if (isUnaryOperator(next)) {
            advance(); // consume next operator
            parseUnary(next);
        } else {
            Token value = advance();
            System.out.print(getLiteralValue(value));
        }
        
        System.out.print(")");
    }

    private void parseGroup() {
        System.out.print("(group ");

        while (!isAtEnd() && !check(new Token("RIGHT_PAREN", ")", null, current))) {
            Token innerToken = peek();
            if (innerToken.getType().equals("LEFT_PAREN")) {
                advance(); // consume LEFT_PAREN
                parseGroup();  // Recursively handle nested groups
            } else if (isUnaryOperator(innerToken)) {
                advance(); // consume operator
                parseUnary(innerToken);
            } else {
                advance(); // consume token
                System.out.print(getLiteralValue(innerToken));
            }
        }

        if (check(new Token("RIGHT_PAREN", ")", null, current))) {
            advance();
        }

        System.out.print(")");
    }

    private String getLiteralValue(Token token) {
        return switch (token.getType()) {
            case "TRUE" -> "true";
            case "FALSE" -> "false";
            case "NIL" -> "nil";
            case "NUMBER", "STRING" -> token.getLiteral();
            case "BANG" -> "!";
            case "MINUS" -> "-";
            default -> "";
        };
    }
}