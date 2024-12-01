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
    //< check
    //> advance
      private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
      }
    //< advance
    //> utils
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
            } else if (token.getType().equals("MINUS") || token.getType().equals("BANG")) {
                parseUnary(token);
            } else {
                System.out.println(getLiteralValue(token));
            }
        }
    }

    private void parseGroup() {
        // Print the opening of the group
        System.out.print("(group ");

        // Process all tokens until we hit the closing parenthesis
        while (!isAtEnd() && !check(new Token("RIGHT_PAREN", ")", null, current))) {
            Token innerToken = advance();
            if (innerToken.getType().equals("LEFT_PAREN")) {
                parseGroup();  // Recursively handle nested groups
            } else {
                System.out.print(getLiteralValue(innerToken));
            }
        }

        // Consume the closing parenthesis if present
        if (check(new Token("RIGHT_PAREN", ")", null, current))) {
            advance();
        }

        // Close the group
        System.out.print(")");  // Changed from println to print for nested groups
    }

    private void parseUnary(Token operator) {
        // Print the opening of the unary expression
        System.out.print("(" + getOperatorSymbol(operator) + " ");

        // Parse the operand
        Token operand = advance();
        if (operand.getType().equals("LEFT_PAREN")) {
            parseGroup();
        } else if (operand.getType().equals("MINUS") || operand.getType().equals("BANG")) {
            parseUnary(operand);  // Recursively handle nested unary operators
        } else {
            System.out.print(getLiteralValue(operand));
        }

        // Close the unary expression
        System.out.print(")");  // Changed from println to print for nested expressions
    }

    private String getOperatorSymbol(Token token) {
        return switch (token.getType()) {
            case "MINUS" -> "-";
            case "BANG" -> "!";
            default -> "";
        };
    }

    private String getLiteralValue(Token token) {
        return switch (token.getType()) {
            case "TRUE" -> "true";
            case "FALSE" -> "false";
            case "NIL" -> "nil";
            case "NUMBER", "STRING" -> token.getLiteral();
            default -> "";
        };
    }
  }