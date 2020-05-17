package hr.fer.zemris.java.hw07.shell;

import java.nio.file.Path;
import java.util.SortedMap;

/**
 * Interface that is used for communication between {@link MyShell} and different {@link ShellCommand} objects.
 * 
 * @author Ante Gazibarić
 * @version 1.0
 *
 */
public interface Environment {

	/**
	 * Method reads line from user
	 * 
	 * @return user input 
	 * @throws ShellIOException if error occurred during reading user input
	 */
	String readLine() throws ShellIOException;
	
	/**
	 * Method writes given text to user
	 * 
	 * @param text that is written to user
	 * @throws ShellIOException if error occurred during writing
	 */
	void write(String text) throws ShellIOException;
	
	/**
	 * Method writes given text to user with new line at the end
	 * 
	 * @param text that is written to user
	 * @throws ShellIOException if error occurred during writing
	 */
	void writeln(String text) throws ShellIOException;
	
	/**
	 * Method returns map of commands.
	 * 
	 * @return sorted map of commands
	 */
	SortedMap<String, ShellCommand> commands();
	
	/**
	 * Method returns multiline symbol
	 * 
	 * @return multiline symbol
	 */
	Character getMultilineSymbol();
	
	/**
	 * Method sets multiline symbol
	 * 
	 * @param symbol new multiline symbol
	 */
	void setMultilineSymbol(Character symbol);
	
	/**
	 * Method returns prompt symbol
	 * 
	 * @return prompt symbol
	 */
	Character getPromptSymbol();
	
	/**
	 * Method sets prompt symbol
	 * 
	 * @param symbol new prompt symbol
	 */
	void setPromptSymbol(Character symbol);
	
	/**
	 * Method returns more lines symbol
	 * 
	 * @return symbol for more lines
	 */
	Character getMorelinesSymbol();
	
	/**
	 * Method sets symbol for more lines
	 * 
	 * @param symbol new symbol for more lines
	 */
	void setMorelinesSymbol(Character symbol);
	
	/**
	 * Method returns current working directory.
	 * 
	 * @return {@link Path} which represents current working directory
	 */
	Path getCurrentDirectory();
	
	/**
	 * Method sets current working directory to the given {@code path}.
	 * 
	 * @param path new current working directory
	 */
	void setCurrentDirectory(Path path);
	
	/**
	 * Method returns shared data associated to the given {@code key}.
	 * 
	 * @param key key to which returned shared data is associated
	 * @return    shared data associated to the given {@code key}
	 */
	Object getSharedData(String key);
	
	/**
	 * Method sets shared data associated to the given {@code key} to the given {@code value}.
	 * 
	 * @param key   key to which given value is associated
	 * @param value value that is associated to the given key and stored in shared data
	 */
	void setSharedData(String key, Object value);
	
}
