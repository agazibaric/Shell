package hr.fer.zemris.java.hw07.shell.namebuilder;

/**
 * Class represents token in lexical analysis. </br>
 * Token is lexical unit that groups one or more consecutive symbols from input text.
 * 
 * @author Ante Gazibarić
 * @version 1.0
 */
public class Token {

	/**
	 * type of token
	 */
	private TokenType type;
	/**
	 * value that token represents
	 */
	private Object value;
	
	/**
	 * Constructor for creating new <code>Token</code>.
	 * 
	 * @param type  {@link TokenType} type of token
	 * @param value {@code Object} that token stores
	 */
	public Token(TokenType type, Object value) {
		if (type == null)
			throw new IllegalArgumentException("Token type must not be null");
		
		this.type = type;
		this.value = value;
	}
	
	/**
	 * Method returns value that token contains.
	 * 
	 * @return {@code Object} value of token
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * Method returns token type.
	 * 
	 * @return {@link TokenType} type of this token.
	 */
	public TokenType getType() {
		return type;
	}
	
}
