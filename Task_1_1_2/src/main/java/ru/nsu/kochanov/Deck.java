package ru.nsu.kochanov;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The Deck class represents a standard deck of playing cards used in Blackjack.
 * It allows for creating a deck, shuffling the deck, and drawing cards from it.
 */
public class Deck {

    private List<Card> cards;

    /**
     * Constructor for Deck. It initializes the deck with 52 cards (13 ranks in 4 suits).
     */
    public Deck() {
        cards = new ArrayList<>();

        // Define ranks and suits
        String[] ranks = new String[] {"2", "3", "4", "5", "6", "7", "8", "9", "J", "Q", "K", "A"};
        String[] suits = new String[] {"червы", "трефы", "пики", "бубны"};

        // Populate the deck with 52 cards (all combinations of ranks and suits)
        for (String suit : suits) {
            for (String rank : ranks) {
                cards.add(new Card(rank, suit));
            }
        }
    }

    /**
     * Shuffles the deck using the Fisher-Yates algorithm.
     * It randomly rearranges the cards in the deck.
     */
    public void shuffle() {
        Random rand = new Random();
        for (int i = cards.size() - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            Card temp = cards.get(i);
            cards.set(i, cards.get(j));
            cards.set(j, temp);
        }
    }

    /**
     * Takes a card from the top of the deck.
     * If the deck is not empty, removes and returns the top card; otherwise, returns null.
     *
     * @return the card from the top of the deck or null if the deck is empty
     */
    public Card takeCard() {
        if (cards.size() > 0) {
            return cards.remove(cards.size() - 1);
        }
        return null;
    }
}