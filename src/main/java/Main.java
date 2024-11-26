import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
  public static void main(String[] args) {

    if (args.length < 2) {
      System.err.println("Usage: ./your_program.sh tokenize <filename>");
      System.exit(1);
    }

    String command = args[0];
    String filename = args[1];

    boolean hasErrors = false;

    if (!command.equals("tokenize")) {
      System.err.println("Unknown command: " + command);
      System.exit(1);
    }

    String fileContents = "";
    try {
      fileContents = Files.readString(Path.of(filename));
    } catch (IOException e) {
      System.err.println("Error reading file: " + e.getMessage());
      System.exit(1);
    }

    if (fileContents.length() > 0) {
      for (int idx = 0; idx < fileContents.length(); idx++) {
        char c = fileContents.charAt(idx);
        switch (c) {
          case '(' -> System.out.println("LEFT_PAREN ( null");
          case ')' -> System.out.println("RIGHT_PAREN ) null");
          case '{' -> System.out.println("LEFT_BRACE { null");
          case '}' -> System.out.println("RIGHT_BRACE } null");
          case '.' -> System.out.println("DOT . null");
          case ',' -> System.out.println("COMMA , null");
          case ';' -> System.out.println("SEMICOLON ; null");
          case '+' -> System.out.println("PLUS + null");
          case '-' -> System.out.println("MINUS - null");
          case '*' -> System.out.println("STAR * null");
          case '=' -> {
            if (idx + 1 < fileContents.length() && fileContents.charAt(idx + 1) == '=') {
              System.out.println("EQUAL_EQUAL == null");
              ++idx;
            } else {
              System.out.println("EQUAL = null");
            }
          }
          case '!' -> {
            if (idx + 1 < fileContents.length() && fileContents.charAt(idx + 1) == '=') {
              System.out.println("BANG_EQUAL != null");
              ++idx;
            } else {
              System.out.println("BANG ! null");
            }
          }
          case '<' -> {
            if (idx + 1 < fileContents.length() && fileContents.charAt(idx + 1) == '=') {
              System.out.println("LESS_EQUAL <= null");
              ++idx;
            } else {
              System.out.println("LESS < null");
            }
          }
          case '>' -> {
            if (idx + 1 < fileContents.length() && fileContents.charAt(idx + 1) == '=') {
              System.out.println("GREATER_EQUAL >= null");
              ++idx;
            } else {
              System.out.println("GREATER > null");
            }
          }
          default -> {
            System.err.println("[line 1] Error: Unexpected character: " + c);
            hasErrors = true;
          }
        }
      }
    }
    System.out.println("EOF  null");

    if (hasErrors) {
      System.exit(65);
    } else {
      System.exit(0);
    }
  }
}
