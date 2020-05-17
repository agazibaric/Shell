package hr.fer.zemris.java.hw07.shell.commands;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellIOException;
import hr.fer.zemris.java.hw07.shell.ShellStatus;
import hr.fer.zemris.java.hw07.shell.commands.tools.CommandTools;

/**
 * Command that list all files and directories with its attributes from given directory.
 * 
 * @author Ante Gazibaric
 * @version 1.0
 *
 */
public class LsCommand implements ShellCommand {

	/** name of command */
	private static final String COMMAND_NAME = "ls";
	/** command description */
	private static final String COMMAND_DESCRIPTION =
			"LS command prints out all files and directory with its attributes that given directory contains.\n" +
			"It takes single argument and that is path of directory whose content is printed.";
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env, "Environment must not be null");
		
		try {
			String[] argumentsParts = CommandTools.getArguments(arguments, true);
			if (argumentsParts.length != 1) 
				throw new IllegalArgumentException("Invalid number of arguments. Expected: 1. Was: " + argumentsParts.length);
			
			Path path = env.getCurrentDirectory().resolve(Paths.get(argumentsParts[0]));
			CommandTools.checkPathForDirectory(path);
			executeLsCommand(path);
			
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
	 * Method that executes list command.
	 * 
	 * @param path         path of directory
	 * @throws IOException if error occurred during process of reading files
	 */
	private void executeLsCommand(Path path) throws IOException {
		Files.walkFileTree(path, new LsFileVisitor());
	}
	
	/**
	 * Implementation of {@link FileVisitor} used for listing files and directory from current directory.
	 * 
	 * @author Ante Gazibaric
	 * @version 1.0
	 *
	 */
	private static class LsFileVisitor implements FileVisitor<Path> {

		/** value used for detecting how deep in directory structure we got */
		private int depth = 0;
		/** date format */
		private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		@Override
		public FileVisitResult postVisitDirectory(Path path, IOException arg1) throws IOException {
			depth--;
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes arg1) throws IOException {
			if (depth > 0)
				printFileAttributes(path, arg1);
			depth++;
			if (depth > 1) {
				depth--;
				return FileVisitResult.SKIP_SUBTREE;
			}
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path path, BasicFileAttributes arg1) throws IOException {
			printFileAttributes(path, arg1);
			
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path path, IOException arg1) throws IOException {
			return FileVisitResult.CONTINUE;
		}
		
		/**
		 * Method prints out given file or directory with its attributes.
		 * 
		 * @param path         path that is printed
		 * @param attrs		   path's attributes
		 * @throws IOException if error occurred during process of getting file attributes
		 */
		private void printFileAttributes(Path path, BasicFileAttributes attrs) throws IOException {
			FileTime fileTime = attrs.creationTime();
			String formattedDateTime = dateFormat.format(new Date(fileTime.toMillis()));
			System.out.format("%s  %10d %s %s%n",
					getFileDescription(path), 
					Files.size(path), 
					formattedDateTime, 
					path.getFileName());
		}
		
		/**
		 * Method used for getting file description
		 * 
		 * @param file whose description is returned
		 * @return description of given file
		 */
		private String getFileDescription(Path file) {
			StringBuilder sb = new StringBuilder();
			sb.append(Files.isDirectory(file) ? "d" : "-");
			sb.append(Files.isReadable(file) ? "r" : "-");
			sb.append(Files.isWritable(file) ? "w" : "-");
			sb.append(Files.isExecutable(file) ? "x" : "-");
			
			return sb.toString();
		}
		
	}

}
