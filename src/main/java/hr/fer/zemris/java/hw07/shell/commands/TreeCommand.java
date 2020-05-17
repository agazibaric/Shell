package hr.fer.zemris.java.hw07.shell.commands;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
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
 * Command that writes out tree structure of given directory.
 * 
 * @author Ante Gazibarić
 * @version 1.0
 *
 */
public class TreeCommand implements ShellCommand {

	/** name of command */
	private static final String COMMAND_NAME = "tree";
	/** command description */
	private static final String COMMAND_DESCRIPTION = 
			"TREE command prints out tree structure of given directory.\n" +
			"It takes single argument and that is directory whose tree structure is printed.";
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env, "Environment must not be null");
		
		try {
			String[] argumentsParts = CommandTools.getArguments(arguments, true);
			if (argumentsParts.length != 1) 
				throw new IllegalArgumentException("Invalid number of arguments. Expected: 1. Was: " + argumentsParts.length);
			
			Path path = env.getCurrentDirectory().resolve(Paths.get(argumentsParts[0]));
			CommandTools.checkPathForDirectory(path);
			executeTreeCommand(path);
			
		} catch (IllegalArgumentException | NullPointerException ex) {
			env.writeln(ex.getMessage());
		} catch (IOException ex) {
			env.writeln("Failed to open given directory");
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
	 * Method executes tree command.
	 * 
	 * @param path         directory path
	 * @throws IOException if error occurred during process of opening files
	 */
	private void executeTreeCommand(Path path) throws IOException {
		Files.walkFileTree(path, new TreeFileVisitor());
	}
	
	/**
	 * Implementation of {@link FileVisitor} that writes out tree structure of directory.
	 * 
	 * @author Ante Gazibarić
	 * @version 1.0
	 *
	 */
	private static class TreeFileVisitor implements FileVisitor<Path> {

		/** value used for detecting how deep in directory structure we got */
		private int depth = 1;
		
		@Override
		public FileVisitResult postVisitDirectory(Path path, IOException arg1) throws IOException {
			depth--;
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes arg1) throws IOException {
			System.out.format("%" + (2 * depth) + "s[DIR] %s%n", "", path.getFileName());
			depth++;
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path path, BasicFileAttributes arg1) throws IOException {
			System.out.format("%" + (2 * depth) + "s[FILE] %s%n", "", path.getFileName());
			
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path path, IOException arg1) throws IOException {
			return FileVisitResult.CONTINUE;
		}
		
	}

}
