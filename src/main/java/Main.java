import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: ./your_program.sh [command] <filename>");
            System.exit(1);
        }

        String command = args[0];
        String filename = args[1];

        try {
            String source = Files.readString(Path.of(filename));
            
            if (command.equals("tokenize")) {
                Scanner scanner = new Scanner(source);
                List<Token> tokens = scanner.scanTokens();
                tokens.forEach(System.out::println);
                
                if (scanner.hasErrors()) {
                    System.exit(65);
                }
            } else if (command.equals("parse")) {
                Scanner scanner = new Scanner(source);
                List<Token> tokens = scanner.scanTokens();
                if (scanner.hasErrors()) {
                    System.exit(65);
                }

                Parser parser = new Parser(tokens);
                parser.doParse();
                
            } else {
                System.err.println("Unknown command: " + command);
                System.exit(1);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.exit(1);
        }
    }
}
