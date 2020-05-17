package hr.fer.zemris.java.hw07.shell.commands;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
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
 * Command copies given directory content to the given destination directory.
 * 
 * @author Ante Gazibarić
 * @version 1.0
 *
 */
public class CptreeCommand implements ShellCommand {
	
	/** name of command */
	private static final String COMMAND_NAME = "pushd";
	/** command description */
	private static final String COMMAND_DESCRIPTION = 
			"CPTREE command copies given directory content to the given destination directory.\n" +
			"It accepts two arguments: \n" + 
			"First argument is source directory which is copied together with all its content.\n" +
			"Second argument is destination to which source directory is copied. \n";

	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env, "Environment must not be null");
		try {
			String[] argumentsParts = CommandTools.getArguments(arguments, true);
			if (argumentsParts.length != 2) 
				throw new IllegalArgumentException("Invalid number of arguments. Expected: 2. Was: " + argumentsParts.length);
			
			Path sourcePath = CommandTools.getResolvedPathFrom(env.getCurrentDirectory(), argumentsParts[0]);
			CommandTools.checkPathForDirectory(sourcePath);
			Path destinationArgument = CommandTools.getResolvedPathFrom(env.getCurrentDirectory(), argumentsParts[1]);
			Path destinationPath = null;
			if (Files.exists(destinationArgument)) {
				destinationPath = CommandTools.getResolvedPathFrom(destinationArgument, sourcePath.getFileName().toString());
			} else {
				destinationPath = CommandTools.getResolvedPathFrom(destinationArgument, "..");
				if (!Files.exists(destinationPath)) {
					env.writeln("Invalid destination path for cptree command");
					return ShellStatus.CONTINUE;
				}
				destinationPath = CommandTools.getResolvedPathFrom(destinationPath, destinationArgument.getFileName().toString());
			}
			Files.walkFileTree(sourcePath, new CptreeFileVisitor(destinationPath));
			
		} catch (IllegalArgumentException | NullPointerException ex) {
			env.writeln(ex.getMessage());
		} catch (IOException ex) {
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
	 * Implementation of {@link FileVisitor} that writes out tree structure of directory.
	 * 
	 * @author Ante Gazibarić
	 * @version 1.0
	 *
	 */
	private static class CptreeFileVisitor implements FileVisitor<Path> {
		
		/** destination path */
		private Path destinationPath;
		/** source from which directory tree is copied */
		private Path sourcePath;
		
		/**
		 * Constructor that creates new {@link CptreeFileVisitor} object.
		 * 
		 * @param destinationPath {@link #destinationPath}
		 * @throws IOException 
		 */
		public CptreeFileVisitor(Path destinationPath) throws IOException {
			this.destinationPath = destinationPath;
			if (!Files.exists(destinationPath)) {
        		Files.createDirectories(destinationPath);
        	}
		}

		@Override
		public FileVisitResult postVisitDirectory(Path path, IOException arg1) throws IOException {
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes arg1) throws IOException {
			if (sourcePath == null) {
	            sourcePath = path;
	        } else {
				Files.createDirectories(destinationPath.resolve(sourcePath.relativize(path)));
	        }
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFile(Path path, BasicFileAttributes arg1) throws IOException {
			Files.copy(path, destinationPath.resolve(sourcePath.relativize(path)));
			return FileVisitResult.CONTINUE;
		}

		@Override
		public FileVisitResult visitFileFailed(Path path, IOException arg1) throws IOException {
			return FileVisitResult.CONTINUE;
		}
		
	}

}
