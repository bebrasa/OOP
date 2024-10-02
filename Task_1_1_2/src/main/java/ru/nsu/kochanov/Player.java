package ru.nsu.kochanov;

import java.util.ArrayList;

/**
 * The Player class represents a player in a Blackjack game.
 * It holds the cards the player has, calculates the score,
 * and manages the player's actions such as getting new cards.
 */
public class Player {
    public ArrayList<Card> playerCards;

    /**
     * Constructor to initialize a player with a deck.
     *
     */
    public Player() {
        this.playerCards = new ArrayList<>();
    }

    /**
     * Method to deal cards to the player. If no cards are dealt yet,
     * it deals two cards; otherwise, it adds one more card.
     */
    public void getPlayerCards(Deck deck) {
        if (playerCards.isEmpty()) {
            playerCards.add(deck.takeCard());
            playerCards.add(deck.takeCard());
        } else {
            playerCards.add(deck.takeCard());
        }
    }

    /**
     * Returns the number of cards the player currently holds.
     *
     * @return the number of cards in the player's hand
     */
    public int getPlayerCount() {
        return playerCards.size();
    }

    /**
     * Displays all the cards that the player currently holds in hand.
     *
     * @return a string representing the player's cards
     */
    public String showPlayerCards() {
        StringBuilder cards = new StringBuilder();
        for (Card card : playerCards) {
            cards.append(card.getSuitRank()).append(", "); // Adds each card and delimiter
        }

        // Remove the last delimiter if the collection is not empty
        if (cards.length() > 0) {
            cards.setLength(cards.length() - 2); // Remove the last ', '
        }

        return cards.toString(); // Return the formatted string
    }

    /**
     * Displays the last card drawn by the player.
     *
     * @return a string representing the last card in the player's hand
     */
    public String showLastCard() {
        return playerCards.get(getPlayerCount() - 1).getSuitRank();
    }

    /**
     * Calculates the score for the given list of cards.
     *
     * @param cards the cards to calculate the score for
     * @return the total score based on the cards
     */
    public int calcScore(ArrayList<Card> cards) {
        int score = 0;
        int acesCount = 0;

        for (Card card : cards) {
            String rank = card.getRank();
            switch (rank) {
                case "2":
                case "3":
                case "4":
                case "5":
                case "6":
                case "7":
                case "8":
                case "9":
                    score += Integer.parseInt(rank);
                    break;
                case "10":
                case "J":
                case "Q":
                case "K":
                    score += 10;
                    break;
                case "A":
                    acesCount++;
                    break;
            }
        }
        for (int i = 0; i < acesCount; i++) {
            if (score + 11 <= 21) {
                score += 11;
            } else {
                score += 1;
            }
        }
        return score;
    }

    /**
     * Returns the total score of the player's hand.
     *
     * @return the player's current score
     */
    public int playerScore() {
        return calcScore(playerCards);
    }
}

/**
 * The Dealer class represents the dealer in a Blackjack game.
 * It extends the Player class and adds specific behavior such
 * as hiding a card from the player.
 */
class Dealer extends Player {

    /**
     * Constructor to initialize a dealer with the same deck as the player.
     */
    public Dealer() {
        super();  // Передаем ту же самую колоду
    }

    /**
     * Displays the dealer's cards, with one card hidden.
     *
     * @return a string representing the dealer's visible cards
     */
    @Override
    public String showPlayerCards() {
        if (getPlayerCount() > 1) {
            StringBuilder cards = new StringBuilder();
            for (int i = 0; i < getPlayerCount() - 1; i++) {
                cards.append(playerCards.get(i).getSuitRank()).append(", ");
            }
            cards.append("<скрытая карта>");
            return cards.toString();
        }
        return super.showPlayerCards();
    }

    /**
     * Displays all of the dealer's cards, including the hidden one.
     *
     * @return a string representing all of the dealer's cards
     */
    public String showAllCards() {
        return super.showPlayerCards();
    }
}