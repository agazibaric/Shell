package hr.fer.zemris.java.hw07.shell.commands;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellIOException;
import hr.fer.zemris.java.hw07.shell.ShellStatus;
import hr.fer.zemris.java.hw07.shell.commands.tools.CommandTools;

/**
 * Command pushes current directory to the shared files stack
 * and sets current directory to the given path.
 * 
 * @author Ante Gazibarić
 * @version 1.0
 *
 */
public class PushdCommand implements ShellCommand {
	
	/** name of command */
	private static final String COMMAND_NAME = "pushd";
	/** key for shared files map */
	private static final String PUSHD_KEY = "cdstack";
	/** command description */
	private static final String COMMAND_DESCRIPTION = 
			"PUSHD command pushes current directory to the shared files stack \n" +
			"and sets current directory to the given path.\n" +
			"It accepts single arguments which is path of new current directory.";

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env, "Environment must not be null");
		try {
			String[] argumentsParts = CommandTools.getArguments(arguments, true);
			int argLength = argumentsParts.length;
			if (argLength != 1) {
				env.writeln("Invalid number of arguments for pushd command. Expected: 1. Was: " + argLength);
				return ShellStatus.CONTINUE;
			}
			Path path = env.getCurrentDirectory().resolve(Paths.get(argumentsParts[0]));
			CommandTools.checkPathForDirectory(path);
			
			Object pushdStackObject = env.getSharedData(PUSHD_KEY);
			if (pushdStackObject == null) {
				Stack<Path> pushdStack = new Stack<>();
				pushdStack.add(env.getCurrentDirectory());
				env.setSharedData(PUSHD_KEY, pushdStack);
			} else {
				@SuppressWarnings({ "unchecked" })
				Stack<Path> pushdStack = (Stack<Path>) pushdStackObject;
				pushdStack.add(env.getCurrentDirectory());
			}
			env.setCurrentDirectory(path);
			
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

}
