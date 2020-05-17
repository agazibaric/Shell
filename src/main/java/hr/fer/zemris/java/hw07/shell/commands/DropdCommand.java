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
 * Command removes last pushed directory to the shared files stack.
 * 
 * @author Ante Gazibarić
 * @version 1.0
 *
 */
public class DropdCommand implements ShellCommand {

	/** name of command */
	private static final String COMMAND_NAME = "dropd";
	/** key for shared files map */
	private static final String DROPD_KEY = "cdstack";
	/** command description */
	private static final String COMMAND_DESCRIPTION = 
			"DROPD command removes directory that is last pushed to the shared files stack.\n" +
			"It accepts no arguments.";
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env, "Environment must not be null");
		try {
			CommandTools.checkForNoArguments(arguments, COMMAND_NAME);
			Object popdStackObject = env.getSharedData(DROPD_KEY);
			if (popdStackObject == null) {
				env.writeln("Invalid dropd command call. Stack is empty");
				return ShellStatus.CONTINUE;
			}
			@SuppressWarnings({ "unchecked" })
			Stack<Path> popdStack = (Stack<Path>) popdStackObject;
			env.getCurrentDirectory().resolve(popdStack.pop());
			
		} catch (IllegalArgumentException | NullPointerException ex) {
			env.writeln(ex.getMessage());
		} catch (EmptyStackException ex) {
			env.writeln("Invalid dropd command call. Stack is empty");
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
