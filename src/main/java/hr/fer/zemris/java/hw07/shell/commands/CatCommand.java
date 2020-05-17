package hr.fer.zemris.java.hw07.shell.commands;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
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
 * Command that prints out content of given file.
 * 
 * @author Ante Gazibaric
 * @version 1.0
 *
 */
public class CatCommand implements ShellCommand {

	/** default charset */
	private static final Charset DEFAULT_CHARSET = Charset.defaultCharset();	
	/** name of command */
	private static final String COMMAND_NAME = "cat";
	/** command description */
	private static final String COMMAND_DESCRIPTION = 
			"CAT command prints out content of given file.\n" + 
			"It takes one argument that represents path to the file" +
			" whose content is printed out";
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env, "Environment must not be null");
		
		try {
			String[] argumentsParts = CommandTools.getArguments(arguments, true);
			int argLength = argumentsParts.length;
			List<String> fileLines;
			
			if (argLength == 2) {
				Path path = env.getCurrentDirectory().resolve(Paths.get(argumentsParts[0]));
				CommandTools.checkPathForFile(path);
				Charset charset = Charset.forName(argumentsParts[1]);
				fileLines = executeCutCommand(path, charset);
			} else if (argLength == 1){
				Path path = env.getCurrentDirectory().resolve(Paths.get(argumentsParts[0]));
				CommandTools.checkPathForFile(path);
				fileLines = executeCutCommand(path, DEFAULT_CHARSET);
			} else {
				env.writeln("Invalid number of arguments for cat command. Must be 1 or 2. Was: " + argLength);
				return ShellStatus.CONTINUE;
			}
			fileLines.forEach(env::writeln);
			
		} catch (IOException ex) {
			env.writeln("Failed to open given file");
		} catch (IllegalCharsetNameException | UnsupportedCharsetException ex) {
			env.writeln("Given charset is invalid");
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
	 * Method that executes cut command.
	 * 
	 * @param path         {@link Path} of file whose content is printed out
	 * @param charset      {@link Charset} 
	 * @return             list of strings that represents lines of given file
	 * @throws IOException if error occurred during file reading
	 */
	private List<String> executeCutCommand(Path path, Charset charset) throws IOException {
		return Files.readAllLines(path, charset);
	}

}
