package hr.fer.zemris.java.hw07.shell.namebuilder;


/**
 * Class represents exception that can be used by {@link NameBuilderParser} </br>
 * if given expression was invalid.
 * 
 * @author Ante Gazibarić
 * @version 1.0
 *
 */
public class NameBuilderParserException extends RuntimeException {
	
	/**
	 * default serial number
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor that accepts message about invalid situation that occurred during process of parsing
	 * 
	 * @param message {@code String} that describes what went wrong during process of parsing
	 */
	public NameBuilderParserException(String message) {
		super(message);
	}

}
