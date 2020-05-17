package hr.fer.zemris.java.hw07.shell.commands;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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
 * Command that copies file to the given destination.
 * 
 * @author Ante Gazibaric
 * @version 1.0
 *
 */
public class CopyCommand implements ShellCommand {

	/** name of command */
	private static final String COMMAND_NAME = "copy";
	/** default charset */
	private static final String DEFAULT_CHARSET = "UTF-8";
	/** command description */
	private static final String COMMAND_DESCRIPTION = 
			"COPY command copies given file to the given destination.\n" +
			"It takes two arguments: first is file that is copied, and second is destination.\n" +
			"If destination is directory then new file will be stored in that directory\n" +
			" with the same name as copied file.\n" +
			"If destination is file then file that is copied will be named as given file.\n" +
			"If destination file already exists you will be asked do you want to override it.";
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env, "Environment must not be null");
		
		try {
			String[] argumentsParts = CommandTools.getArguments(arguments, true);
			if(argumentsParts.length != 2) {
				env.writeln("Invalid number of arguments. Expected: 2. Was: " + argumentsParts.length);
				return ShellStatus.CONTINUE;
			}
			Path currentDir = env.getCurrentDirectory();
			Path sourcePath = currentDir.resolve(Paths.get(argumentsParts[0]));
			CommandTools.checkPathForFile(sourcePath);
			Path destinationPath = currentDir.resolve(Paths.get(argumentsParts[1]));
			executeCopyCommand(env, sourcePath, destinationPath);
			
		} catch (IOException ex) {
			env.writeln("Failed to open given file");
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
	 * Method executes copy command.
	 * 
	 * @param env                           {@link Environment} object
	 * @param sourcePath                    {@link Path} of source file
	 * @param destinationPath               {@link Path} of destination
	 * @throws UnsupportedEncodingException if encoding is unsupported
	 * @throws IOException                  if error occurred during copying files
	 */
	private void executeCopyCommand(Environment env, Path sourcePath, Path destinationPath) throws UnsupportedEncodingException, IOException {
		if (Files.isDirectory(destinationPath)) {
			destinationPath = Paths.get(destinationPath.toString().concat("\\" + sourcePath.getFileName()));
		}
		
		if (Files.exists(destinationPath)) {
			env.writeln("Destination file already exists. Do you want to override it? [y/n]");
			String userInput = env.readLine().trim().toLowerCase();
			if (!userInput.equals("y"))
				return;
		}
		
		try (
				BufferedReader br = new BufferedReader(
						new InputStreamReader(
						new BufferedInputStream(
								Files.newInputStream(sourcePath, 
								StandardOpenOption.READ))));
				
				Writer bw = new BufferedWriter(
						 new OutputStreamWriter(
						 new BufferedOutputStream(
								 Files.newOutputStream(destinationPath, 
								 StandardOpenOption.WRITE, 
								 StandardOpenOption.TRUNCATE_EXISTING,
								 StandardOpenOption.CREATE)), DEFAULT_CHARSET))) {
			
			while(true) {
				String line = br.readLine();
				if(line == null) 
					break;
				bw.write(line.concat("\n"));
			}
		}
	}

}
