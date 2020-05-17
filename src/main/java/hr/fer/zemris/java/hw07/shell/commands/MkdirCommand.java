package hr.fer.zemris.java.hw07.shell.commands;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
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
 * Command that makes new directory structure from given path.
 * 
 * @author Ante Gazibaric
 * @version 1.0
 *
 */
public class MkdirCommand implements ShellCommand {

	/** name of command */
	private static final String COMMAND_NAME = "mkdir";
	/** command description */
	private static final String COMMAND_DESCRIPTION = 
			"MKDIR command makes directory structure that user provided.\n" +
			"It takes single argument and that is direcotry that is made.";
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env, "Environment must not be null");
		
		try {
			String[] argumentsParts = CommandTools.getArguments(arguments, true);
			if (argumentsParts.length != 1) 
				throw new IllegalArgumentException("Invalid number of arguments. Expected: 1. Was: " + argumentsParts.length);
			
			Path path = env.getCurrentDirectory().resolve(Paths.get(argumentsParts[0]));
			executeMkdirCommand(path);
			
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
	 * Method executes mkdir command 
	 * @param path path of directory that will be created
	 */
	private void executeMkdirCommand(Path path) {
		new File(path.toString()).mkdirs();
	}

}
