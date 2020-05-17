package hr.fer.zemris.java.hw07.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellIOException;
import hr.fer.zemris.java.hw07.shell.ShellStatus;
import hr.fer.zemris.java.hw07.shell.commands.tools.CommandTools;
import hr.fer.zemris.java.hw07.shell.namebuilder.NameBuilder;
import hr.fer.zemris.java.hw07.shell.namebuilder.NameBuilderInfo;
import hr.fer.zemris.java.hw07.shell.namebuilder.NameBuilderParser;
import hr.fer.zemris.java.hw07.shell.namebuilder.NameBuilderParserException;

/**
 * Massrename command provides several action that you can perform: </br>
 * filter, show, groups, execute. </p>
 * 
 * <b>filter</b> action prints out files from given directory that are filtered using given pattern. </br>
 * <b>groups</b> action prints out files from given directory that are filtered and individual groups got from given pattern. </br>
 * <b>show</b> action prints out files from given directory that are filtered using given pattern and
 * newly generated file names got from given name generator pattern. </br>
 * <b>execute</b> action copies files from given directory that are filtered using given pattern </br>
 * to the given destination directory with newly generated names got from given name generator pattern. </p>
 * 
 * @author Ante Gazibaric
 * @version 1.0
 *
 */
public class MassrenameCommand implements ShellCommand {

	/** name of command */
	private static final String COMMAND_NAME = "massrename";
	/** command description */
	private static final String COMMAND_DESCRIPTION = 
			"Massrename command provides several action that you can perform:\n" + 
			"filter, show, groups, execute.\n\n" +
			"For filter and groups it accepts 4 arguments.\n" + 
			"For show and execute it accepts 5 arguments.\n" + 
			"First three arguments must be: source directory, destination directory and action that is performed.\n\n" +
			"filter action prints out files from given directory that are filtered using given pattern.\n" + 
			"4th argument for filter action is pattern used to filter files.\n\n" +
			"goups action prints out files form given directory that are filtered and its individual groups got from given pattern.\n" + 
			"4th argument for groups action is pattern used to filter files.\n" + 
			"show action prints out files from given directory that are filtered using given pattern and\n" + 
			"prints out newly generated file names got from given name generator pattern" + 
			"4th argument for show action is pattern used to filter files.\n" + 
			"5th argument for show action is pattern used to generate new file names.\n\n" + 
			"execute action copies files from given directory that are filtered using given pattern\n" + 
			"to the given destination directory with newly generated names got from given name generator pattern." + 
			"4th argument for execute action is pattern used to filter files.\n" + 
			"5th argument for execute action is pattern used to generate new file names.";
	
	/** name of filter action */
	private static final String filterString = "filter";
	/** name of show action */
	private static final String showString = "show";
	/** name of groups action */
	private static final String groupsString = "groups";
	/** name of execute action */
	private static final String executeString = "execute";
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env, "Environment must not be null");
		try {
		String[] argumentsParts = CommandTools.getArguments(arguments, false);
		int argLength = argumentsParts.length;
		
		if (argLength < 4 || argLength > 5) {
			env.writeln("Invalid number of arguments for 'massrename' command.\n"
					+ "Expected: 4 or 5. Was: " + argLength);
			return ShellStatus.CONTINUE;
		}
		
		Path sourcePath = env.getCurrentDirectory().resolve(Paths.get(argumentsParts[0])).toAbsolutePath().normalize();
		Path destinationPath = env.getCurrentDirectory().resolve(Paths.get(argumentsParts[1])).toAbsolutePath().normalize();
		CommandTools.checkPathForDirectory(sourcePath);
		CommandTools.checkPathForDirectory(destinationPath);
		String actionName = argumentsParts[2];
		
		if (argLength == 4) {
			switch (actionName) {
			case filterString:
				runFilterCommand(sourcePath, argumentsParts[3], env);
				break;
			case groupsString:
				runGroupsCommand(sourcePath, argumentsParts[3], env);
				break;
			default:
				env.writeln("Invalid argument for type of action for 'massrename' command.\n"
						+ "Was: " + actionName);
			}
		} else {
			switch (actionName) {
			case showString:
				runShowCommand(sourcePath, argumentsParts[3], argumentsParts[4], env);
				break;
			case executeString:
				runExecuteCommand(sourcePath, destinationPath, argumentsParts[3], argumentsParts[4], env);
				break;
			default:
				env.writeln("Invalid argument for type of action for 'massrename' command.\n"
						+ "Was: " + actionName);
			}
		} 
		} catch (IllegalArgumentException | NullPointerException ex) {
			env.writeln(ex.getMessage());
		} catch (IOException ex) {
			env.writeln("Failed to open given directories.");
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
	 * Method executes filter command which prints out files that satisfies given pattern.
	 * 
	 * @param sourcePath    directory from which files are filtered
	 * @param patternString pattern used for filtering
	 * @param env           {@link Environment} object used for communication with shell
	 * @throws IOException  if reading form directory fails
	 */
	private void runFilterCommand(Path sourcePath, String patternString, Environment env) throws IOException {
		List<Path> files = getFilteredFiles(sourcePath, patternString);
		files.forEach(f -> env.writeln(f.getFileName().toString()));
	}
	
	/**
	 * Method executes group command which prints out all files that satisfies given pattern </br>
	 * and also individual groups used in given pattern.
	 * 
	 * @param sourcePath    directory from which files are filtered
	 * @param patternString pattern used for filtering
	 * @param env           {@link Environment} object used for communication with shell
	 * @throws IOException  if reading form directory fails
	 */
	private void runGroupsCommand(Path sourcePath, String patternString, Environment env) throws IOException {
		Pattern pattern = Pattern.compile(patternString);
		List<Path> filesList = Files.list(sourcePath).collect(Collectors.toList());
		for (Path path : filesList) {
			String fileName = path.getFileName().toString();
			Matcher matcher = pattern.matcher(fileName);
			if (matcher.find()) {
				printGroupsForFile(fileName, matcher, env);
			}
		}	
	}
	
	/**
	 * Method prints out given file and its group given by {@code matcher}.
	 * 
	 * @param fileName file that is printed
	 * @param matcher  {@link Matcher} that contains groups of given file
	 * @param env      {@link Environment} object used for communication with shell
	 */
	private void printGroupsForFile(String fileName, Matcher matcher, Environment env) {
		StringBuilder groupBuilder = new StringBuilder(fileName);
		for (int i = 0; i <= matcher.groupCount(); i++) {
			groupBuilder.append(" ").append(i).append(": ").append(matcher.group(i));
		}
		env.writeln(groupBuilder.toString());
	}
	
	/**
	 * Method executes show command which prints out filtered file's original name </br> 
	 * and new filename got from given {@code namePattern}.
	 * 
	 * @param sourcePath    directory from which files are filtered
	 * @param filterPattern pattern used for filtering
	 * @param namePattern   pattern used to generate new file name
	 * @param env           {@link Environment} object used for communication with shell
	 * @throws IOException  if reading form directory fails
	 */
	private void runShowCommand(Path sourcePath, String filterPattern, String namePattern, Environment env)
			throws IOException {
		try {
			NameBuilderParser parser = new NameBuilderParser(namePattern);
			NameBuilder builder = parser.getNameBuilder();
			Pattern pattern = Pattern.compile(filterPattern);
			List<Path> filesList = Files.list(sourcePath).collect(Collectors.toList());
			for (Path path : filesList) {
				Matcher matcher = pattern.matcher(path.getFileName().toString());
				if (matcher.find()) {
					NameBuilderInfo info = new NameBuilderInfoImpl(matcher);
					builder.execute(info);
					String newName = info.getStringBuilder().toString();
					printFileRenaming(path.getFileName().toString(), newName, env);
				}
			}
		} catch (NameBuilderParserException | IndexOutOfBoundsException ex) {
			env.writeln(ex.getMessage());
		}
	}
	
	/**
	 * Method prints out old name and newly generated name.
	 * 
	 * @param oldName old file name
	 * @param newName new file name 
	 * @param env     {@link Environment} object used for communication with shell
	 */
	private void printFileRenaming(String oldName, String newName, Environment env) {
		env.writeln(String.format("%s => %s", oldName, newName));
	}
	
	/**
	 * Method runs execute command which copies filtered files </br>
	 * to the new directory {@code destinationPath} with new name </br>
	 * generated using given {@code namePattern}.
	 * 
	 * @param sourcePath      directory from which files are copied
	 * @param destinationPath directory where files are copied
	 * @param filterPattern   pattern used to filter files
	 * @param namePattern     pattern used to generate new file name
	 * @param env             {@link Environment} object used for communication with shell
	 * @throws IOException    if reading or writing to directory fails
	 */
	private void runExecuteCommand(Path sourcePath, Path destinationPath, String filterPattern, String namePattern,
			Environment env) throws IOException {
		try {
			NameBuilderParser parser = new NameBuilderParser(namePattern);
			NameBuilder builder = parser.getNameBuilder();
			Pattern pattern = Pattern.compile(filterPattern);
			List<Path> filesList = Files.list(sourcePath).collect(Collectors.toList());
			for (Path path : filesList) {
				Matcher matcher = pattern.matcher(path.getFileName().toString());
				if (matcher.find()) {
					NameBuilderInfo info = new NameBuilderInfoImpl(matcher);
					builder.execute(info);
					String newName = info.getStringBuilder().toString();
					String sourcePathName = sourcePath.getFileName().toString()
							.concat("\\" + path.getFileName().toString());
					String destinationPathName = destinationPath.getFileName().toString().concat("\\" + newName);
					Files.copy(path, destinationPath.resolve(newName));
					printFileRenaming(sourcePathName, destinationPathName, env);
				}
			}
		} catch (NameBuilderParserException | IndexOutOfBoundsException ex) {
			env.writeln(ex.getMessage());
		}
	}
	
	/**
	 * Method filters files from given {@code sourcePath} using pattern {@code patternString}
	 * and returns list of files that remained after filtering.
	 * 
	 * @param sourcePath    directory from which files are filtered
	 * @param patternString pattern used to filter files
	 * @return              list of filtered files
	 * @throws IOException  if reading from directory fails
	 */
	private List<Path> getFilteredFiles(Path sourcePath, String patternString) throws IOException {
		List<Path> filteredFilesList = new ArrayList<>();
		Pattern pattern = Pattern.compile(patternString);
		List<Path> filesList = Files.list(sourcePath).collect(Collectors.toList());
		for (Path path : filesList) {
			Matcher matcher = pattern.matcher(path.getFileName().toString());
			if (matcher.find()) {
				filteredFilesList.add(path);
			}
		}
		return filteredFilesList;
	}
	
	/**
	 * Implementation of {@link NameBuilderInfo} used for generating new file names.
	 * 
	 * @author Ante Gazibaric
	 * @version 1.0
	 *
	 */
	private static class NameBuilderInfoImpl implements NameBuilderInfo {

		/** StringBuilder used for concatenating parts of name */
		private StringBuilder sb;
		/** Matcher used for providing groups of file name */
		private Matcher matcher;
		
		/**
		 * Constructor that creates new {@link NameBuilderInfoImpl} object.
		 * 
		 * @param matcher {@link #matcher}
		 */
		public NameBuilderInfoImpl(Matcher matcher) {
			this.sb = new StringBuilder();
			this.matcher = matcher;
		}
		@Override
		public StringBuilder getStringBuilder() {
			return sb;
		}

		@Override
		public String getGroup(int index) {
			return matcher.group(index);
		}
		
	}

}
