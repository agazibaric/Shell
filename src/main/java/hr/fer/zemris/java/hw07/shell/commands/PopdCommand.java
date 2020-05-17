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
 * Command pops directory from shared files stack and 
 * sets current directory to the popped directory.
 * 
 * @author Ante Gazibarić
 * @version 1.0
 *
 */
public class PopdCommand implements ShellCommand {

	/** name of command */
	private static final String COMMAND_NAME = "popd";
	/** key for shared files map */
	private static final String POPD_KEY = "cdstack";
	/** command description */
	private static final String COMMAND_DESCRIPTION = 
			"POPD command pops directory from shared files stack\n" +
			"and sets current working directory to the popped directory.\n" +
			"It accepts no arguments.";
	
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env, "Environment must not be null");
		try {
			CommandTools.checkForNoArguments(arguments, COMMAND_NAME);
			Object popdStackObject = env.getSharedData(POPD_KEY);
			if (popdStackObject == null) {
				env.writeln("Stack is empty");
				return ShellStatus.CONTINUE;
			}
			@SuppressWarnings({ "unchecked" })
			Stack<Path> popdStack = (Stack<Path>) popdStackObject;
			Path path = env.getCurrentDirectory().resolve(popdStack.pop());
			CommandTools.checkPathForDirectory(path);
			env.setCurrentDirectory(path);
			
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

}
