import java.util.List;
import java.util.Stack;

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
        Stack<String> operands = new Stack<>();
        Stack<Token> operators = new Stack<>();

        while (!isAtEnd()) {
            Token token = advance();
            if (token.getType().equals("NUMBER")) {
                operands.push(getLiteralValue(token));
            } else if (isBinaryOperator(token)) {
                while (!operators.isEmpty() && hasPrecedence(token, operators.peek())) {
                    processOperator(operands, operators);
                }
                operators.push(token);
            }else if (token.getType().equals("LEFT_PAREN")) {
                parseGroup();
                System.out.println();
            } else if (isUnaryOperator(token)) {
                parseUnary(token);
                System.out.println();
            } else {
                System.out.println(getLiteralValue(token));
            }
        }

        while (!operators.isEmpty()) {
            processOperator(operands, operators);
        }

        System.out.println(operands.pop());
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

    private void processOperator(Stack<String> operands, Stack<Token> operators) {
        Token operator = operators.pop();
        String right = operands.pop();
        String left = operands.pop();
        String result = "(" + getLiteralValue(operator) + " " + left + " " + right + ")";
        operands.push(result);
    }

    private boolean hasPrecedence(Token current, Token top) {
        // * and / have higher precedence than + and -
        if ((current.getType().equals("STAR") || current.getType().equals("SLASH")) &&
            (top.getType().equals("PLUS") || top.getType().equals("MINUS"))) {
            return false;
        }
        return true;
    }

    private boolean isBinaryOperator(Token token) {
        return token.getType().equals("STAR") || 
               token.getType().equals("SLASH") ||
               token.getType().equals("PLUS") || 
               token.getType().equals("MINUS");
    }

    private String getLiteralValue(Token token) {
        return switch (token.getType()) {
            case "TRUE" -> "true";
            case "FALSE" -> "false";
            case "NIL" -> "nil";
            case "NUMBER", "STRING" -> token.getLiteral();
            case "BANG" -> "!";
            case "MINUS" -> "-";
            case "STAR" -> "*";
            case "SLASH" -> "/";
            default -> "";
        };
    }
}