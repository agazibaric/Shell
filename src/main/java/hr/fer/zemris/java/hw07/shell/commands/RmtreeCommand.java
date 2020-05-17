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
 * Command deletes given directory and all its content.
 * 
 * @author Ante Gazibarić
 * @version 1.0
 *
 */
public class RmtreeCommand implements ShellCommand {

	/** name of command */
	private static final String COMMAND_NAME = "rmtree";
	/** command description */
	private static final String COMMAND_DESCRIPTION = 
			"Command deletes given directory and all its content.\n" +
			"It accepts single argument which is directory that is removed.";
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env, "Environment must not be null");
		try {
			String[] argumentsParts = CommandTools.getArguments(arguments, true);
			int argLength = argumentsParts.length;
			if (argLength != 1) {
				env.writeln("Invalid number of arguments for pushd command. Expected: 1. Was: " + argLength);
				return ShellStatus.CONTINUE;
			}
			Path currentDir = env.getCurrentDirectory();
			Path path = currentDir.resolve(Paths.get(argumentsParts[0]));
			CommandTools.checkPathForDirectory(path);
			Files.walkFileTree(path, new RmtreeFileVisitor());
			if (!Files.exists(currentDir)) {
				Path previousPath = currentDir.resolve(Paths.get(".."));
				env.setCurrentDirectory(previousPath);
			}
			
		} catch (IllegalArgumentException | NullPointerException ex) {
			env.writeln(ex.getMessage());
		} catch (IOException ex) {
			env.writeln("Tree command failed to delete file or directory.");
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
	 * Implementation of {@link FileVisitor} used to delete directory tree structure.
	 * 
	 * @author Ante Gazibarić
	 * @version 1.0
	 *
	 */
	private static class RmtreeFileVisitor implements FileVisitor<Path> {

		@Override
		public FileVisitResult postVisitDirectory(Path path, IOException arg1) throws IOException {
			Files.deleteIfExists(path);
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes arg1) throws IOException {
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path path, BasicFileAttributes arg1) throws IOException {
			Files.deleteIfExists(path);
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path path, IOException arg1) throws IOException {
			return FileVisitResult.CONTINUE;
		}
		
	}

}
