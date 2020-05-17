package hr.fer.zemris.java.hw07.shell;

import java.util.List;

/**
 * Interface represents general form of command used by {@link MyShell}.
 * 
 * @author Ante Gazibarić
 * @version 1.0
 *
 */
public interface ShellCommand {

	/**
	 * Method executes command.
	 * 
	 * @param env       {@link Environment} object 
	 * @param arguments command arguments
	 * @return          {@link ShellStatus} that represents how command execution has passed.
	 */
	ShellStatus executeCommand(Environment env, String arguments);
	
	/**
	 * Method returns command name
	 * @return command name
	 */
	String getCommandName();
	
	/**
	 * Method returns description of command
	 * 
	 * @return command description
	 */
	List<String> getCommandDescription();
	
}
