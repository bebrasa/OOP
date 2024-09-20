package ru.nsu.kochanov;

/**
 * The card class is creating a card with a rank and suit.
 */
public class Card {
    private String rank;
    private String suit;


    public Card(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;
    }

    /**
     * This method is returning rank of the card.
     */
    public String getRank() {
        return rank;
    }

    /**
     * This method is returning rank and suit of card.
     */
    public String getSuitRank() {
        return rank + " " + suit;
    }
}
