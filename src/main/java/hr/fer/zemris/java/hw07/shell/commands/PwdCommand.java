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
 * Command prints out current working directory.
 * 
 * @author Ante Gazibarić
 * @version 1.0
 *
 */
public class PwdCommand implements ShellCommand {
	
	/** name of command */
	private static final String COMMAND_NAME = "pwd";
	/** command description */
	private static final String COMMAND_DESCRIPTION = 
			"PWD command prints out current working directory.\n" +
			"It takes no arguments.";

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env, "Environment must not be null");
		try {
			CommandTools.checkForNoArguments(arguments, COMMAND_NAME);
			env.writeln(env.getCurrentDirectory().toString());
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
