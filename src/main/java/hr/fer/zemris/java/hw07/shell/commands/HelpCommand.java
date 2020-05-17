package hr.fer.zemris.java.hw07.shell.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellIOException;
import hr.fer.zemris.java.hw07.shell.ShellStatus;
import hr.fer.zemris.java.hw07.shell.commands.tools.CommandTools;

/**
 * Method that prints out description of given command 
 * or prints out list of all commands if there's no arguments.
 * 
 * @author Ante Gazibaric
 * @version 1.0
 *
 */
public class HelpCommand implements ShellCommand {

	/** name of command */
	private static final String COMMAND_NAME = "help";
	/** command description */
	private static final String COMMAND_DESCRIPTION = 
			"HELP command if given no arguments lists all supported commands.\n" +
			"If arguments is given then it must be one of supported commands.\n" +
			"Then it prints out given command's description.";
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env, "Environment must not be null");
		
		try {
			String argumentsParts[] = CommandTools.getArguments(arguments, true);
			int argLength = argumentsParts.length;
			if (argLength == 0) {
				listAllCommands(env);
			} else if (argLength == 1) {
				String commandName = argumentsParts[0];
				printCommandDescription(commandName, env);
			} else {
				env.writeln("Invalid number of arguments for help command. Expected: 1 or 2. Was: " + argLength);
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
	 * Method lists all supported commands.
	 * 
	 * @param env {@link Environment} object used for communication with shell
	 */
	private void listAllCommands(Environment env) {
		env.commands().forEach((k, v) -> System.out.println(k));
	}
	
	/**
	 * Method prints out description of given command if supported.
	 * 
	 * @param env {@link Environment} object used for communication with shell
	 */
	private void printCommandDescription(String commandName,Environment env) {
		ShellCommand command = env.commands().get(commandName);
		if (command != null) {
			command.getCommandDescription().forEach(System.out::println);
		} else {
			env.writeln("Command '" + commandName + "' is invalid.");
		}	
	}

}
