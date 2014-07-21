package com.insightdataeng.blackjack;

import java.io.Console;

/**
 * @author latha.ravishankar
 * BlackJack game.
 * 
 * The way you play the game - 
 * java com.insightdataeng.blackjack.BlackJackGame
 * 
 * The following features are implemented - 
 * There is 1 Dealer and 1 Player, playing with 1 Deck Of Cards. 
 * 
 * DOUBLEDOWN
 * HIT
 * STAND
 * SURRENDER
 * 
 */
public class BlackJackGame {
	private final DeckOfCards deckOfCards;
	private final BlackJackPlayer player;
	private final BlackJackHand dealer;

	public BlackJackGame (DeckOfCards deckOfCards, BlackJackPlayer player, BlackJackHand dealer) {
		this.deckOfCards = deckOfCards;
		this.player = player;
		this.dealer = dealer;
	}

	/** 
	 * Get the player of the game
	 * 
	 * @return BlackJackPlayer
	 */
	public BlackJackPlayer getPlayer() {
		return player;
	}

	/** 
	 * Get the dealer of the game
	 * 
	 * @return BlackJackPlayer
	 */
	public BlackJackHand getDealer() {
		return dealer;
	}

	public static void main(String args[]) {
		BlackJackPlayer player = new BlackJackPlayer();
		BlackJackHand dealer = new BlackJackHand();
		DeckOfCards deckOfCards = new DeckOfCards();
		BlackJackGame game = new BlackJackGame(deckOfCards, player, dealer);		
		System.out.println("*****************************************************************");
		System.out.println("\tGAME OF BLACKJACK STARTING! You can input your \n" +
						   "\tcommand in any case or the first character. \n" +
						   "\tE.g. - HIT/hit/H/h for HIT!");
		System.out.println("*****************************************************************");

		do {
			int playersBetForThisRound = 0;
			do {
				String playerInput = game.getPlayerInputForStartOfGame();
				if (game.doesPlayerWantToQuit(playerInput)) {
					System.out.println("\n*****************************************************************");
					System.out.println("\tGame Ending.. CONGRATULATIONS! \n" +
									   "\tQuitting while ahead, with Chip Count = " + player.getChipCount() );
					System.out.println("*****************************************************************");
					System.exit(-1);
				}
				playersBetForThisRound = game.getPlayerBetInput(playerInput);
			} while (!game.validatePlayerBetInput(playersBetForThisRound));

			game.setPlayersBetForThisRound(playersBetForThisRound);
			System.out.println();
			game.dealInitialTwoCards();
			boolean firstOptionForThisRound = true;
			game.play(firstOptionForThisRound);
			if (!player.playerHasChipsLeft()) {
				System.out.println("*****************************************************************");
				System.out.println("\tGame Ending.. \n" +
								   "\tSorry, No chips left. EXITING!!");
				System.out.println("*****************************************************************");
				System.exit(0);
			}
			game.startANewRound();
		} while(player.playerHasChipsLeft());
	}

	private void printMessage(String messageToPlayer) {
		System.out.println(messageToPlayer);
	}

	/**
	 * Get the player input - bet or Quit for Exiting the game
	 * @return playerInput
	 */
	private String getPlayerInputForStartOfGame() {
		int maxBet = player.getChipCount();
		String message = "YOUR CHIP COUNT - " + maxBet + ", PLACE BET, less than " +
				"or equal to " + maxBet + " (or (Q)UIT) ? ";
		String input = null;
		do {
			input = getInputFromPlayer(message);
		} while (input == null || input.length() == 0);
		return input;
	}

	private String getInputFromPlayer(String message) {
		Console c = System.console();
        if (c == null) {
            System.err.println("No console.");
            System.exit(1);
        }
		return c.readLine("\n>  " + message);
	}

	private boolean doesPlayerWantToQuit(String playerInput) {
		char firstChar = playerInput.charAt(0);
		if (firstChar == 'q' || firstChar == 'Q') {
			return (playerInput.equalsIgnoreCase("QUIT") || playerInput.equalsIgnoreCase("Q"));
		}
		return false;
	}

	private int getPlayerBetInput(String betInput) {
		int chipCountAmt = 0;
		try {
			chipCountAmt = Integer.parseInt(betInput);
		} catch (NumberFormatException nfe) {
			printMessage("Please input a valid number or (Q)UIT to quit");
		}
		return chipCountAmt;
	}

	/**
	 * Validate if player has enough chips to bet for this amount
	 * @param playerBet
	 * @return true/false
	 */
	private boolean validatePlayerBetInput(int playerBet) {
		return (playerBet > 0 && player.doesPlayerHaveEnoughChipsLeftForBet(playerBet));
	}

	private void setPlayersBetForThisRound(int playersBetForThisRound) {
		player.setPlayersBetForThisRound(playersBetForThisRound);
	}

	/**
	 * Deal the first 2 cards of the game for the Player and the Dealer.
	 */
	private void dealInitialTwoCards() {
        for (int i=0; i < BlackJackGameConstants.NUM_INITIAL_CARDS; i++) {
                player.addToCardsInHand(deckOfCards.pickFirstCard());
                dealer.addToCardsInHand(deckOfCards.pickFirstCard());
        }
        printCards(false);
    }

	private void printCards(boolean printAllCardsForDealer) {
		printMessage("YOUR CARDS:\t\t" + player.printCardsFormatted() +
				"\tTOTAL VALUE = " + player.getValueOfCardsInHand());
		if (printAllCardsForDealer)
			printMessage("DEALERS CARD(S):\t" + dealer.printCardsFormatted() + 
					"\tTOTAL VALUE = " + dealer.getValueOfCardsInHand() + "\n");
		else
			printMessage("DEALERS CARD(S):\t" + dealer.printFirstCardFormatted() +
					"\tFIRST CARD's VALUE = " + dealer.getFirstCard().getValue() + "\n");
		printMessage("-------------------------");
	}

	/**
	 * Main method of the game. Checks for an outcome each round (BLACKJACK the 
	 * first time, WIN and LOSE for others) and asks for Player input if the round
	 * did not end.
	 * 
	 * @param firstOptionForPlayer boolean whether the player was dealt the first 
	 * 2 cards and is ready to play the first time for this round
	 */
	private void play(boolean firstOptionForPlayer) {
		evaluateRound(firstOptionForPlayer);
		boolean didThisRoundEnd = didRoundEnd();
		//print cards if input was a hit and previous round did not end.
		if (player.getPlayerInput() == BlackJackGameConstants.INPUT.HIT && !didThisRoundEnd)
			printCards(false);
		String message = "";
		if (!didThisRoundEnd) {
			message = getValidInputsAsString(firstOptionForPlayer);
			BlackJackGameConstants.INPUT playerInput = getPlayerInput(message, firstOptionForPlayer);
			this.printMessage(""); //Print New Line
			firstOptionForPlayer = false;
			dealWithPlayerInput(playerInput);
			play(firstOptionForPlayer);
		}
	}

	/**
	 * Enums representing the outcome of a round.
	 */
	private enum ROUND_OUTCOME {
		PLAYER,
		DEALER,
		BOTH,
		NONE;
	}

	private ROUND_OUTCOME hasAnyoneGotATwentyOne() {
		boolean playerBlackJack = checkIfPlayerHasATwentyOne(player);
		boolean dealerBlackJack = checkIfPlayerHasATwentyOne(dealer);
		return roundOutcome(playerBlackJack, dealerBlackJack);
	}

	private boolean checkIfPlayerHasATwentyOne(BlackJackHand checkPlayer) {
		if (checkPlayer.getValueOfCardsInHand() == BlackJackGameConstants.BLACKJACK_POINTS)
			return true;
		else
			return false;
	}

	private ROUND_OUTCOME roundOutcome(boolean playerOutcome,
			boolean dealerOutcome) {
		if (playerOutcome && dealerOutcome) {
			return ROUND_OUTCOME.BOTH;
		}
		else if (playerOutcome) {
			return ROUND_OUTCOME.PLAYER;
		}
		else if (dealerOutcome) {
			return ROUND_OUTCOME.DEALER;
		}
		else {
			return ROUND_OUTCOME.NONE;
		}
	}

	/**
	 * For a winning (player) outcome of the round, decide if player won / lost 
	 * / nobody won / continue playing the round. Print messages for each outcome.
	 * @param whoWon
	 * @param isBlackJack
	 */
	private void evaluateIfThereIsAWinningOutcome(ROUND_OUTCOME whoWon, boolean isBlackJack) {
		switch (whoWon) {
		case PLAYER:
			playerWon(isBlackJack);
			break;
		case DEALER:
			playerLost(isBlackJack);
			break;
		case BOTH:
			playerPush(isBlackJack);
			break;
		case NONE: //No one wins this round!
			break;
		}
	}

	/**
	 * For a losing (player) outcome of the round, decide if player won / lost 
	 * / nobody won / continue playing the round. Print messages for each outcome.
	 * @param whoBust
	 * @param isBlackJack
	 */
	private void evaluateIfThereIsABustOutcome(ROUND_OUTCOME whoBust, boolean isBlackJack) {
		switch (whoBust) {
		case PLAYER:
			playerLost(false);
			break;
		case DEALER:
			playerWon(false);
			break;
		case BOTH:
			playerLost(false); //if both were above 21, we say Player lost
			break;
		case NONE:
			//continue playing
			break;
		}
	}

	/** 
	 * Was there a status change for the Player? 
	 * @return true - round ended.
	 */
	private boolean didRoundEnd() {
		BlackJackGameConstants.STATUS playerStatus = player.getPlayerStatus();
		return (playerStatus == BlackJackGameConstants.STATUS.BLACKJACK || 
				playerStatus == BlackJackGameConstants.STATUS.WIN || 
				playerStatus == BlackJackGameConstants.STATUS.BUST ||
				playerStatus == BlackJackGameConstants.STATUS.SURRENDER ||
				playerStatus == BlackJackGameConstants.STATUS.PUSH
				);
	}

	private String getValidInputsAsString(boolean firstTimeForPlayer) {
		return BlackJackGameConstants.validInputValues(firstTimeForPlayer);
	}

	/**
	 * Get the INPUT from the player to decide what to do next in this round.
	 * @param message
	 * @param firstTimeForPlayer - based on this INPUT values change.
	 * @return INPUT from the player
	 */
	private BlackJackGameConstants.INPUT getPlayerInput(String message, boolean firstTimeForPlayer) {
		String input = getInputFromPlayer(message + " ? ");
		if (!validatePlayerInput(input, firstTimeForPlayer)) {
			printMessage("Please provide a valid input, valid values are - " + message);
			return getPlayerInput(message, firstTimeForPlayer);
		}
		BlackJackGameConstants.INPUT inputVal = convertStringToInputEnum(input.trim().toUpperCase());
		inputVal = validateAndSetPlayerBetForDD(inputVal, firstTimeForPlayer);
		return inputVal;
	}

	/**
	 * Validate the Player input (make sure that the bet set for this round is 
	 * valid, based on the INPUT). And if it is valid, set the Player's bet.
	 * @param inputVal
	 * @param firstTimeForPlayer - some inputs allowed only for the first round.
	 * @return INPUT from the player
	 */
	private BlackJackGameConstants.INPUT validateAndSetPlayerBetForDD(
			BlackJackGameConstants.INPUT inputVal, boolean firstTimeForPlayer) {
		if (inputVal == BlackJackGameConstants.INPUT.DOUBLEDOWN) {
			if (!doesPlayerHaveEnoughToDoubleBet()) {
				printMessage("You DO NOT have enough chips for a " + inputVal + ", choose something else!");
				return getPlayerInput(BlackJackGameConstants.validInputValuesIrresepctiveOfBetAmt(), firstTimeForPlayer);
			}
			else {
				int playerBet = player.getPlayersBetForThisRound();
				player.setPlayersBetForThisRound(playerBet * 2);
			}
		}
		return inputVal;
	}

	private boolean validatePlayerInput(String playerInput, boolean firstTimeForPlayer) {
		String playerInputUpperCase = null;
		if (playerInput != null && !playerInput.trim().isEmpty()) {
			playerInputUpperCase = playerInput.trim().toUpperCase();
		}
		if (playerInputUpperCase.length() == 1) {
			return BlackJackGameConstants.isPlayerInputValid(
					playerInputUpperCase.charAt(0), firstTimeForPlayer);
		}
		return BlackJackGameConstants.isPlayerInputValid(playerInputUpperCase, 
					firstTimeForPlayer);
	}

	private boolean doesPlayerHaveEnoughToDoubleBet() {
		int playerBet = player.getPlayersBetForThisRound();
		return (player.doesPlayerHaveEnoughChipsLeftForBet(playerBet * 2));
	}

	private BlackJackGameConstants.INPUT convertStringToInputEnum(String playerInput) {
		if (playerInput.length() == 1) {
			return BlackJackGameConstants.getInputForChar(playerInput.charAt(0));
		}
		return BlackJackGameConstants.getInputForString(playerInput);
	}

	/**
	 * Do the following - 
	 * check 1) anybody is at 21 - YES, stop
	 * check 2) check if player chose SURRENDER and act accordingly.
	 * check 3) anybody BUST - YES, stop
	 * check 4) if input was STAND/DOUBLEDOWN, check who won.
	 * check 5) if none of the above, continue playing round.
	 * 
	 * @param blackJackPossible - this boolean is used for print purposes.
	 */
	private void evaluateRound(boolean blackJackPossible) {
		//blackJackPossible - true only with the first 2 cards.
		evaluateIfThereIsAWinningOutcome(hasAnyoneGotATwentyOne(), blackJackPossible);
		if (!blackJackPossible) { //u want to check for other possibilities only when there are more than 2 cards.
			boolean didThisRoundEnd = didRoundEnd();
			if (!didThisRoundEnd) {
				if (player.getPlayerInput() == BlackJackGameConstants.INPUT.SURRENDER) {
					playerChoseSurrender();
					return;
				}
				evaluateIfThereIsABustOutcome(hasAnyoneGoneBust(), blackJackPossible);
				didThisRoundEnd = didRoundEnd();
				if (!didThisRoundEnd) {
					if (player.getPlayerInput() == BlackJackGameConstants.INPUT.STAND ||
							player.getPlayerInput() == BlackJackGameConstants.INPUT.DOUBLEDOWN) {
						evaluateIfThereIsAWinningOutcome(whoHasHigherPoints(), false);
					}
				}
			}
		}
	}

	/**
	 * React to each Player Input.
	 * @param playerInput
	 */
	private void dealWithPlayerInput(BlackJackGameConstants.INPUT playerInput) {
		player.setPlayerInput(playerInput);
		switch (playerInput) {
			case HIT:
				player.addToCardsInHand(deckOfCards.pickFirstCard());
				break;
			case STAND:
				while(dealer.getValueOfCardsInHand() < 
						BlackJackGameConstants.DEALER_TAKES_HIT_UNTIL_THIS) {
					dealer.addToCardsInHand(deckOfCards.pickFirstCard());
				}
				break;
			case DOUBLEDOWN:
				player.addToCardsInHand(deckOfCards.pickFirstCard());
				while(dealer.getValueOfCardsInHand() < 
						BlackJackGameConstants.DEALER_TAKES_HIT_UNTIL_THIS) {
					dealer.addToCardsInHand(deckOfCards.pickFirstCard());
				}
				break;
			case SURRENDER:
				break;
			default:
				System.out.println("INVALID INPUT");
				break;
		}
	}

	private ROUND_OUTCOME hasAnyoneGoneBust() {
		boolean playerIsBust = checkIfPlayerIsBust(player);
		boolean dealerIsBust = checkIfPlayerIsBust(dealer);
		// if we get a response that both are Bust, then we will go with player as Bust
		ROUND_OUTCOME outcome = roundOutcome(playerIsBust, dealerIsBust);
		return outcome;
	}

	private boolean checkIfPlayerIsBust(BlackJackHand checkPlayer) {
		if (checkPlayer.getValueOfCardsInHand() > BlackJackGameConstants.BLACKJACK_POINTS)
			return true;
		else
			return false;
	}

	private ROUND_OUTCOME whoHasHigherPoints() {
		boolean playerHigher = doesPlayerHaveHigherValueThanDealer();
		boolean playersEqual = doesPlayerHaveEqualValueAsDealer();
		if (playersEqual)
			return roundOutcome(true, true);
		else
			return roundOutcome(playerHigher, !playerHigher);
	}

	private boolean doesPlayerHaveHigherValueThanDealer() {
		return player.getValueOfCardsInHand() > dealer.getValueOfCardsInHand();
	}

	private boolean doesPlayerHaveEqualValueAsDealer() {
		return player.getValueOfCardsInHand() == dealer.getValueOfCardsInHand();
	}

	private void playerChoseSurrender() {
		player.setPlayerStatus(BlackJackGameConstants.STATUS.SURRENDER);
		int playerBet = player.getPlayersBetForThisRound();
		player.setPlayersBetForThisRound(playerBet/2);
		player.playerLostThisRound();
		printMessage("You chose to SURRENDER, you lost " + playerBet/2 + ", half your bet (rounded).\n");
	}

	/**
	 * Method to print appropriate messages when the Player Won.
	 * @param isBlackJack
	 */
	private void playerWon(boolean isBlackJack) {
		player.playerWonThisRound();
		String outcome = "";
		dealer.setPlayerStatus(BlackJackGameConstants.STATUS.BUST);
		if (isBlackJack) {
			player.setPlayerStatus(BlackJackGameConstants.STATUS.BLACKJACK);
			outcome = "You have a BLACKJACK. You WIN.";
		}
		else { 	//win here because 1) player has 21, 2) player has higher points
				//than dealer or 3) dealer bust.
			player.setPlayerStatus(BlackJackGameConstants.STATUS.WIN);
			if (player.getValueOfCardsInHand() == BlackJackGameConstants.BLACKJACK_POINTS ||
					player.getValueOfCardsInHand() > dealer.getValueOfCardsInHand()) {
				outcome = "You WIN";
			}
			else {
				outcome = "Dealer BUST. You WIN.";
			}
		}
		printCards(true);
		printMessage(outcome);
		printMessage("\n*************************");
	}

	/**
	 * Method to print appropriate messages when the Player Loses.
	 * @param isBlackJack
	 */
	private void playerLost(boolean isBlackJack) {
		player.playerLostThisRound();
		String outcome = "";
		player.setPlayerStatus(BlackJackGameConstants.STATUS.BUST);
		if (isBlackJack) {
			dealer.setPlayerStatus(BlackJackGameConstants.STATUS.BLACKJACK);
			outcome = "Dealer has a BLACKJACK. You LOSE.";
		}
		else { //lose here because 1) u bust 2) dealer has 21, 3) dealer has higher points
			dealer.setPlayerStatus(BlackJackGameConstants.STATUS.WIN);
			if (player.getValueOfCardsInHand() > BlackJackGameConstants.BLACKJACK_POINTS)
				outcome = "You BUST. You LOSE.";
			else
				outcome = "Dealer WON. You LOSE.";
		}
		printCards(true);
		printMessage(outcome);
		printMessage("\n*************************");
	}

	/**
	 * Method to print appropriate messages when the Player and 
	 * Dealer have the same points (PUSH).
	 * @param isBlackJack
	 */
	private void playerPush(boolean isBlackJack) {
		BlackJackGameConstants.STATUS statusToSet = null;
		String outcome = "";
		if (isBlackJack) {
			outcome = "PUSH. Both You and Dealer have a BLACKJACK. No one wins this round!";
			statusToSet = BlackJackGameConstants.STATUS.PUSH;
		}
		else {
			outcome = "PUSH. Both You and Dealer have the same value. No one wins this round!";
			statusToSet = BlackJackGameConstants.STATUS.PUSH;
		}
		player.setPlayerStatus(statusToSet);
		dealer.setPlayerStatus(statusToSet);
		printCards(true);
		printMessage(outcome);
		printMessage("\n*************************");
	}

	/**
	 * Reset and start a new round
	 */
	private void startANewRound() {
		player.resetPlayer();
		dealer.resetPlayer();
		deckOfCards.createTheDeck();
	}
}