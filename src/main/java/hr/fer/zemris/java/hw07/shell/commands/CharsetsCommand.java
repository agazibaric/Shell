package hr.fer.zemris.java.hw07.shell.commands;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellStatus;

/**
 * Command prints out all supported charsets.
 * 
 * @author Ante Gazibaric
 * @version 1.0
 *
 */
public class CharsetsCommand implements ShellCommand {

	/** name of command */
	private static final String COMMAND_NAME = "charset";
	/** command description */
	private static final String COMMAND_DESCRIPTION = 
			"CHARSET command prints out all supported charsets.\n" +
			"It takes no arguments.";
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env, "Environment must not be null");
		
		if (!arguments.isEmpty()) {
			env.writeln("charset command accepts no arguments");
		} else {
			for (String charset : Charset.availableCharsets().keySet()) {
				env.writeln(charset);
			}
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
