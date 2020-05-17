package hr.fer.zemris.java.hw07.shell.lexer;

import java.util.LinkedList;
import java.util.List;

/**
 * Class represents lexical analyzer for shell command-line program.
 * 
 * @author Ante Gazibarić
 * @version 1.0
 *
 */
public class ShellLexer {

	/** input string in chars */
	private char[] data;
	/** current token */
	private Token token;
	/** current data index to be processed */ 
	private int currentIndex;
	/** lexer state */
	private LexerState lexerState = LexerState.WITH_ESCAPING;
	
	/**
	 * Constructor that creates new {@code ShellLexer} object.
	 * 
	 * @param input that is analyzed
	 */
	public ShellLexer(String input) {
		if (input == null) 
			throw new NullPointerException("Query input must not be null");
		
		data = input.toCharArray();
	}
	
	/**
	 * Method returns current token.
	 * 
	 * @return current {@link Token}
	 */
	public Token getToken() {
		return token;
	}
	
	/**
	 * Method that returns next token.
	 * 
	 * @return next {@link Token}
	 */
	public Token nextToken() {
		switch (lexerState) {
		case WITH_ESCAPING:
			setNextTokenWithEscaping();
			break;
		case NO_ESCAPING:
			setNextTokenWithNoEscaping();
		}
		return token;
	}
	
	/**
	 * Method that does lexical analysis and sets next token
	 */
	private void setNextTokenWithEscaping() {
		
		if (token != null && token.getType() == TokenType.EOF)
			throw new ShellLexerException("There is no more tokens");

		skipWhitespaces();
		
		if (currentIndex >= data.length) {
			token = new Token(TokenType.EOF, null);
			return;
		}
		
		//if it's input given in double quotes
		List<Integer> indexes = new LinkedList<>();
		if (data[currentIndex] == '\"') {
			indexes.add(currentIndex);
			currentIndex++;
			
			while (true) {
				if (currentIndex >= data.length)
					throw new ShellLexerException("Wrong input. Missing one double quote.");
				
				if (data[currentIndex] == '\"') 
					break;
				
				if (data[currentIndex] == '\\') {
					indexes.add(currentIndex);
					currentIndex++;
				}
				currentIndex++;
			}
			indexes.add(currentIndex);
			
			String input = "";
			for (int i = 0, n = indexes.size(); i < n - 1; i++) {
				int firstIndex = indexes.get(i) + 1;
				int secondIndex = indexes.get(i + 1);
				String partOfInput = new String(data, firstIndex, secondIndex - firstIndex);
				input = input.concat(partOfInput);
			}
			
			token = new Token(TokenType.TEXT, input);
			currentIndex++;
			return;
		}
		
		// input without double quotes
		int beginningIndex = currentIndex;
		while(currentIndex < data.length && !isWhitespace(data[currentIndex])) {
			currentIndex++;	
		}
		
		String input = new String(data, beginningIndex, currentIndex - beginningIndex);
		token = new Token(TokenType.TEXT, input);
		currentIndex++;
	}
	
	private void setNextTokenWithNoEscaping() {
		
		if (token != null && token.getType() == TokenType.EOF)
			throw new ShellLexerException("There is no more tokens");

		skipWhitespaces();
		
		if (currentIndex >= data.length) {
			token = new Token(TokenType.EOF, null);
			return;
		}
		
		//if it's input given in double quotes
		if (data[currentIndex] == '\"') {
			currentIndex++;
			int beginningIndex = currentIndex;
			
			while (true) {
				if (currentIndex >= data.length)
					throw new ShellLexerException("Wrong input. Missing one double quote.");
				
				if (data[currentIndex] == '\"')
					break;
				
				currentIndex++;
			}
			
			String input = new String(data, beginningIndex, currentIndex - beginningIndex);
			token = new Token(TokenType.TEXT, input);
			currentIndex++;
			return;
		}
		
		// input without double quotes
		int beginningIndex = currentIndex;
		while(currentIndex < data.length && !isWhitespace(data[currentIndex])) {
			currentIndex++;	
		}
		
		String input = new String(data, beginningIndex, currentIndex - beginningIndex);
		token = new Token(TokenType.TEXT, input);
		currentIndex++;
	}
	
	public void setLexerState(LexerState lexerState) {
		this.lexerState = lexerState;
	}
	
	/**
	 * Private method for skipping any whitespaces.
	 * That includes: ' ', '\n', '\t', '\r'.
	 */
	private void skipWhitespaces() {
		while(currentIndex < data.length) {
			if (isWhitespace(data[currentIndex])) {
				currentIndex++;
				continue;
			}
			break;	
		}
	}
	
	/**
	 * Method checks if given character is a whitespace.
	 * That includes: ' ', '\n', '\t', '\r'.
	 * @param c character that is checked
	 * @return  <code>true</code> if <code>c</code> is a whitespace, otherwise <code>false</code>
	 */
	private boolean isWhitespace(char c) {
		return c == ' ' || c == '\n' || c == '\r' || c == '\t';
	}
	
}
