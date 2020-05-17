package hr.fer.zemris.java.hw07.shell;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

import hr.fer.zemris.java.hw07.shell.commands.CatCommand;
import hr.fer.zemris.java.hw07.shell.commands.CdCommand;
import hr.fer.zemris.java.hw07.shell.commands.CharsetsCommand;
import hr.fer.zemris.java.hw07.shell.commands.CopyCommand;
import hr.fer.zemris.java.hw07.shell.commands.CptreeCommand;
import hr.fer.zemris.java.hw07.shell.commands.DropdCommand;
import hr.fer.zemris.java.hw07.shell.commands.ExitShellCommand;
import hr.fer.zemris.java.hw07.shell.commands.HelpCommand;
import hr.fer.zemris.java.hw07.shell.commands.HexdumbCommand;
import hr.fer.zemris.java.hw07.shell.commands.ListdCommand;
import hr.fer.zemris.java.hw07.shell.commands.LsCommand;
import hr.fer.zemris.java.hw07.shell.commands.MassrenameCommand;
import hr.fer.zemris.java.hw07.shell.commands.MkdirCommand;
import hr.fer.zemris.java.hw07.shell.commands.PopdCommand;
import hr.fer.zemris.java.hw07.shell.commands.PushdCommand;
import hr.fer.zemris.java.hw07.shell.commands.PwdCommand;
import hr.fer.zemris.java.hw07.shell.commands.RmtreeCommand;
import hr.fer.zemris.java.hw07.shell.commands.SymbolCommand;
import hr.fer.zemris.java.hw07.shell.commands.TreeCommand;

/**
 * MyShell is command-line program. </br>
 * It offers you to perform following commands: </br>
 * charsets, cat, ls, tree, copy, mkdir, hexdump, symbol, help, exit.
 * 
 * @author Ante Gazibarić
 * @version 1.0
 *
 */
public class MyShell {
	
	/** greeting message */
	private static final String greetingMessage = "Welcome to MyShell v 1.0";
	
	/** prompt symbol */
	private static String promptSymbol = "> ";
	
	/** multiline symbol */
	private static String multilineSymbol = "| ";
	
	/** more lines symbol */
	private static String morelinesSymbol = "\\";
	
	/** map of all supported commands */
	private static final SortedMap<String, ShellCommand> commands = new TreeMap<>();
	
	/** flag to distinguish in which mode is Shell */
	private static boolean isMultiline = false;
	
	static {
		commands.put("exit", new ExitShellCommand());
		commands.put("cat", new CatCommand());
		commands.put("charset", new CharsetsCommand());
		commands.put("copy", new CopyCommand());
		commands.put("help", new HelpCommand());
		commands.put("hexdumb", new HexdumbCommand());
		commands.put("ls", new LsCommand());
		commands.put("mkdir", new MkdirCommand());
		commands.put("tree", new TreeCommand());
		commands.put("symbol", new SymbolCommand());
		commands.put("pwd", new PwdCommand());
		commands.put("cd", new CdCommand());
		commands.put("pushd", new PushdCommand());
		commands.put("popd", new PopdCommand());
		commands.put("listd", new ListdCommand());
		commands.put("dropd", new DropdCommand());
		commands.put("rmtree", new RmtreeCommand());
		commands.put("cptree", new CptreeCommand());
		commands.put("massrename", new MassrenameCommand());
	}
	
	/**
	 * Main method. Accepts no arguments.
	 * 
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		
		try (Scanner sc = new Scanner(System.in)){
			startShellProgram(sc);
		}
		
	}
	
	/**
	 * Method that is used to run the program.
	 * 
	 * @param sc {@link Scanner} for user input
	 */
	private static void startShellProgram(Scanner sc) {
		printGreetingMessage();
		Environment env = new EnvironmentImpl(sc);
		ShellStatus status = ShellStatus.CONTINUE;
		String processedInput = "";
		do {
			try {
				if (isMultiline) {
					printMultilineSymbol();
				} else {
					printPromptSymbol();
				}
					
				String userInput = sc.nextLine();
				if (userInput == null)
					break;
				if (userInput.isEmpty() && !isMultiline)
					continue;
				
				processedInput = processedInput.concat(getProcessedInput(userInput));
				if (isMultiline)
					continue;
				
				String commandName = extractCommandName(processedInput);
				String arguments = extractArguments(processedInput);
				ShellCommand command = commands.get(commandName);
				status = command.executeCommand(env, arguments);
				processedInput = "";
				
			} catch (IllegalArgumentException ex) {
				System.out.println(ex.getMessage());
				processedInput = "";
			} catch (ShellIOException ex) {
				System.err.println(ex.getMessage());
				status = ShellStatus.TERMINATE;
			}
		} while (status != ShellStatus.TERMINATE);
	}
	
	/**
	 * Method extracts command name from given user input.
	 * 
	 * @param userInput user input from which command name is extracted
	 * @return command name
	 * @throws IllegalArgumentException if given command name is invalid
	 */
	private static String extractCommandName(String userInput) {
		String[] parts = userInput.trim().split("\\s+");
		if (commands.keySet().contains(parts[0]))
			return parts[0];
		throw new IllegalArgumentException("Invalid command. Was: " + parts[0]);
	}
	
	/**
	 * Method extracts arguments from given user input.
	 * 
	 * @param userInput user input from which arguments are extracted
	 * @return arguments for command
	 */
	private static String extractArguments(String userInput) {
		String[] parts = userInput.trim().split("\\s+");
		return userInput.replace(parts[0], "").trim();
	}
	
	/**
	 * Method used for processing value that follows the rules for multiline input.
	 * 
	 * @param userInput user input that is processed
	 * @return processed user input
	 */
	private static String getProcessedInput(String userInput) {
		userInput = userInput.trim();
		if (userInput.endsWith(morelinesSymbol)) {
			isMultiline = true;
			return userInput.substring(0, userInput.length() - 1);
		}
		isMultiline = false;
		return userInput;
	}
	
	/**
	 * Method prints prompt symbol to standard output
	 */
	private static void printPromptSymbol() {
		System.out.print(promptSymbol);
	}
	
	/**
	 * Method prints multiline symbol to standard output.
	 */
	private static void printMultilineSymbol() {
		System.out.print(multilineSymbol);
	}
	
	/**
	 * Method prints out greeting message to user
	 */
	private static void printGreetingMessage() {
		System.out.println(greetingMessage);
	}
	
	/**
	 * Implementation of {@link Environment} interface used for communication with commands.
	 * 
	 * @author Ante Gazibarić
	 * @version 1.0
	 *
	 */
	private static class EnvironmentImpl implements Environment {

		/** directory of project */
		private static final String CURRENT_DIR = ".";
		/** Scanner with whom object talks through methods */
		private Scanner sc;
		/** path of current directory */
		private Path currentDirectoryPath;
		/** shared data map */
		private Map<String, Object> sharedDataMap = new HashMap<>();
		
		/**
		 * Constructor that creates new {@code EnvironmentImpl} object.
		 * 
		 * @param sc {@link #sc}
		 */
		public EnvironmentImpl(Scanner sc) {
			this.sc = sc;
			currentDirectoryPath = Paths.get(CURRENT_DIR).toAbsolutePath().normalize();
		}
		
		@Override
		public String readLine() throws ShellIOException {
			try {
				return sc.nextLine();
			} catch (NoSuchElementException | IllegalStateException ex) {
				throw new ShellIOException(ex.getMessage());
			}
		}

		@Override
		public void write(String text) throws ShellIOException {
			System.out.print(text);
			
		}

		@Override
		public void writeln(String text) throws ShellIOException {
			System.out.println(text);
		}

		@Override
		public SortedMap<String, ShellCommand> commands() {
			return Collections.unmodifiableSortedMap(commands);
		}

		@Override
		public Character getMultilineSymbol() {
			return multilineSymbol.charAt(0);
		}

		@Override
		public void setMultilineSymbol(Character symbol) {
			multilineSymbol = String.valueOf(symbol) + " ";
			
		}

		@Override
		public Character getPromptSymbol() {
			return promptSymbol.charAt(0);
		}

		@Override
		public void setPromptSymbol(Character symbol) {
			promptSymbol = String.valueOf(symbol) + " ";
			
		}

		@Override
		public Character getMorelinesSymbol() {
			return morelinesSymbol.charAt(0);
		}

		@Override
		public void setMorelinesSymbol(Character symbol) {
			morelinesSymbol = String.valueOf(symbol);	
		}

		@Override
		public Path getCurrentDirectory() {
			return currentDirectoryPath.toAbsolutePath().normalize();
		}

		@Override
		public void setCurrentDirectory(Path path) {
			currentDirectoryPath= Objects.requireNonNull(path, "Path must not be null")
										 .toAbsolutePath().normalize();
		}

		@Override
		public Object getSharedData(String key) {
			if (key == null)
				return null;
			return sharedDataMap.get(key);
		}

		@Override
		public void setSharedData(String key, Object value) {
			if (key == null)
				return;
			sharedDataMap.put(key, value);
		}
		
	}

}
