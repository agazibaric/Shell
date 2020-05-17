package hr.fer.zemris.java.hw07.shell.commands;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellIOException;
import hr.fer.zemris.java.hw07.shell.ShellStatus;
import hr.fer.zemris.java.hw07.shell.commands.tools.CommandTools;

/**
 * Command lists all directories that have been pushed to the shared files stack.
 * 
 * @author Ante Gazibarić
 * @version 1.0
 *
 */
public class ListdCommand implements ShellCommand {
	
	/** name of command */
	private static final String COMMAND_NAME = "pushd";
	/** key for shared files map */
	private static final String LISTD_KEY = "cdstack";
	/** command description */
	private static final String COMMAND_DESCRIPTION = 
			"LISTD lists all directory that have been pushed to the shared files stack.\n" +
			"It lists out them in order so that the first one is the last that is pushed to the stack.\n" +
			"It accepts no arguments.";

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env, "Environment must not be null");
		try {
			CommandTools.checkForNoArguments(arguments, COMMAND_NAME);
			Object popdStackObject = env.getSharedData(LISTD_KEY);
			if (popdStackObject == null) {
				env.writeln("There is no stored directories");
				return ShellStatus.CONTINUE;
			}
			@SuppressWarnings({ "unchecked" })
			Stack<Path> popdStack = (Stack<Path>) popdStackObject;
			if(popdStack.empty()) {
				env.writeln("There is no stored directories");
				return ShellStatus.CONTINUE;
			}
			printDirectories(popdStack, env);
			
		} catch (IllegalArgumentException | NullPointerException ex) {
			env.writeln(ex.getMessage());
		} catch (EmptyStackException ex) {
			env.writeln("Stack is empty");
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
	 * Method prints out all paths that have been pushed to the stack.
	 * 
	 * @param stack stack that contains directories
	 * @param env   {@link Environment} object used for communication with shell
	 */
	private void printDirectories(Stack<Path> stack, Environment env) {
		Object[] pathObjectarray = stack.toArray();
		for (int i = pathObjectarray.length - 1; i >= 0; i--) {
			Path path = (Path) pathObjectarray[i];
			env.writeln(path.toString());
		}
	}

}
