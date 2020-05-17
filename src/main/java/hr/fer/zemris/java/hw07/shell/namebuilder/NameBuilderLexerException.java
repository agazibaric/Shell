package hr.fer.zemris.java.hw07.shell.namebuilder;

/**
 * Class represents exception that {@link NameBuilderLexer} uses when it encounters
 * invalid input during lexical analysis.
 * 
 * @author Ante Gazibarić
 * @version 1.0
 *
 */
public class NameBuilderLexerException extends RuntimeException {

	/**
	 * Default serial number
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor that accepts message about invalid situation that occurred during lexical analysis
	 * 
	 * @param message message that describes what went wrong during lexical analysis
	 */
	public NameBuilderLexerException(String message) {
		super(message);
	}
	
}
