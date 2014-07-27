package com.mycasino.blackjack;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/**
 * Class that represents the Deck of Cards.
 * @author latha.ravishankar
 */
public class DeckOfCards {

	public static final int MAX_NUM_OF_CARDS_IN_DECK = 52;
	private int numOfCardsLeftInDeck = 0;
	private List<Card> cardDeck = new ArrayList<Card>(52);

	public DeckOfCards() {
		createTheDeck();
	}

	public static void main(String args[]) {
		DeckOfCards deckOfCards = new DeckOfCards();
		deckOfCards.printCardsInDeck();
		for (int i = 0; i < DeckOfCards.MAX_NUM_OF_CARDS_IN_DECK; ++i) {
			Card card = deckOfCards.pickFirstCard();
			System.out.println("Card returned for request = " + i + " is " + card);
			System.out.println("Does card exist in deck? " + deckOfCards.checkIfCardExistsInDeck(card));
		}		
		System.out.println("Number of cards left in deck = " + deckOfCards.getNumberOfCardsLeftInDeck());
	}

	/**
	 * The Deck of Cards is created and is shuffled for randomness.
	 */
	public void createTheDeck() {
		numOfCardsLeftInDeck = 0;
		cardDeck.clear();
		for (Card.Suit suit: Card.Suit.values()) {
			for (Card.Rank rank: Card.Rank.values()) {
				cardDeck.add(new Card(suit, rank));
				++numOfCardsLeftInDeck;
			}
		}
		if (numOfCardsLeftInDeck != MAX_NUM_OF_CARDS_IN_DECK) {
			System.out.println("Card Deck not Complete");
		}
		Collections.shuffle(cardDeck);
	}

	public int getNumberOfCardsLeftInDeck() {
		return numOfCardsLeftInDeck;
	}

	public void printCardsInDeck() {
		int i = 0;
		for (Card card: cardDeck) {
			System.out.println("Card at index - " + i + " is " + card.toString());
			++i;
		}
	}

	/**
	 * Return the top card in the deck.
	 * @return the first card in the Deck. null, if the deck has been played out.
	 */
	public Card pickFirstCard() {
		if (numOfCardsLeftInDeck == 0) {
			System.out.println("Card Deck is empty. Return null");
			return null;
		}
		--numOfCardsLeftInDeck;
		return cardDeck.remove(0);
	}

	/**
	 * Utility method to check if a specific card exists in the Deck.
	 * @param card Card to check
	 * @return true if Card is in the deck, else false.
	 */
	private boolean checkIfCardExistsInDeck(Card card) {
		for (Card cardInDeck: cardDeck) {
			if (cardInDeck.equals(card))
				return true;
		}
		return false;
	}
}
class Card {
	/**
	 * Each Card in the Deck - represented by a Suit and Rank.
	 * @author latharavishankar
	 */
	enum Suit {
		DIAMOND, HEARTS, CLUBS, SPADE;
	}

	enum Rank {
		TWO(2, "2"),
		THREE(3, "3"),
		FOUR(4, "4"),
		FIVE(5, "5"),
		SIX(6, "6"),
		SEVEN(7, "7"),
		EIGHT(8, "8"),
		NINE(9, "9"),
		TEN(10, "10"),
		JACK(10, "J"),
		QUEEN(10, "Q"),
		KING(10, "K"),
		ACE(11, "A"); //can be 1 also

		private int value;
		private String representation;
		Rank(int value, String representation) {
			this.value = value;
			this.representation = representation;
		}

		public int getValue() {
			return value;
		}

		public String getRepresentation() {
			return representation;
		}
	}

	private Suit suit;
	private Rank rank;
	public Card(Suit suit, Rank rank) {
		this.suit = suit;
		this.rank = rank;
	}

	public int getValue() {
		return rank.getValue();
	}

	public Rank getRank() {
		return rank;
	}

	public String toString() {
		return (new StringBuffer()
					.append(rank.getRepresentation())
					.append("-")
					.append(suit.toString())
				).toString();
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof Card))
            return false;
        if (obj == this)
            return true;
        Card card = (Card) obj;
        return (card.rank == rank && card.suit == suit);
	}
}