package hr.fer.zemris.java.hw07.crypto;

/**
 * Class that offers two methods used for transforming 
 * hexadecimal value into byte array and vice versa.
 *  
 * @author Ante Gazibarić
 * @version 1.0
 *
 */
public class Util {

	/**
	 * Method transforms given hexadecimal value into byte array
	 * 
	 * @param keyText hexadecimal value that is transformed
	 * @return        byte array representation of given hex value
	 * @throws        IllegalArgumentException if given {@code keyText} is valid hexadecimal value
	 * @throws		  NullPointerException if given {@code keyText} is {@code null}
	 */
	public static byte[] hextobyte(String keyText) {
		if (keyText == null)
			throw new NullPointerException("KeyText must not be null");
		
		try {
			int len = keyText.length();
			byte[] data = new byte[len / 2];
			for (int n = 0; n < len; n += 2) {
				int index = n / 2;
				int firstPart = Character.digit(keyText.charAt(n), 16);
				int secondPart = Character.digit(keyText.charAt(n + 1), 16);
				if (firstPart == -1 || secondPart == -1)
					throw new IllegalArgumentException(keyText + " is not valid hex value.");
				
				data[index] = (byte)((firstPart << 4) + secondPart);
			}
			return data;
		} catch (StringIndexOutOfBoundsException ex) {
			throw new IllegalArgumentException("KeyText must not be odd-sized. You entered: " + keyText);
		}
	}
	
	/**
	 * Method transforms given byte array into hexadecimal representation of given array.
	 * 
	 * @param bytes byte array from which hexadecimal string is created
	 * @return      hexadecimal representation of given byte array
	 * @throws      NullPointerException if given byte array is {@code null}
	 */
	public static String bytesToHex(byte[] bytes) {
		if (bytes == null) 
			throw new NullPointerException("Byte array must not be null");
		
		int length = 2 * bytes.length;
		StringBuilder hexBuilder = new StringBuilder(length);
		for (byte byt : bytes) {
			hexBuilder.append(String.format("%02x", byt));
		}
		
		return hexBuilder.toString();
	}
	
}
