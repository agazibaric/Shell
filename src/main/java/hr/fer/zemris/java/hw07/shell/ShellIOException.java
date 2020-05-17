package hr.fer.zemris.java.hw07.shell;

/**
 * Class represents exception that is used for {@link MyShell}.
 * 
 * @author Ante Gazibarić
 * @version 1.0
 *
 */
public class ShellIOException extends RuntimeException {
	
	
	/**
	 * Serial number
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor that creates new {@code ShellIOException} object.
	 * 
	 * @param message message that describes what went wrong
	 */
	public ShellIOException(String message) {
		super(message);
	}

}
