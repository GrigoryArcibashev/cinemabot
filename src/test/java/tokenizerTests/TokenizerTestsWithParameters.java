package tokenizerTests;

import dataIO.inputModule.Lexeme;
import inputModuleForTests.InputModuleForTests;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import tokenizer.Token;
import tokenizer.Tokenizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(Parameterized.class)
public class TokenizerTestsWithParameters {
    private final Lexeme currentLexeme;
    private final Token currentExpectedToken;

    private static final InputModuleForTests inputModule;
    private static final Tokenizer tokenizer;

    static {
        inputModule = new InputModuleForTests(new ArrayList<>());
        tokenizer = new Tokenizer(inputModule);
    }

    public TokenizerTestsWithParameters(final Lexeme lexeme, final Token expectedToken) {
        this.currentLexeme = lexeme;
        this.currentExpectedToken = expectedToken;
    }

    @Test
    public void test() {
        ArrayList<Lexeme> lexemes = new ArrayList<>();
        lexemes.add(currentLexeme);
        inputModule.setNewLexemes(lexemes);

        Token actualToken = tokenizer.getNextToken();

        TokenizerTests.areExpectedTokenAndActualTokenEqual(currentExpectedToken, actualToken);
    }

    @Parameterized.Parameters
    public static List<Object[]> getParametersForTests() {
        String userID = "userID";
        return Arrays.asList(new Object[][]{
                {new Lexeme(" ", userID), null},
                {new Lexeme("   ", userID), null},
                {new Lexeme(" command  ", userID), new Token("command", new String[0], userID)},
                {new Lexeme("\n", userID), null},
                {new Lexeme("\r", userID), null},
                {new Lexeme("\n\r", userID), null},
                {new Lexeme("\r\r\n\r\n\n", userID), null},
                {new Lexeme("\r   \r\n ", userID), null},
                {new Lexeme("\r\ncommand\n", userID), new Token("command", new String[0], userID)},
                {new Lexeme("\r\ncommand \n", userID), new Token("command", new String[0], userID)},
                {new Lexeme("\r  \ncommand \n ", userID), new Token("command", new String[0], userID)},
                {new Lexeme("\ncommand arg1\n  ", userID), new Token("command", new String[]{"arg1"}, userID)},
                {new Lexeme("command\narg1", userID), new Token("command" + "arg1", new String[0], userID)},
                {new Lexeme("command \narg1", userID), new Token("command", new String[]{"arg1"}, userID)},
                {new Lexeme("command arg1\n arg2 ", userID), new Token("command", new String[]{"arg1", "arg2"}, userID)},
                {new Lexeme("command arg1   arg2", userID), new Token("command", new String[]{"arg1", "arg2"}, userID)}
        });
    }
}
