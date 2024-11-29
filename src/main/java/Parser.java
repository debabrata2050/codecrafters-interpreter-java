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
      if (isAtEnd())
        return;
      Token token = peek();
      if (token.getType().equals("TRUE")) {
        System.out.println("true");
        advance();
      } else if (token.getType().equals("FALSE")) {
        System.out.println("false");
        advance();
      } else if (token.getType().equals("NIL")) {
        System.out.println("nil");
        advance();
      } else if (token.getType().equals("NUMBER")) {
        System.out.println(token.getLiteral());
        advance();
      } else if (token.getType().equals("STRING")) {
        System.out.println(token.getLiteral());
        advance();
      } else if (token.getType().equals("LEFT_PAREN")) {
        System.out.print("(");
        advance();
        doParse(); // recursively parse the contents inside parentheses
        if (check(tokenList.get(current))) {
          System.out.print(")");
          advance();
        }
      }
    }
  }