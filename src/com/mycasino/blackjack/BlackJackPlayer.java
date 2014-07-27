package com.mycasino.blackjack;

/**
 * A hand that plays a game of BlackJack. This is for the Player, with more
 * features related to the bet placed, input from the player, etc.
 * @author latha.ravishankar
 *
 */
public class BlackJackPlayer extends BlackJackHand {
	private final int INITIAL_PLAYER_CHIP_COUNT = 100;
	protected int chipCount = 0;
	protected int playersBetForThisRound = 0;
	private BlackJackGameConstants.INPUT playerInput;

	public BlackJackPlayer() {
		super();
		this.chipCount = INITIAL_PLAYER_CHIP_COUNT;
	}

	public BlackJackGameConstants.INPUT getPlayerInput() {
		return playerInput;
	}

	public void setPlayerInput(BlackJackGameConstants.INPUT input) {
		this.playerInput = input;
	}

	public int getChipCount() {
		return chipCount;
	}

	/**
	 * Increase the chipCount by the bet amount as the player won the round
	 */
	public void playerWonThisRound() {
		chipCount = chipCount + playersBetForThisRound; //assuming that player can go over 100 once he/she starts winning.
	}
	
	/**
	 * Decrease the chipCount by the bet amount as the player lost the round
	 */
	public void playerLostThisRound() {
		chipCount = chipCount - playersBetForThisRound;
	}

	public boolean playerHasChipsLeft() {
		return chipCount > 0;
	}

	public boolean doesPlayerHaveEnoughChipsLeftForBet(int chipAmtThatHasBeenBet) {
		return chipCount >= chipAmtThatHasBeenBet;
	}

	public void setPlayersBetForThisRound(int playersBetForThisRound) {
		this.playersBetForThisRound = playersBetForThisRound;
	}

	public int getPlayersBetForThisRound() {
		return playersBetForThisRound;
	}

	public void resetPlayer() {
		super.resetPlayer();
		playerInput = null;
		playersBetForThisRound = 0;
		cardsInHand.clear();
	}
}
