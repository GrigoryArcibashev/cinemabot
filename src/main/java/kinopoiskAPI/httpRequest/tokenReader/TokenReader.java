package kinopoiskAPI.httpRequest.tokenReader;

import java.io.*;

public class TokenReader {
    private static final String pathToToken;

    static {
        pathToToken = "src/main/java/kinopoiskAPI/httpRequest/tokenReader/token";
    }

    public static String ReadToken() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(pathToToken))) {
            return bufferedReader.readLine();
        } catch (FileNotFoundException e) {
            System.out.println(
                    """
                            The token was not found
                            The token can be obtained on the website https://kinopoiskapiunofficial.tech/
                            Copy the obtained token to a file 'token', then place this file in the
                            src/main/java/kinopoiskAPI/HTTPRequest/TokenReader/ directory""");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("The token could not be read");
            System.exit(2);
        }
        return null;
    }
}