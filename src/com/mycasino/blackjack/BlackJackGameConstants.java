package com.mycasino.blackjack;

/**
 * BlackJack Game constants.
 * @author latha.ravishankar
 *
 */
public final class BlackJackGameConstants {

	public final static int NUM_INITIAL_CARDS = 2;
	public final static int BLACKJACK_POINTS = 21;
	public final static int DEALER_TAKES_HIT_UNTIL_THIS = 17;
	private static String validInputValues = "";
	private static String validInputValuesPastFirstTime = "";
	private static String validInputValuesIrresepctiveOfBetAmt = "";

	public enum INPUT {
		DOUBLEDOWN('D', true, false),
		HIT('H', true, true),
		STAND('S', true, true),
		SURRENDER('U', false, true);

		/**
		 * Single character to represent the input.
		 */
		char validChar;
		/**
		 * Boolean to check if this Input can be displayed and accepted past 
		 * the first input from the Player.
		 */
		boolean validPastFirstTime;
		/**
		 * Boolean to check if this Input can be displayed irrespective of the
		 * amount the player has bet. For DoubleDown, the player needs atleast
		 * twice what he/she has bet.
		 */
		boolean validIrresepctiveOfBetAmt;

		INPUT(char validChar, boolean validPastFirstTime, boolean validIrresepctiveOfBetAmt) {
			this.validChar = validChar;
			this.validPastFirstTime = validPastFirstTime;
			this.validIrresepctiveOfBetAmt = validIrresepctiveOfBetAmt;
		}

		char getValidChar() {
			return validChar;
		}

		boolean isInputValidPastFirstTime() {
			return validPastFirstTime;
		}

		boolean isInputValidIrresepctiveOfBetAmt() {
			return validIrresepctiveOfBetAmt;
		}

		public String toString() {
			StringBuffer buff = new StringBuffer();
			buff.append(getValidChar()).append("-")
				.append(name());
			return buff.toString();
		}
	}

	public enum STATUS {
		BLACKJACK,
		BUST,
		PUSH,
		SURRENDER,
		WIN;
	}

	static {
		INPUT[] values = INPUT.values();
		StringBuffer bufferAllValuesForInput = new StringBuffer();
		StringBuffer bufferValidPastFirstTime = new StringBuffer();
		StringBuffer bufferValidIrrespectiveOfBetAmt = new StringBuffer();
		for (INPUT value: values) {
			bufferAllValuesForInput.append(value.toString()).append(" / ");
			if (value.isInputValidPastFirstTime()) {
				bufferValidPastFirstTime.append(value.toString()).append(" / ");
			}
			if (value.isInputValidIrresepctiveOfBetAmt()) {
				bufferValidIrrespectiveOfBetAmt.append(value.toString())
											.append(" / ");
			}
		}

		int length = bufferValidPastFirstTime.length();
		if (length > 3) {
			bufferValidPastFirstTime = bufferValidPastFirstTime.delete(length - 3, length - 1);
		}
		validInputValuesPastFirstTime = bufferValidPastFirstTime.toString();
		length = bufferAllValuesForInput.length();
		if (length > 3) {
			bufferAllValuesForInput = bufferAllValuesForInput.delete(length - 3, length - 1);
		}
		validInputValues = bufferAllValuesForInput.toString();
		length = bufferValidIrrespectiveOfBetAmt.length();
		if (length > 3) {
			bufferValidIrrespectiveOfBetAmt = bufferValidIrrespectiveOfBetAmt.delete(length - 3, length - 1);
		}
		validInputValuesIrresepctiveOfBetAmt = bufferValidIrrespectiveOfBetAmt.toString();
	}
	
	public static String validInputValues(boolean firstTime) {
		if (firstTime)
			return validInputValues;
		else
			return validInputValuesPastFirstTime;
	}

	public static String validInputValuesIrresepctiveOfBetAmt() {
		return validInputValuesIrresepctiveOfBetAmt;
	}

	/**
	 * Check the validity of the Input, when the Input is a String, such as HIT/STAND.
	 * @param playerInput String representation of the Input
	 * @param firstTime
	 * @return true/false based on the validity.
	 */
	public static boolean isPlayerInputValid(String playerInput, boolean firstTime) {
		for (INPUT input: INPUT.values()) {
			if (playerInput.equals(input.name())) {
				if (!firstTime) { // we need to do additional check if not first time from player
					if (input.isInputValidPastFirstTime())
						return true;
					else
						return false;
				}
				return true;
			}
		}
		return false;
	}
	/**
	 * Check the validity of the Input, when the Input is a character, such as H / S.
	 * @param playerInput character representation of the Input
	 * @param firstTime
	 * @return true/false based on the validity.
	 */
	public static boolean isPlayerInputValid(char playerInput, boolean firstTime) {
		for (INPUT input: INPUT.values()) {
			if (playerInput == input.getValidChar()) {
				if (!firstTime) { // we need to do additional check if not first time from player
					if (input.isInputValidPastFirstTime())
						return true;
					else
						return false;
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * Get the INPUT enum for the String input
	 * @param playerInput
	 * @return enum INPUT representation.
	 */
	public static INPUT getInputForString(String playerInput) {
		for (INPUT input: INPUT.values()) {
			if (playerInput.equals(input.name())) {
				return input;
			}
		}
		return null;
	}

	/**
	 * Get the INPUT enum for the character input
	 * @param playerInput
	 * @return enum INPUT representation.
	 */
	public static INPUT getInputForChar(char playerInput) {
		for (INPUT input: INPUT.values()) {
			if (playerInput == input.getValidChar()) {
				return input;
			}
		}
		return null;
	}
}