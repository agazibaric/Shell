package hr.fer.zemris.java.hw07.shell.lexer;

/**
 * Class represents different states of lexical analyzer {@link ShellLexer}.
 * 
 * @author Ante Gazibarić
 * @version 1.0
 */
public enum LexerState {
	/**
	 * Mode that support escape character '\'
	 */
	WITH_ESCAPING,
	/**
	 * Mode does not support escaping characters
	 */
	NO_ESCAPING
}
