package hr.fer.zemris.java.hw07.shell.namebuilder;

/**
 * Class represents lexical analyzer for name building pattern.
 * 
 * @author Ante Gazibarić
 * @version 1.0
 *
 */
public class NameBuilderLexer {
	
	/**
	 * input text arranged in char array
	 */
	private char[] data;
	/**
	 * current token
	 */
	private Token token;
	/**
	 * index of first unprocessed <code>char</code> in {@link #data}
	 */
	private int currentIndex;
	/**
	 * current lexer state
	 */
	private NameBuilderLexerState lexerState = NameBuilderLexerState.TEXT;
	
	/**
	 * Constructor for creating new {@code NameBuilderLexer}.
	 * 
	 * @param input input that is processed
	 */
	public NameBuilderLexer(String input) {
		if (input == null) 
			throw new IllegalArgumentException("Input must not be null");
		
		data = input.toCharArray();
	}
	
	/**
	 * Method returns current token.
	 * 
	 * @return current token
	 */
	public Token getToken() {
		return token;
	}
	
	/**
	 * Method used for setting next token from input data.
	 * 
	 * @return next token
	 */
	public Token nextToken() {
		switch (lexerState) {
		case TEXT:
			setCurrentTokenText();
			break;
		case SUBSTITUTION:
			setCurrentTokenSubstitution();
		}
		return token;
	}
	
	/**
	 * Method sets next token for text mode of lexer
	 */
	private void setCurrentTokenText() {
		
		if (token != null && token.getType() == TokenType.EOF)
			throw new NameBuilderLexerException("There is no more tokens");

		if (currentIndex >= data.length) {
			token = new Token(TokenType.EOF, null);
			return;
		}
		
		int beginningIndex = currentIndex;
		
		if (data[currentIndex] == '$') {
			currentIndex++;
			if (currentIndex < data.length && data[currentIndex] == '{') {
				token = new Token(TokenType.START_SUBSTITUTION, null);
				currentIndex++;
				return;
			}
		}
		
		while (true) {
			if (currentIndex >= data.length)
				break;
			
			if (data[currentIndex] == '$') {
				currentIndex++;
				if (currentIndex >= data.length) {
					break;
				}
				if (data[currentIndex] == '{') {
					currentIndex--;
					String text = new String(data, beginningIndex, currentIndex - beginningIndex);
					token = new Token(TokenType.TEXT, text);
					return;
				}
			}
			currentIndex++;
		}
		
		String text = new String(data, beginningIndex, currentIndex - beginningIndex);
		token = new Token(TokenType.TEXT, text);
	}
	
	/**
	 * Method sets next token for substitution mode of lexer
	 */
	private void setCurrentTokenSubstitution() {
		
		if (token != null && token.getType() == TokenType.EOF)
			throw new NameBuilderLexerException("There is no more tokens");

		skipWhitespaces();
		
		if (currentIndex >= data.length) {
			token = new Token(TokenType.EOF, null);
			return;
		}
		
		if (data[currentIndex] == '0') {
			token = new Token(TokenType.NUMBER, Integer.valueOf(0));
			currentIndex++;
			return;
		}
		
		if (Character.isDigit(data[currentIndex])) {
			int beginningOfNumber = currentIndex;
			currentIndex++;
			while(currentIndex < data.length && Character.isDigit(data[currentIndex])) {
				currentIndex++;
			}
			String numberInput = new String(data, beginningOfNumber, currentIndex - beginningOfNumber);
			Integer number = Integer.parseInt(numberInput);
			token = new Token(TokenType.NUMBER, number);
			return;
		}
		
		if (data[currentIndex] == ',') {
			token = new Token(TokenType.SEPARATOR, null);
			currentIndex++;
			return;
		}
		
		if (data[currentIndex] == '}') {
			token = new Token(TokenType.END_SUBSTITUTION, null);
			currentIndex++;
			return;
		}
		
		throw new NameBuilderLexerException("Invalid substitution input.\n"
				+ "Invalid character was: '" + data[currentIndex]
				+ "' at index: " + currentIndex + " in substitution.");	
	}
	
	/**
	 * Method sets lexer state.
	 * 
	 * @param lexerState new lexer state
	 */
	public void setLexerState(NameBuilderLexerState lexerState) {
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
