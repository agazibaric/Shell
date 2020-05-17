package hr.fer.zemris.java.hw07.shell.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Command used for exiting program.
 * 
 * @author Ante Gazibaric
 * @version 1.0
 *
 */
public class ExitShellCommand implements ShellCommand {

	/** name of command */
	private static final String COMMAND_NAME = "exit";
	/** command description */
	private static final String COMMAND_DESCRIPTION =
			"EXIT command exits program.\n" +
			"It takes no arguments.";
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		return ShellStatus.TERMINATE;
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
