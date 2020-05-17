package hr.fer.zemris.java.hw07.shell.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.MyShell;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellIOException;
import hr.fer.zemris.java.hw07.shell.ShellStatus;
import hr.fer.zemris.java.hw07.shell.commands.tools.CommandTools;

/**
 * Command that is used for printing out and changing symbols of {@link MyShell}.
 * 
 * @author Ante Gazibaric
 * @version 1.0
 *
 */
public class SymbolCommand implements ShellCommand {
	
	/** name of command */
	private static final String COMMAND_NAME = "symbol";
	/** command description */
	private static final String COMMAND_DESCRIPTION =
			"SYMBOL command expects one or two arguments.\n" +
			"If one arguments is given it prints out current symbol for given argument.\n" +
			"First argument must be: PROMPT, MULTILINE or MORELINES.\n" +
			"If two arguments are given it sets given shell symbol to the given character.\n" +
			"Second argument must be character.";
	
	/** prompt command string */
	private static final String promptString = "PROMPT";
	/** multiline command string */
	private static final String multilineSymbolString = "MULTILINE";
	/** more lines command string */
	private static final String morelinesString = "MORELINES";
	/** format for printing out info about symbol */
	private static final String messageInfoFormat = "Symbol for %s is '%c'";
	/** format that is printed when symbol is changed */
	private static final String messageChangeFormat = "Symbol for %s changed from '%c' to '%c'";

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env, "Environment must not be null");
		
		try {
			String[] argumentsParts = CommandTools.getArguments(arguments, true);
			int argsLength = argumentsParts.length;
			if (argsLength == 1) {
				executeSymbolCommand(env, argumentsParts[0]);
			} else if (argsLength == 2) {
				String symbol = argumentsParts[1];
				if (symbol.length() > 1) {
					env.writeln("Given symbol is invalid. Must be single character. Was: " + symbol);
					return ShellStatus.CONTINUE;
				}
				executeSymbolCommand(env, argumentsParts[0], symbol);
			} else {
				env.writeln("Invalid number of arguments. Expected 1 or 2. Was: " + argsLength);
			}
			
		} catch (IllegalArgumentException | NullPointerException ex) {
			env.writeln(ex.getMessage());
		} catch (Exception ex) {
			throw new ShellIOException(ex.getMessage());
		}
		return ShellStatus.CONTINUE;
	}

	@Override
	public String getCommandName() {
		return COMMAND_NAME;
	}

	@Override
	public List<String> getCommandDescription() {
		List<String> description = new ArrayList<>();
		for (String s : COMMAND_DESCRIPTION.split("\\n")) {
			description.add(s);
		}
		return Collections.unmodifiableList(description);
	}
	
	/**
	 * Method executes symbol command.
	 * 
	 * @param env  {@link Environment} object
	 * @param name name of symbol
	 */
	private void executeSymbolCommand(Environment env, String name) {
		if (name.equals(promptString)) {
			env.writeln(String.format(messageInfoFormat, promptString, env.getPromptSymbol()));
		} else if (name.equals(morelinesString)) {
			env.writeln(String.format(messageInfoFormat, morelinesString, env.getMorelinesSymbol()));
		} else if (name.equals(multilineSymbolString)) {
			env.writeln(String.format(messageInfoFormat, multilineSymbolString, env.getMultilineSymbol()));
		} else {
			throw new IllegalArgumentException("Invalid argument for symbol command. Was: " + name);
		}
	}
	
	/**
	 * Method executes symbol command.
	 * 
	 * @param env    {@link Environment} object
	 * @param name   name of symbol
	 * @param symbol new symbol that is set
	 */
	private void executeSymbolCommand(Environment env, String name, String symbol) {
		if (name.equals(promptString)) {
			Character oldSymbol = env.getPromptSymbol();
			Character newSymbol = symbol.charAt(0);
			env.setPromptSymbol(newSymbol);
			env.writeln(String.format(messageChangeFormat, promptString, oldSymbol, newSymbol));
		} else if (name.equals(morelinesString)) {
			Character oldSymbol = env.getMorelinesSymbol();
			Character newSymbol = symbol.charAt(0);
			env.setMorelinesSymbol(newSymbol);
			env.writeln(String.format(messageChangeFormat, morelinesString, oldSymbol, newSymbol));
		} else if (name.equals(multilineSymbolString)) {
			Character oldSymbol = env.getMultilineSymbol();
			Character newSymbol = symbol.charAt(0);
			env.setMultilineSymbol(newSymbol);
			env.writeln(String.format(messageChangeFormat, multilineSymbolString, oldSymbol, newSymbol));
		} else {
			throw new IllegalArgumentException("Invalid argument for symbol command. Was: " + name);
		}
	}

}
