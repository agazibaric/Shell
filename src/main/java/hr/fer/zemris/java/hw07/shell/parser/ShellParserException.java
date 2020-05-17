package hr.fer.zemris.java.hw07.shell.parser;

/**
 * Class represents exception that can be used by {@link ShellParser} </br>
 * if given query was invalid.
 * 
 * @author Ante Gazibarić
 * @version 1.0
 *
 */
public class ShellParserException extends RuntimeException {

	/**
	 * default serial number
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor that accepts message about invalid situation that occurred during process of parsing
	 * 
	 * @param message {@code String} that describes what went wrong during process of parsing
	 */
	public ShellParserException(String message) {
		super(message);
	}
	
}
