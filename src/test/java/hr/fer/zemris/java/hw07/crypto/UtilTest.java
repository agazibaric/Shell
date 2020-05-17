package hr.fer.zemris.java.hw07.crypto;

import org.junit.Test;
import org.junit.Assert;

public class UtilTest {
	
	@Test
	public void testForHexValue() {
		String s = "01aE22";
		byte[] actuals = Util.hextobyte(s);
		byte[] expecteds = new byte[] {1, -82, 34};
		
		Assert.assertArrayEquals(expecteds, actuals);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testInvalidHexValue() {
		String s = "0aE22";
		byte[] actuals = Util.hextobyte(s);
		byte[] expecteds = new byte[] {1, -82, 34};
		
		Assert.assertArrayEquals(expecteds, actuals);
	}
	
	@Test (expected = NullPointerException.class)
	public void testNullasHex() {
		String s = null;
		byte[] actuals = Util.hextobyte(s);
		byte[] expecteds = new byte[] {1, -82, 34};
		
		Assert.assertArrayEquals(expecteds, actuals);
	}
	
	@Test
	public void testForByteArray() {
		byte[] bytes = new byte[] {1, -82, 34};
		String expected = "01ae22";
		String actual = Util.bytesToHex(bytes);
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test (expected = NullPointerException.class)
	public void testNullAsByteArray() {
		byte[] bytes = null;
		String expected = "01ae22";
		String actual = Util.bytesToHex(bytes);
		
		Assert.assertEquals(expected, actual);
	}
	
}
