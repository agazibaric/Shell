package hr.fer.zemris.java.hw07.shell.namebuilder;

/**
 * Class represents different types of {@link Token}.
 * 
 * @author Ante Gazibarić
 * @version 1.0
 */
public enum TokenType {
	/**
	 * represents end of query input
	 */
	EOF,
	/**
	 * represents text input
	 */
	TEXT,
	/**
	 * represents number in substitution
	 */
	NUMBER,
	/**
	 * represents beginning of substitution
	 */
	START_SUBSTITUTION,
	/**
	 * represents the end of substitution
	 */
	END_SUBSTITUTION,
	/**
	 * separator which separates two values in substitution
	 */
	SEPARATOR
}