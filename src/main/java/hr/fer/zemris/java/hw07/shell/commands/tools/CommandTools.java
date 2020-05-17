package hr.fer.zemris.java.hw07.shell.commands.tools;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.hw07.shell.parser.ShellParser;
import hr.fer.zemris.java.hw07.shell.parser.ShellParserException;

/**
 * Class offers tools used by {@link Command} objects.
 * 
 * @author Ante Gazibaric
 * @version 1.0
 *
 */
public class CommandTools {
	
	/** 
	 * Method processes and returns arguments in array
	 * 
	 * @param input input that is processed
	 * @return arguments array
	 * @throws IllegalArgumentException if given input is not valid
	 * @throws NullPointerException if given input is {@code null}
	 */
	public static String[] getArguments(String input, boolean toEscape) {
		if (input == null)
			throw new NullPointerException("Input must not be null");
		
		try {
			ShellParser parser = new ShellParser(input, toEscape);
			List<String> listOfInputs = parser.getInputs();
			int size = listOfInputs.size();
			String[] arguments = new String[size];
			for (int i = 0; i < size; i++) {
				arguments[i] = listOfInputs.get(i);
			}
			return arguments;
		} catch (ShellParserException ex) {
			throw new IllegalArgumentException(ex.getMessage());
		}
	}
	
	/**
	 * Method that checks validity of given argument for command.
	 * 
	 * @param arguments   arguments that are checked
	 * @param commandName name of command
	 * @throws            IllegalArgumentException if arguments are not valid
	 */
	public static void checkForNoArguments(String arguments, String commandName) {
		if (arguments != null && !arguments.isEmpty())
			throw new IllegalArgumentException("Command '" + commandName + "' accepts no arguments.");
	}
	/**
	 * Method checks if given path is directory.
	 * 
	 * @param path path that is checked
	 * @throws IllegalArgumentException if given directory does not exists or if it's not directory
	 * @throws NullPointerException if given path is {@code null}
	 */
	public static void checkPathForDirectory(Path path) {
		Objects.requireNonNull(path, "Given path must not be null");
		
		if (!Files.exists(path)) 
			throw new IllegalArgumentException("Given directory does not exist. Was: " + path);
		
		if(!Files.isDirectory(path)) 
			throw new IllegalArgumentException("Given path is not directory. Was: " + path);
	
	}
	
	/**
	 * Method checks if given path is file.
	 * 
	 * @param path path that is checked
	 * @throws IllegalArgumentException if given file does not exists or if it's directory
	 * @throws NullPointerException if given path is {@code null}
	 */
	public static void checkPathForFile(Path path) {
		Objects.requireNonNull(path, "Given path must not be null.");
		
		if (!Files.exists(path)) 
			throw new IllegalArgumentException("Given file does not exist. Was: " + path);
		
		if(Files.isDirectory(path)) 
			throw new IllegalArgumentException("Given path must not be directory. Was: " + path);
	
	}
	
	/**
	 * Method resolves given {@code path} considering the given path {@code current}.
	 * Then it transforms it to absolute path and normalizes it.
	 * 
	 * @param current current path
	 * @param path    path that will be resolve considering the given current path
	 * @return        absolute normalized path that is resolved considering the current path
	 */
	public static Path getResolvedPathFrom(Path current, String path) {
		return current.resolve(Paths.get(path)).toAbsolutePath().normalize();
	}
	
	/**
	 * Method that checks if given string contains whitespaces.
	 * 
	 * @param argument string that is checked
	 * @return {@code true} if given string contains whitespaces,
	 * 		   {@code false} otherwise
	 */
	public static boolean containsWhitespaces(String argument) {
		if(argument.contains(" ") ||
			argument.contains("\t") ||
			argument.contains("\n"))
			return true;
		
		return false;
	}

}
