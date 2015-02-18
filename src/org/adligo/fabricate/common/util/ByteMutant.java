package org.adligo.fabricate.common.util;


/**
 * I think something like this class should be added to 
 * the JSE Standard, as it produces more readable
 * byte manipulation source code.
 * 
 * slot zero is the most significant
 * slot seven is 0 or 1
 * slot six and seven is 0 - 3
 * exc.
 * 
 * @author scott
 *
 */
public class ByteMutant {
	private boolean [] bits = null;
	private byte bigB;
	
	public ByteMutant() {
		clean();
	}
	
	/**
	 * reads in a byte
	 */
	public ByteMutant(int b) {
		this((byte) b);
	}
	
	public ByteMutant(short b) {
		this((byte) b);
	}
	
	public ByteMutant(String onesAndZeros) {
		clean();
		char [] cs = new char[8];
		int padEndIndex = 0;
		if (onesAndZeros.length() < 8) {
			padEndIndex = 8 - onesAndZeros.length();
		}
		char [] inChars = onesAndZeros.toCharArray();
		if (padEndIndex != 0) {
			int inCharsCounter = 0;
			for (int i = 0; i < cs.length; i++) {
				if (i < padEndIndex) {
					cs[i] = '0';
				} else {
					cs[i] = inChars[inCharsCounter++];
				}
			}
		} else {
			cs = onesAndZeros.toCharArray();
		}
		

		for (int i = 0; i < 8; i++) {
			char c = cs[i];
			if (c == '0') {
				bits[i] = false;
			} else {
				bits[i] = true;
			}
		}
	}
	
	public ByteMutant(Byte b) {
		this(b.byteValue());
	}
	public ByteMutant(byte b) {
		clean();
		bigB = b;
		toBooleans(bigB);
	}
	
	/**
	 * copy the right most bits into the left most slots
	 * @param b
	 * @param slots
	 */
	public void copy(int b, int slots) {
		copy((byte) b, (byte) slots);
	}
	
	/**
	 * copy the bits from the right of the input byte b
	 * to the left of the slots for the number of slots
	 * 
	 * @param b
	 * @param slots
	 */
	public void copy(byte b, byte slots) {
		if (slots > 8) {
			slots = 8;
		}
		ByteMutant other = new ByteMutant(b);
		int nextSlot = Math.abs(1 - slots);
		int rightSlot = 7;
		
		for (int i = 0; i < slots; i++) {
			boolean otherValue = other.bits[rightSlot];
			bits[nextSlot] = otherValue;
			nextSlot--;
			rightSlot--;
		}
	}
	
	private void clean() {
		bits = new boolean[] {false, false, false, false, false, false, false, false};
	}
	
	static int unsign(byte p) {
		return p & 0xff;
	}
	
	public int unsigned() {
		int result = 0;
		if (bits[0]) {
			result = 128;
		}
		if (bits[1]) {
			result = result + 64;
		}
		if (bits[2]) {
			result = result + 32;
		}
		if (bits[3]) {
			result = result + 16;
		}
		if (bits[4]) {
			result = result + 8;
		}
		if (bits[5]) {
			result = result + 4;
		}
		if (bits[6]) {
			result = result + 2;
		}
		if (bits[7]) {
			result = result + 1;
		}
		return result;
	}

	public String toOnesAndZeros() {
		char [] chars = new char[8];
		for (int i = 0; i < chars.length; i++) {
			boolean bit = bits[i];
			if (bit) {
				chars[i] = '1';
			} else {
				chars[i] = '0';
			}
		}
		return new String(chars);
	}
	
	private void toBooleans(byte bigB) {
		int result = unsign(bigB);
		if (result >= 128) {
			result = result - 128;
			bits[0] = true;
		}
		if (result >= 64) {
			result = result - 64;
			bits[1] = true;
		}
		if (result >= 32) {
			result = result - 32;
			bits[2] = true;
		}
		if (result >= 16) {
			result = result - 16;
			bits[3] = true;
		}
		if (result >= 8) {
			result = result - 8;
			bits[4] = true;
		}
		if (result >= 4) {
			result = result - 4;
			bits[5] = true;
		}
		if (result >= 2) {
			result = result - 2;
			bits[6] = true;
		}
		if (result >= 1) {
			result = result - 1;
			bits[7] = true;
		}
	}
	
	public void setSlot(int i, boolean p) {
		bits[i] = p;
	}
	
	public void setSlotZero(boolean p) {
		bits[0] = p;
	}
	public void setSlotOne(boolean p) {
		bits[1] = p;
	}
	public void setSlotTwo(boolean p) {
		bits[2] = p;
	}
	public void setSlotThree(boolean p) {
		bits[3] = p;
	}
	public void setSlotFour(boolean p) {
		bits[4] = p;
	}
	public void setSlotFive(boolean p) {
		bits[5] = p;
	}
	public void setSlotSix(boolean p) {
		bits[6] = p;
	}
	public void setSlotSeven(boolean p) {
		bits[7] = p;
	}
	
	public boolean getSlot(int p) {
		return bits[p];
	}
	public boolean getSlotZero() {
		return bits[0];
	}
	public boolean getSlotOne() {
		return bits[1];
	}
	public boolean getSlotTwo() {
		return bits[2];
	}
	public boolean getSlotThree() {
		return bits[3];
	}
	public boolean getSlotFour() {
		return bits[4];
	}
	public boolean getSlotFive() {
		return bits[5];
	}
	public boolean getSlotSix() {
		return bits[6];
	}
	public boolean getSlotSeven() {
		return bits[7];
	}
	
	public String toString() {
		StringBuilder out = new StringBuilder();
		for (int i = 0; i < bits.length; i++) {
			if (bits[i]) {
				out.append("1");
			} else {
				out.append("0");
			}
		}
		
		return out.toString();
	}
	
	public byte toByte() {
		String ozs = toOnesAndZeros();
		byte b = (byte) Integer.parseInt(ozs, 2);
		return b;
	}
	
	public char toCharAsciiUtf8() {
		return (char) toByte();
	}
	
	/**
	 * 
	 * @return the number of bytes for 
	 * this character from the first byte 
	 * (represented by this) of a 
	 * utf-8 character.
	 */
	public int getUTF8_BytesInSequence() {
		if (!getSlotZero() || !getSlotOne()) {
			return 1;
		}
		int counter = 1;
		for (int i = 1; i < 6; i++) {
			if (getSlot(i)) {
				counter ++;
			} else {
				break;
			}
		}
		return counter;
	}
}
