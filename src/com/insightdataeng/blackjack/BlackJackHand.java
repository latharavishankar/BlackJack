package com.insightdataeng.blackjack;

import java.util.ArrayList;
import java.util.List;

/**
 * A hand that plays a game of BlackJack. This is for the Dealer.
 * @author latha.ravishankar
 *
 */
public class BlackJackHand {

	private BlackJackGameConstants.STATUS playerStatus;
	protected List<Card> cardsInHand = new ArrayList<Card>();

	public BlackJackHand() {
	}

	public BlackJackGameConstants.STATUS getPlayerStatus() {
		return playerStatus;
	}

	public void setPlayerStatus(BlackJackGameConstants.STATUS status) {
		this.playerStatus = status;
	}

	public void addToCardsInHand(Card card) {
		cardsInHand.add(card);
	}

	public String printCardsFormatted() {
		StringBuffer buffer = new StringBuffer(60);
		buffer.append(printCards());
		int charsUsed = buffer.length();
		for (int k = charsUsed; k < buffer.capacity(); k++) {
			buffer.append(" ");
		}
		return buffer.toString();
	}

	public String printCards() {
		StringBuffer buffer = new StringBuffer();
		for (int i= 0; i < cardsInHand.size(); i++) {
			buffer.append(cardsInHand.get(i).toString());
			if (i != cardsInHand.size() - 1) 
				buffer.append(", ");
		}
		return buffer.toString();
	}

	/**
	 * Get the value of the cards in hand. Ace is considered a 11 for the first
	 * card. If the second card is an Ace, it's value is 1, if the first card
	 * was an Ace as well. At all other times, the value is 1. 
	 * @return
	 */
	public int getValueOfCardsInHand() {
		int valueOfCardsInHand = 0;
		for (int i= 0; i < cardsInHand.size(); i++) {
			Card currentCard = cardsInHand.get(i);
			int currentCardValue = currentCard.getValue();
			if (currentCard.getRank() == Card.Rank.ACE) {
				switch (i) {
					case 0: currentCardValue = 11;
							break;
					case 1: if (valueOfCardsInHand == 11) //if first and second card = ACE, make second card value=1,
								currentCardValue = 1;
							break;
					default: currentCardValue = 1;
							break;
				}
			}
			valueOfCardsInHand = valueOfCardsInHand + currentCardValue;
		}
		return valueOfCardsInHand;
	}

	/**
	 * Reset Player for a fresh round.
	 */
	public void resetPlayer() {
		playerStatus = null;
		cardsInHand.clear();
	}

	public String printFirstCardFormatted() {
		StringBuffer buffer = new StringBuffer(60);
		buffer.append(getFirstCard().toString());
		int charsUsed = buffer.length();
		for (int k = charsUsed; k < buffer.capacity(); k++) {
			buffer.append(" ");
		}
		return buffer.toString();
	}

	/**
	 * Get the first card from the cards in hand.
	 * @return Card the first card that was dealt to this hand.
	 */
	public Card getFirstCard() {
		if (cardsInHand.size() >= 1) {
			return cardsInHand.get(0);
		}
		else {
			return null;
		}
	}
}