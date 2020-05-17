package hr.fer.zemris.java.hw07.crypto;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Scanner;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Program allows user to calculate and check SHA-256 file digest </br>
 * or to encrypt or decrypt given file using AES crypto-algorithm. </p>
 * 
 * If file digest is performed, program needs two arguments: </br>
 * first is file digest command "checksha" and second is file path that is digested. </p>
 * 
 * If encrypting or decrypting is performed, program expects three arguments: </br>
 * first is command "encrypt" or "decrypt" that decides what will be performed, </br>
 * second is source file path and third is direction file path.
 * 
 * @author Ante Gazibarić
 * @version 1.0
 *
 */
public class Crypto {
	
	/** sha command */
	private static final String checksha = "checksha";
	/** encrypt command */
	private static final String encryptString = "encrypt";
	/** decrypt command */
	private static final String dencryptString = "decrypt";
	/** prompt symbol */
	private static final String promptSymbol = "> ";
	/**
	 * Message format used for getting user input if sha is performed 
	 */
	private static final String shaUserInputMessageFormat = "Please provide expected sha-256 digest for %s:";
	/**
	 * Message format used if sha was successful
	 */
	private static final String shaSuccessfulMessageFormat = "Digesting completed. Digest of %s matches expected digest.";
	/**
	 * Message format used if sha failed
	 */
	private static final String shaFailureMessageFormat = "Digesting completed. Digest of %s does not match the expected digest.%n"
			     										+ "Digest was: %s";
	/** 
	 * Name of sha algorithm that is used 
	 */
	private static final String shaAlgorithm = "SHA-256";
	/**
	 * Message that is printed when password form user is needed
	 */
	private static final String passwordUserInputMessage = "Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):";
	/**
	 * Message that is printed when vector from user is needed
	 */
	private static final String vectorUserInputMessage = "Please provide initialization vector as hex-encoded text (32 hex-digits):";
	/**
	 * Encryption or Decryption format used when process is done
	 */
	private static final String cryptionMessageFormat = "%s completed. Generated file %s based on file %s.%n";
	
	/**
	 * Main method that accepts two or three arguments,
	 * otherwise it exits program.
	 * 
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		
		int numOfArg = args.length;
		
		if(numOfArg < 2 && numOfArg > 3) {
			System.err.println("Invalid number of arguments. Was: " + numOfArg);
			return;
		}
		
		try(Scanner sc = new Scanner(System.in)) {
			if (numOfArg == 2) {
				checkArgument(args[0], checksha);
				
				String fileToDigest = args[1];
				String message = String.format(shaUserInputMessageFormat, fileToDigest);
				String userDigest = getUserInput(sc, message);
				
				Path path = Paths.get(fileToDigest);
				byte[] digestedFile = digestFile(path, shaAlgorithm);
				String hexValue = Util.bytesToHex(digestedFile);
				
				analyzeDigests(hexValue, userDigest, fileToDigest);
			} else {
				boolean toEnrypt;
				String command = args[0];
				if (command.equals(encryptString)) {
					toEnrypt = true;
				} else if (command.equals(dencryptString)) {
					toEnrypt = false;
				} else {
					System.err.println("Invalid argument input. Was: " + command);
					return;
				}
				cryptFile(args[1], args[2], toEnrypt, sc);
			}
		} catch(IllegalArgumentException | IOException | NoSuchAlgorithmException | 
				InvalidKeyException | InvalidAlgorithmParameterException | BadPaddingException |
				NoSuchPaddingException | IllegalBlockSizeException  ex) {
			System.err.println(ex.getMessage());
			return;
		}
	}
	
	/**
	 * Method encrypts or decrypts given {@code sourceFile} 
	 * and writes out resulting file to the given {@code destinationFile}.
	 * It decides whether to encrypt or decrypt from given {@code toEnrypt} boolean expression.
	 * 
	 * @param sourceFile			 file that is encrypted or decrypted
	 * @param destinationFile		 resulting file
	 * @param toEncrypt				 boolean expression which decides whether to encrypt or decrypt
	 * @param sc					 {@link Scanner} used for getting user input
	 * @throws InvalidKeyException   if given password is not valid
	 * @throws IOException			 if file failed to open
	 */
	private static void cryptFile(String sourceFile, String destinationFile, boolean toEncrypt, Scanner sc) 
			throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, 
				   NoSuchPaddingException, IOException, IllegalBlockSizeException, BadPaddingException {
		try {
			String keyText = getUserInput(sc, passwordUserInputMessage);
			String ivText = getUserInput(sc, vectorUserInputMessage);
			SecretKeySpec keySpec = new SecretKeySpec(Util.hextobyte(keyText), "AES");
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(Util.hextobyte(ivText));
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(toEncrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, paramSpec);
			
			Path sourcePath = Paths.get(sourceFile);
			Path destinationPath = Paths.get(destinationFile);
			
			try (
				InputStream is = new BufferedInputStream(
						Files.newInputStream(sourcePath, 
								StandardOpenOption.READ));
				OutputStream os = new BufferedOutputStream(
						Files.newOutputStream(destinationPath, 
								StandardOpenOption.WRITE, 
								StandardOpenOption.TRUNCATE_EXISTING, 
								StandardOpenOption.CREATE))) {
				
				byte[] buff = new byte[1024];
				while (true) {
					int r = is.read(buff);
					if (r < 1) break;
					byte[] byteArray = cipher.update(buff, 0, r);
					os.write(byteArray, 0, byteArray.length);
				}
				os.write(cipher.doFinal());
			}
	
			System.out.format(cryptionMessageFormat,
					toEncrypt ? "Encryption" : "Decryption", destinationFile, sourceFile);
			
		} catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchPaddingException |
				IllegalBlockSizeException | BadPaddingException ex) {
			throw new IllegalArgumentException(ex.getMessage());
		}
		
	}
	
	/**
	 * Method gets input from user by using given {@code Scanner}.
	 * 
	 * @param sc      {@link Scanner} from which it reads input
	 * @param message message that prints out to user
	 * @return        user input
	 */
	private static String getUserInput(Scanner sc, String message) {
		System.out.println(message);
		printPromptSymbol();
		String userInput = sc.nextLine();
		if (userInput == null)
			throw new NullPointerException("Input must not be null");
		
		return userInput;
	}

	/**
	 * Method does message digestion from given path.
	 * 
	 * @param path         				path to the file that is digested
	 * @param shaAlgorithm			    name of the algorithm used in digestion
	 * @return             				byte array that represents digested message
	 * @throws IOException 				if file failed to open
	 * @throws NoSuchAlgorithmException if given algorithm is unsupported
	 */
	private static byte[] digestFile(Path path, String shaAlgorithm) throws IOException, NoSuchAlgorithmException {
		MessageDigest mDigest = MessageDigest.getInstance(shaAlgorithm);
		
		try (InputStream is = new BufferedInputStream(Files.newInputStream(path, StandardOpenOption.READ))) {
			byte[] buff = new byte[1024];
			while (true) {
				int r = is.read(buff);
				if (r < 1) break;
				mDigest.update(buff, 0, r);
			}
		} catch (IOException ex) {
			throw new IOException(ex.getMessage());
		}
		return mDigest.digest();
	}
	
	/**
	 * Method that compares given hex message digests 
	 * and prints out the result of comparison.
	 * 
	 * @param expected expected message digest
	 * @param actual   actual message digest
	 * @param fileName name of file that for printing the resulting message
	 */
	private static void analyzeDigests(String expected, String actual, String fileName) {
		if (expected.equals(actual)) {
			System.out.format(shaSuccessfulMessageFormat, fileName);
		} else {
			System.out.format(shaFailureMessageFormat, fileName, expected);
		}
	}
	
	/**
	 * Method used for comparing given argument string with expected command string. 
	 * It prints out suited message if comparison fails.
	 * 
	 * @param actual                    argument that is provided by user
	 * @param expected                  expected argument from user
	 * @throws IllegalArgumentException if comparison fails
	 */
	private static void checkArgument(String actual, String expected) throws IllegalArgumentException {
		if(!actual.equals(expected)) {
			String message = String.format("Command you entered is not valid.%n"
										 + "Was: %s%nExpected: %s", actual, expected);
			throw new IllegalArgumentException(message);
		}
		
	}
	
	/**
	 * Method prints out prompt symbol
	 */
	private static void printPromptSymbol() {
		System.out.print(promptSymbol);
	}	

}
