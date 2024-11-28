import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
  
  private static String removeTrailingZeroes(String s) {
    int index;
    for (index = s.length() - 1; index >= 0; index--) {
      if (s.charAt(index) != '0') {
        break;
      }
    }
    if (index == 0 && s.charAt(index) == '.') {
      return "";
    }
    return s.substring(0, index + 1);
  }

  private static int lineNumber = 1;

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

        // if (Character.isWhitespace(c)) continue; // Ignore whitespace characters
        // using Character Class
        if (c == ' ' || c == '\t')
          continue;

        // For every new line we will count number
        if (c == '\n') {
          ++lineNumber;
          continue;
        }

        // Check for number literals
        if (Character.isDigit(c)) {
          // Start scanning an integer or floating-point literal
          int startIdx = idx;
          boolean isFloat = false;

          while (idx < fileContents.length() && (Character.isDigit(fileContents.charAt(idx)) || fileContents.charAt(idx) == '.')) {
            if (fileContents.charAt(idx) == '.') {
              isFloat = true; // It's a float
            }
            idx++;
          }

          // Extract the number
          String number = fileContents.substring(startIdx, idx);
          
          if (isFloat) {
            int dotIndex = number.indexOf('.');
            
              String fractionalPart = removeTrailingZeroes(number.substring(dotIndex));
              // Check if the modified number still contains a dot
              if (fractionalPart.isEmpty()) {
                System.out.println("NUMBER " + number + " " + number.substring(0, dotIndex) + ".0"); // Display original and modified number
              } else {
                System.out.println("NUMBER " + number + " " + number.substring(0, dotIndex) + fractionalPart); // Display original number with .0
              }
            
          } else {
            System.out.println("NUMBER " + number + " " + number + ".0");
          }
          continue;
        }

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
          case '/' -> {
            if (idx + 1 < fileContents.length() && fileContents.charAt(idx + 1) == '/') {
              while (idx < fileContents.length() && fileContents.charAt(idx) != '\n') {
                idx++;
              }
              ++lineNumber;
            } else {
              System.out.println("SLASH / null");
            }
          }

          // Check String Literals
          case '"' -> {
            int startIdx = idx;
            idx++;
            while (idx < fileContents.length() && fileContents.charAt(idx) != '"') {
              if (fileContents.charAt(idx) == '\n') {
                ++lineNumber;
              }
              ++idx;
            }
            if (idx >= fileContents.length()) {
              System.err.println("[line " + lineNumber + "] Error: Unterminated string.");
              hasErrors = true;
            } else {
              String lexeme = fileContents.substring(startIdx, idx + 1); // Include quotes
              String value = fileContents.substring(startIdx + 1, idx); // Exclude quotes
              System.out.println("STRING " + lexeme + " " + value);
            }
          }

          default -> {
            System.err.println("[line " + lineNumber + "] Error: Unexpected character: " + c);
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
