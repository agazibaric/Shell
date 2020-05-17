package hr.fer.zemris.java.hw07.shell.lexer;

/**
 * Class represents exception that {@link ShellLexer} uses when it encounters
 * invalid input during lexical analysis.
 * 
 * @author Ante Gazibarić
 * @version 1.0
 *
 */
public class ShellLexerException extends RuntimeException {

	/**
	 * Default serial number
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor
	 */
	public ShellLexerException() {
		super();
	}
	
	/**
	 * Constructor that accepts message about invalid situation that occurred during lexical analysis
	 * 
	 * @param message message that describes what went wrong during lexical analysis
	 */
	public ShellLexerException(String message) {
		super(message);
	}
	
}
