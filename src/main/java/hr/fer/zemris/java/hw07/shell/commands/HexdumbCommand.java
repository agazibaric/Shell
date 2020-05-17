package hr.fer.zemris.java.hw07.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.hw07.crypto.Util;
import hr.fer.zemris.java.hw07.shell.Environment;
import hr.fer.zemris.java.hw07.shell.ShellCommand;
import hr.fer.zemris.java.hw07.shell.ShellIOException;
import hr.fer.zemris.java.hw07.shell.ShellStatus;
import hr.fer.zemris.java.hw07.shell.commands.tools.CommandTools;

/**
 * Command that prints out hex content of given file.
 * 
 * @author Ante Gazibaric
 * @version 1.0
 *
 */
public class HexdumbCommand implements ShellCommand {

	/** name of command */
	private static final String COMMAND_NAME = "hexdumb";
	/** 
	 * symbol used for concatenation of hex value to right length
	 * with upper case hex char to distinguish between all lower cased chars used in hexdumb
	 */
	private static final String REPLACEMENT_HEXCHAR = "A";
	/** command description */
	private static final String COMMAND_DESCRIPTION = 
			"HEXDUMB command prints out hex content of given file.\n" +
			"Command accepts single argument that is path to the file whose hex content is printed.";
	
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		Objects.requireNonNull(env, "Environment must not be null");
		
		try {
			String[] argumentsParts = CommandTools.getArguments(arguments, true);
			if (argumentsParts.length != 1) 
				throw new IllegalArgumentException("Invalid number of arguments. Expected: 1. Was: " + argumentsParts.length);
			
			Path path = env.getCurrentDirectory().resolve(Paths.get(argumentsParts[0]));
			CommandTools.checkPathForFile(path);
			executeHexdumbCommand(path);
			
		} catch (IllegalArgumentException | NullPointerException ex) {
			env.writeln(ex.getMessage());
		} catch (IOException ex) {
			env.writeln("Failed to open given file");
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
	 * Method executes hexdumb command.
	 * 
	 * @param path         path of file whose hex content is printed
	 * @throws IOException if failed to open file
	 */
	private void executeHexdumbCommand(Path path) throws IOException {
		byte[] bytes = Files.readAllBytes(path);
		for (int row = 0, n = bytes.length; row < n; row += 16) {
			String hexValue1 = getHexValue(bytes, row, row + 16);
			String hexValue = fillHexStringToValidLength(hexValue1);
			String firstPart = getValidFilledHexValue(separateHexValues(hexValue.substring(0, hexValue.length() / 2))).toUpperCase();
			String secondPart = getValidFilledHexValue(separateHexValues(hexValue.substring(hexValue.length() / 2))).toUpperCase();
			String actualString = getActualContentFromHex(hexValue);
			String rowString = getRowAsString(row);
			System.out.format("%8s: %24s|%24s | %16s%n", rowString, firstPart, secondPart, actualString);
		}				
	}
	
	/**
	 * Method that transforms given hexadecimal value into ASCII characters.
	 * If given hex value is not valid ASCII character it prints "." on that place.
	 * 
	 * @param hexValue hexadecimal value which is processed
	 * @return         ASCII value string of given {@code hexValue}
	 */
	private String getActualContentFromHex(String hexValue) {
		StringBuilder actualValueSB = new StringBuilder();
		for (int i = 0; i < hexValue.length(); i += 2) {
			String str = hexValue.substring(i, i + 2);
			int intValue = Integer.parseInt(str, 16);
			if (intValue < 32 || intValue > 127) {
				actualValueSB.append(".");
			} else {
				actualValueSB.append((char) intValue);
			}
		}
		return actualValueSB.toString();
	}
	
	/**
	 * Method takes number and transforms it into valid string format for hexdumb command.
	 * 
	 * @param row number of row
	 * @return    hexadecimal string representation of given row
	 */
	private String getRowAsString(int row) {
		String hexRow = Integer.toHexString(row);
		int numOfZerosToAdd = 8 - hexRow.length();
		return getStringWithZeros(numOfZerosToAdd).concat(hexRow).toUpperCase();
	}
	
	/**
	 * Method returns string with {@code number} of zeros in it.
	 * 
	 * @param number number of zeros that is concatenated
	 * @return       string that contains given number of zeros
	 */
	private String getStringWithZeros(int number) {
		StringBuilder zeros = new StringBuilder();
		for (int i = 0; i < number; i++) {
			zeros.append("0");
		}
		return zeros.toString();
	}
	
	/**
	 * Method separates given hex values with one space between them.
	 * 
	 * @param hexValue string that is managed
	 * @return         string which contains space between every hex value of given string
	 */
	private String separateHexValues(String hexValue) {
		return hexValue.replaceAll("..(?!$)", "$0 ");
	}
	
	/**
	 * Method replaces all concatenated {@link #REPLACEMENT_HEXCHAR} with whitespaces.
	 * 
	 * @param value hexadecimal value that is processed
	 * @return      valid hexadecimal value
	 */
	private String getValidFilledHexValue(String hexValue) {
		return hexValue.replace(REPLACEMENT_HEXCHAR, " ");
	}
	
	/**
	 * Method fills hex value to the valid length with {@link #REPLACEMENT_HEXCHAR}.
	 * 
	 * @param value hexadecimal value that is processed
	 * @return      valid length hexadecimal value
	 */
	private String fillHexStringToValidLength(String hexValue) {
		int length = 32 - hexValue.length();
		StringBuilder hexSB = new StringBuilder(hexValue);
		for (int i = 0; i < length; i++) {
			hexSB.append(REPLACEMENT_HEXCHAR);
		}
		return hexSB.toString();
	}
	
	/**
	 * Method returns hexadecimal string representation of given byte array.
	 * 
	 * @param array byte array whose content is transformed
	 * @param start starting index of array
	 * @param end   ending index of array
	 * @return      hexadecimal string representation of given byte array.
	 */
	private String getHexValue(byte[] array, int start, int end) {
		if (array.length < end) {
			end = array.length;
		}
		byte[] newArray = new byte[end - start];
		for (int i = 0; start < end; i++, start++) {
			newArray[i] = array[start];
		}
		return Util.bytesToHex(newArray);	
	}

}
