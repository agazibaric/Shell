package hr.fer.zemris.java.hw07.shell.parser;

import java.util.LinkedList;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.lexer.LexerState;
import hr.fer.zemris.java.hw07.shell.lexer.ShellLexer;
import hr.fer.zemris.java.hw07.shell.lexer.ShellLexerException;
import hr.fer.zemris.java.hw07.shell.lexer.TokenType;

/**
 * Class represents a syntax analyzer for command-line program MyShell. </br>
 * 
 * @author Ante Gazibarić
 * @version 1.0
 *
 */
public class ShellParser {

	/** lexical analyzer */
	private ShellLexer lexer;
	/** list of inputs */
	private List<String> inputs = new LinkedList<>();
	
	/**
	 * Constructor for creating new {@code ShellParser} object.
	 * 
	 * @param input {@code String} that is analyzed
	 */
	public ShellParser(String input, boolean toEscape) {
		lexer = new ShellLexer(input);
		setLexerState(toEscape);
		parseInput();
	}
	
	/**
	 * Method that analyzes input query
	 */
	private void parseInput() {
		try {
			lexer.nextToken();
			while (!isTokenOfType(TokenType.EOF)) {
				String input = (String) lexer.getToken().getValue();
				inputs.add(input);
				lexer.nextToken();
			}
		} catch (ShellLexerException ex) {
			throw new ShellParserException(ex.getMessage());
		}
	}
	
	/**
	 * Method returns list of inputs
	 * 
	 * @return list of inputs
	 */
	public List<String> getInputs() {
		return inputs;
	}
	
	/**
	 * Method sets lexer to appropriate state.
	 * 
	 * @param toEscape flag that distinguishes between two states of lexer
	 */
	private void setLexerState(boolean toEscape) {
		if (toEscape) {
			lexer.setLexerState(LexerState.WITH_ESCAPING);
		} else {
			lexer.setLexerState(LexerState.NO_ESCAPING);
		}
	}
	
	/**
	 * Method that checks if a current token is of a given type.
	 * 
	 * @param type {@link TokenType} whose equality is checked
	 * @return     <code>true</code> if current token type is equal to given token type, </br>
	 * 			   <code>false</code> otherwise
	 */
	private boolean isTokenOfType(TokenType type) {
		return lexer.getToken().getType() == type;
	}
	
}
